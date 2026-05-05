# HttpOnly Cookie + Refresh Token — Güvenlik Implementasyon Planı

## Durum Takibi (Bu dosyayı güncel tut!)

- [ ] BACKEND — application.properties güncelle
- [ ] BACKEND — JwtUtil.java: generateRefreshToken ekle
- [ ] BACKEND — LoginResponse.java: token alanını kaldır
- [ ] BACKEND — AuthService.java: refresh token üretimi + refreshAccessToken metodu
- [ ] BACKEND — AuthController.java: cookie set etme + /refresh + /logout endpoint
- [ ] BACKEND — JwtAuthFilter.java: cookie'den token oku
- [ ] BACKEND — StoreProductController.java: request attribute ile userId
- [ ] BACKEND — AdminUserController.java: kontrol et/güncelle
- [ ] BACKEND — SecurityConfig.java: /refresh, /logout public yap + PATCH ekle
- [ ] BACKEND — Local commit (push yok)
- [ ] FRONTEND — user.model.ts: AuthResponse'dan token kaldır
- [ ] FRONTEND — token.service.ts: localStorage → sessionStorage, token metodlarını kaldır
- [ ] FRONTEND — auth.service.ts: cookie tabanlı login/logout/refresh
- [ ] FRONTEND — jwt.interceptor.ts: withCredentials:true + 401 refresh akışı
- [ ] FRONTEND — Local commit (push yok)
- [ ] TEST — Backend testleri (curl)
- [ ] TEST — Frontend testleri (Browser DevTools)
- [ ] PUSH — git push origin backend
- [ ] PUSH — git push origin frontend

---

## Mimari Değişim

```
ESKİ AKIŞ:
Login → Backend token'ı body'de döndürür → Frontend localStorage'a yazar
Her istek → Frontend header'a manual ekler: Authorization: Bearer <token>

YENİ AKIŞ:
Login → Backend hem access hem refresh token'ı HttpOnly Cookie olarak SET eder
Her istek → Browser cookie'yi OTOMATIK gönderir (withCredentials: true)
Token bitince → Frontend /api/auth/refresh çağırır → Backend yeni cookie SET eder
Logout → Frontend /api/auth/logout çağırır → Backend her iki cookie'yi de siler
```

---

## Güvenlik Karşılaştırması

| Özellik | Eski Sistem (localStorage) | Yeni Sistem (HttpOnly Cookie) |
|---|---|---|
| XSS saldırısına karşı | ❌ Token çalınabilir | ✅ JS token'a erişemez |
| Token süresi | 24 saat | Access: **15 dk**, Refresh: **7 gün** |
| Logout güvenilirliği | Sadece client-side | Server cookie'yi siler |

---

## BACKEND DEĞİŞİKLİKLERİ (backend branch)

### 1. application.properties
```properties
# DEĞİŞTİR: 86400000 → 900000 (15 dakika)
jwt.expiration=900000
# EKLE: (7 gün)
jwt.refresh-expiration=604800000
```

### 2. JwtUtil.java — Eklenecek metod
```java
@Value("${jwt.refresh-expiration}")
private Long refreshExpiration;

public String generateRefreshToken(String email, Long userId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
}
```

### 3. LoginResponse.java — token alanı kaldırılır
```java
// Kaldırılan: private String token;
// Kalan: role, name, surname, userId
// Constructor güncellenir: token parametresi kaldırılır
```

### 4. AuthService.java — login() ve register() güncellenir
```java
// login() ve register() artık LoginResponse'a token koymaz
// Yeni iç metod: Map<String, String> generateTokenPair(User user)
//   → { "accessToken": "...", "refreshToken": "..." } döndürür
// Controller bu map'i alıp cookie'ye yazar

// YENİ METOD:
public String refreshAccessToken(String refreshToken) {
    if (!jwtUtil.validateToken(refreshToken)) {
        throw new IllegalArgumentException("Invalid refresh token");
    }
    String email = jwtUtil.extractEmail(refreshToken);
    Long userId = jwtUtil.extractUserId(refreshToken);
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
    if (!user.isActive()) {
        throw new AccessDeniedException("Account suspended");
    }
    return jwtUtil.generateToken(email, user.getRole(), userId);
}
```

