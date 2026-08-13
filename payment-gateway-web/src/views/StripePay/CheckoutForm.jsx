import React, { useState } from "react";
import { useStripe, useElements, CardNumberElement, CardExpiryElement, CardCvcElement } from "@stripe/react-stripe-js";
import {useNavigate} from "react-router-dom";

export default function CheckoutForm({ clientSecret }) {
  const stripe = useStripe();
  const elements = useElements();
    const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!stripe || !elements) return;

    setLoading(true);
    setMessage("");

    const cardNumber = elements.getElement(CardNumberElement);

    const result = await stripe.confirmCardPayment(clientSecret, {
      payment_method: { card: cardNumber }
    });

    if (result.error) setMessage(result.error.message);
    else if (result.paymentIntent.status === "succeeded") navigate("/success");;

    setLoading(false);
  };

  return (
      <form className="form" onSubmit={handleSubmit}>
          <div className="form-item">
              <label>Card Number:</label>
              <div className="card-input"><CardNumberElement options={cardStyle}/></div>
          </div>

          <div className="form-item">
              <label>Expiry Date:</label>
              <div className="card-input"><CardExpiryElement options={cardStyle}/></div>
          </div>

          <div className="form-item">
              <label>CVC</label>
              <div className="card-input"><CardCvcElement options={cardStyle}/></div>
          </div>

          <button className="submit-btn" disabled={!stripe || loading}>
              {loading ? "Paying..." : "Pay Now"}
          </button>

          {message && <div className="message">{message}</div>}
      </form>
  );
}

const cardStyle = {
    style: {
        base: {
            fontSize: "16px",
            color: "#333",
            "::placeholder": {color: "#aaa"},
            fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
      letterSpacing: "0.5px",
    },
    locale: 'en',
    invalid: { color: "#ff4d4f" },
  },
};