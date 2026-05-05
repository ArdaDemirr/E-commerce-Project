import os
import re
from pathlib import Path

# Paths
FRONTEND_DIR = Path(r"c:\Users\ArDe4\GitHub\AdvancedFrontend\frontend\src")
BACKEND_CONTROLLERS_DIR = Path(r"c:\Users\ArDe4\GitHub\ECommerce\backend\src\main\java\com\advanced\projectspring\controllers")
BACKEND_MODELS_DIR = Path(r"c:\Users\ArDe4\GitHub\ECommerce\backend\src\main\java\com\advanced\projectspring\models")
SECURITY_CONFIG_PATH = Path(r"c:\Users\ArDe4\GitHub\ECommerce\backend\src\main\java\com\advanced\projectspring\config\SecurityConfig.java")

with open("security_report.txt", "w", encoding="utf-8") as out:
    out.write("=========================================\n")
    out.write("  E-COMMERCE APP SECURITY & ENDPOINT LOG \n")
    out.write("=========================================\n")

    # 1. Parse Backend Entities
    entities = set()
    if BACKEND_MODELS_DIR.exists():
        for f in BACKEND_MODELS_DIR.rglob("*.java"):
            content = f.read_text(encoding='utf-8')
            if "@Entity" in content:
                entities.add(f.stem)

    out.write(f"\n[INFO] Found Entities: {', '.join(entities)}\n")

    # 2. Parse Backend Controllers for Endpoints and Security Risks
    backend_endpoints = set()
    security_risks = []

    mapping_regex = re.compile(r'@(Get|Post|Put|Delete|Patch|Request)Mapping\b\s*(?:\(([^)]*)\))?')
    class_mapping_regex = re.compile(r'@RequestMapping\s*\(\s*(?:value\s*=\s*)?["\']([^"\']+)["\']')
    method_signature_regex = re.compile(r'public\s+(?:ResponseEntity\s*<\s*(List\s*<\s*)?([A-Za-z0-9_]+)(?:>)?\s*>|([A-Za-z0-9_]+))\s+([A-Za-z0-9_]+)\s*\((.*?)\)')
    request_body_regex = re.compile(r'@RequestBody\s+([A-Za-z0-9_]+)')

    for f in BACKEND_CONTROLLERS_DIR.rglob("*.java"):
        if not f.name.endswith("Controller.java"):
             continue
        content = f.read_text(encoding='utf-8')
        lines = content.split('\n')
        
        class_base_path = ""
        for line in lines:
            m = class_mapping_regex.search(line)
            if m and "class " not in line: 
                class_base_path = m.group(1)
                break
                
        content_cleaned = re.sub(r'//.*', '', content)
        content_cleaned = re.sub(r'/\*.*?\*/', '', content_cleaned, flags=re.DOTALL)
        
        matches = list(re.finditer(r'@(Get|Post|Put|Delete|Patch|Request)Mapping\b\s*(?:\(([^)]*)\))?', content_cleaned))
        
        for match in matches:
            mapping_type = match.group(1).upper()
            args = match.group(2)
            path = ""
            if args:
                m_path = re.search(r'["\']([^"\']+)["\']', args)
                if m_path:
                    path = m_path.group(1)
                    
            full_path = (class_base_path + path).replace('//', '/')
            backend_endpoints.add(full_path)
            
            start_idx = match.end()
            end_idx = content_cleaned.find('{', start_idx)
            if end_idx != -1:
                method_header = content_cleaned[start_idx:end_idx]
                
                if '@RequestBody' in method_header and '@Valid' not in method_header:
                    security_risks.append(f"[{f.name}] Missing @Valid on @RequestBody in endpoint {full_path}")
                    
                sig_match = method_signature_regex.search(method_header)
                if sig_match:
                    return_type = sig_match.group(2) if sig_match.group(2) else sig_match.group(3)
                    if return_type in entities:
                         security_risks.append(f"[{f.name}] Returns Entity '{return_type}' directly in endpoint {full_path} (Data leakage risk - Use a DTO)")

    out.write(f"[INFO] Found {len(backend_endpoints)} Backend Endpoints mappings.\n")

    # 3. Parse Frontend for API calls
    frontend_endpoints = set()

    if FRONTEND_DIR.exists():
        for f in FRONTEND_DIR.rglob("*.ts"):
            content = f.read_text(encoding='utf-8', errors='ignore')
            matches = re.findall(r"(?:http\.(?:get|post|put|delete)|this\.(?:get|post|put|delete)|apiUrl\s*\+\s*)[\(]?\s*[`'\"](/api/[^`'\"\?]+)[`'\"]", content)
            matches2 = re.findall(r"`?(\/api\/[a-zA-Z0-9_/-]+)", content)
            
            for m in matches + matches2:
                base = m.split('${')[0].split('?')[0].rstrip('/')
                if base:
                    frontend_endpoints.add(base)
    else:
        out.write("\n[WARNING] Frontend directory not found! Ensure paths are correct.\n")

    out.write(f"[INFO] Found {len(frontend_endpoints)} unique API path patterns called by Frontend.\n")

    # 4. Compare Endpoints
    out.write("\n[REPORT] Missing/Unmatched Endpoints:\n")
    has_missing = False
    for fe in sorted(frontend_endpoints):
        matched = False
        for be in backend_endpoints:
            be_norm = re.sub(r'\{[^}]+\}', '.*', be)
            if re.match(f"^{be_norm}.*", fe):
                matched = True
                break
            if fe.startswith(be.replace('/*','').replace('/**','')):
                matched = True
                break
                
        if not matched:
            out.write(f"  - [MISSING ENDPOINT] Frontend expects: {fe} (No matching backend endpoint found)\n")
            has_missing = True

    if not has_missing:
        out.write("  [OK] All frontend API calls seem to be supported by the backend!\n")

    # 5. Parse SecurityConfig
    out.write("\n[REPORT] Security Configuration Analysis:\n")
    if SECURITY_CONFIG_PATH.exists():
        sec_content = SECURITY_CONFIG_PATH.read_text(encoding='utf-8')
        if 'hasRole("ADMIN")' not in sec_content and 'hasRole("ROLE_ADMIN")' not in sec_content:
            security_risks.append("[SecurityConfig.java] Missing explicit hasRole('ADMIN') restriction!")
        else:
            out.write("  [OK] Admin roles are explicitly restricted.\n")
            
        if 'hasRole("CORPORATE")' not in sec_content and 'hasRole("ROLE_CORPORATE")' not in sec_content:
            security_risks.append("[SecurityConfig.java] Missing explicit hasRole('CORPORATE') restriction!")
        else:
            out.write("  [OK] Corporate roles are explicitly restricted.\n")
    else:
        out.write("  [FAIL] SecurityConfig.java not found!\n")

    # 6. Display Security Risks
    out.write("\n[REPORT] Security Risks Identified:\n")
    if security_risks:
        for r in security_risks:
            out.write(f"  - [RISK] {r}\n")
    else:
        out.write("  [OK] No structural security risks found (Entities explicitly mapped to DTOs, @Valid used, Admin protected).\n")

    out.write("\n=========================================\n")
    out.write("  ANALYSIS COMPLETE\n")
    out.write("=========================================\n")
    print("Log written to security_report.txt")
