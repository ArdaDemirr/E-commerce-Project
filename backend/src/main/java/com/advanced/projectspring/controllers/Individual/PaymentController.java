package com.advanced.projectspring.controllers.Individual;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PaymentController {

    @PostMapping("/create-intent")
    @PreAuthorize("hasRole('INDIVIDUAL')")
    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @RequestBody Map<String, Object> body) throws StripeException {

        // amount kuruş/cent cinsindendir: 100 = 1 TL, 15000 = 150 TL
        long amount = Long.parseLong(body.get("amount").toString());

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("try")
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                        .build()
                )
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
    }
}
