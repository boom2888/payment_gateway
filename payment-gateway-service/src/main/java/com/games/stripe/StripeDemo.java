package com.games.stripe;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripeDemo {

    public static void main(String[] args) throws Exception {

        Stripe.apiKey = System.getenv("STRIPE_API_KEY");
        if (Stripe.apiKey == null || Stripe.apiKey.isBlank()) {
            throw new IllegalStateException("STRIPE_API_KEY is required");
        }

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(1000L) // 1000 = $10.00
                        .setCurrency("usd")
                        .addPaymentMethodType("card")
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        System.out.println("PaymentIntent created: " + intent.getId());
    }
}
