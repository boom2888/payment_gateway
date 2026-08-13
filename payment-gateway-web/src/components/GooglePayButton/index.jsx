import React, { useEffect, useRef } from "react";

const GooglePayButton = (props) => {
  const { totalPrice = 0, currencyCode = "USD" } = props;
  const containerRef = useRef(null);

  useEffect(() => {
    if (!window.google) return;

    const paymentsClient = new window.google.payments.api.PaymentsClient({
      environment: "TEST", // 👈 使用 PRODUCTION 时改为 'PRODUCTION'
    });

    const allowedPaymentMethods = [
      {
        type: "CARD",
        parameters: {
          allowedAuthMethods: ["PAN_ONLY", "CRYPTOGRAM_3DS"],
          allowedCardNetworks: ["MASTERCARD", "VISA"],
        },
        tokenizationSpecification: {
          type: "PAYMENT_GATEWAY",
          parameters: {
            gateway: "example", // 👈 替换为你的支付网关，如 'stripe'
            gatewayMerchantId: "exampleGatewayMerchantId",
          },
        },
      },
    ];

    const isReadyToPayRequest = {
      apiVersion: 2,
      apiVersionMinor: 0,
      allowedPaymentMethods,
    };

    
    paymentsClient
      .isReadyToPay(isReadyToPayRequest)
      .then((response) => {
        if (response.result && containerRef.current) {
          const button = paymentsClient.createButton({
            onClick: () => onClick(paymentsClient),
          });
          containerRef.current.appendChild(button);
        }
      })
      .catch(console.error);
  }, []);

  const onClick = (paymentsClient) => {
    const paymentDataRequest = {
      apiVersion: 2,
      apiVersionMinor: 0,
      allowedPaymentMethods: [
        {
          type: "CARD",
          parameters: {
            allowedAuthMethods: ["PAN_ONLY", "CRYPTOGRAM_3DS"],
            allowedCardNetworks: ["VISA", "MASTERCARD", "AMEX", "DISCOVER", "JCB"],
          },
          tokenizationSpecification: {
            type: "PAYMENT_GATEWAY",
            parameters: {
              gateway: "example", // 👈 替换为你的支付网关
              gatewayMerchantId: "exampleGatewayMerchantId",
            },
          },
        },
      ],
      // merchantInfo: {
      //   merchantId: '12345678901234567890', // 👈 替换为你的商户号
      //   merchantName: '测试商户',
      // },
      merchantInfo: {
        merchantName: "123123",
      },
      transactionInfo: {
        totalPriceStatus: "FINAL",
        totalPrice: "10.00",
        currencyCode: "USD",
        countryCode: "US",
      },
    };

    paymentsClient
      .loadPaymentData(paymentDataRequest)
      .then((paymentData) => {
        const token = paymentData.paymentMethodData.tokenizationData.token;
        console.log("支付成功，令牌：", token);

        // ✅ 发送 token 到后端完成支付
      })
      .catch((err) => {
        console.error("支付失败或取消：", err);
      });
  };

  return <div ref={containerRef}></div>;
};

export default GooglePayButton;
