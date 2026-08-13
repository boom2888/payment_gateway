import React, { useEffect, useMemo, useState } from "react";
import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";
import CheckoutForm from "@/views/StripePay/CheckoutForm.jsx";
import "./StripePay.less";
import { useTranslation } from "react-i18next";
import { authFetch, BASE_URL } from "@/requests/api";

export default function StripePay(props) {
    const { t } = useTranslation();

    const [clientSecret, setClientSecret] = useState("");
    const [shopStripeAccount, setShopStripeAccount] = useState("");
    const [loading, setLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState("");

    const { orderNo } = props;

    useEffect(() => {
        if (!orderNo) return;

        setLoading(true);
        setErrorMsg("");

        // StripeController exposes this endpoint at /createStripePayment (without /web).
        authFetch(`${BASE_URL}/createStripePayment?orderNo=${encodeURIComponent(orderNo)}`)
            .then((res) => res.json())
            .then((data) => {
                setClientSecret(data.clientSecret || "");
                setShopStripeAccount(data.shopStripeAccount || "");
            })
            .catch((err) => {
                console.error("Create Stripe Payment Error:", err);
                setErrorMsg("Create payment failed");
            })
            .finally(() => {
                setLoading(false);
            });
    }, [orderNo]);

    const stripePromise = useMemo(() => {
        const publicKey = import.meta.env.VITE_STRIPE_PUBLIC_KEY;

        // 普通模式：shopStripeAccount 为空
        if (!shopStripeAccount) {
            return loadStripe(publicKey);
        }

        // Stripe Connect Direct Charge 模式
        return loadStripe(publicKey, {
            stripeAccount: shopStripeAccount,
        });
    }, [shopStripeAccount]);

    if (loading || !clientSecret || !stripePromise) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <div className="loading-text">{t("loadingInProgress")}...</div>
            </div>
        );
    }

    if (errorMsg) {
        return <div className="message">{errorMsg}</div>;
    }

    return (
        <Elements
            stripe={stripePromise}
            options={{
                clientSecret,
                locale: "en",
            }}
        >
            <CheckoutForm clientSecret={clientSecret} />
        </Elements>
    );
}