### 5. AuthController.java — Cookie set etme
```java
// login ve register metodlarında:
private void setCookies(HttpServletResponse response, String accessToken, String refreshToken) {
    // access_token cookie — 15 dakika, tüm path'ler için
    ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
        .httpOnly(true)
        .path("/")
        .maxAge(900)
        .sameSite("Strict")
        .build();
    
    // refresh_token cookie — 7 gün, sadece /api/auth/refresh path'i için
    ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
        .httpOnly(true)
        .path("/api/auth/refresh")
        .maxAge(604800)
        .sameSite("Strict")
        .build();
    
    response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
}

// YENİ ENDPOINT: POST /api/auth/refresh
@PostMapping("/refresh")
public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
    // refresh_token cookie'sini bul
    // authService.refreshAccessToken() çağır
    // yeni access_token cookie set et
}

// YENİ ENDPOINT: POST /api/auth/logout  
@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletResponse response) {
    // Her iki cookie'yi Max-Age=0 ile sil
}
```

### 6. JwtAuthFilter.java — Cookie'den token oku
```java
// Mevcut Authorization header okuma ÖNCE dene → cookie fallback
// Cookie'den access_token bulunursa validate et
// request.setAttribute("userId", userId) ekle — controller'lar bunu kullanacak
// Sonra filterChain.doFilter() çağır
```

### 7. StoreProductController.java — request attribute kullan
```java
// ESKİ: @RequestHeader("Authorization") String authHeader
//        Long userId = jwtUtil.extractUserId(authHeader.substring(7));
// YENİ:  HttpServletRequest request (parametre olarak ekle)
//        Long userId = (Long) request.getAttribute("userId");
```

### 8. AdminUserController.java
```java
// Kontrol et, Authorization header kullanıyorsa aynı değişikliği yap
```

### 9. SecurityConfig.java
```java
// Ekle:
.requestMatchers("/api/auth/refresh").permitAll()
.requestMatchers("/api/auth/logout").permitAll()

// CORS güncelle:
config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
// allowCredentials(true) zaten var ✅
```

---

## FRONTEND DEĞİŞİKLİKLERİ (frontend branch)

### 1. user.model.ts
```typescript
export interface AuthResponse {
    // token: string; ← KALDIRILDI (artık cookie'de)
    role: string;
    name: string;
    surname: string;
    userId: number;
}
```

### 2. token.service.ts
```typescript
// GÜNCELLEME: localStorage → sessionStorage
// KALDIRILANLAR: setTokens(), getAccessToken(), getRefreshToken()
// KALAN: setUser(), getUser(), clear(), isLoggedIn()
// isLoggedIn() → sessionStorage'da user kaydı var mı kontrol eder
```

### 3. auth.service.ts
```typescript
// login(): body'den sadece role, name, surname, userId okur. Token okuma yok.
// logout(): POST /api/auth/logout (withCredentials:true) → tokenService.clear() → router.navigate('/login')
// refreshToken(): POST /api/auth/refresh (withCredentials:true) → yeni cookie backend'den set edilir
```

### 4. jwt.interceptor.ts
```typescript
// KALDIRILDI: Authorization: Bearer header ekleme
// EKLENDİ: withCredentials: true (browser cookie'leri otomatik gönderir)
// EKLENDİ: 401 yakalanınca authService.refreshToken() → retry → başarısız → logout
```

---

## Push Komutları (Testlerden sonra)
```powershell
# Backend push
git checkout backend
git push origin backend

# Frontend push
git checkout frontend
git push origin frontend
```

---

## Test Senaryoları

### Backend (curl ile)
1. Login → Set-Cookie header'larında access_token + refresh_token görünmeli
2. POST /api/auth/refresh → yeni access_token cookie gelmeli
3. POST /api/auth/logout → cookie'ler silinmeli (Max-Age=0)

### Frontend (Browser DevTools)
4. Login → Application > Cookies > access_token (HttpOnly işaretli) görünmeli
5. sessionStorage → sadece user metadata, token YOK
6. 15 dk sonra → interceptor sessizce refresh yapmalı
