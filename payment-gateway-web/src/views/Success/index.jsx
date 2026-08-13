import React, { useState, useEffect, useRef } from "react";
import { useParams, useLocation } from "react-router-dom";
import { postMessage } from "@/utils/postMessage";
import { useTranslation } from "react-i18next";
import Notification from "@/assets/img/Notification.png";

import "./index.less";
const Success = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const SuccessRef = useRef(null);
  const onSubmit = () => {
    console.info("Error postMessage 00");
    postMessage('order_completed');
  };
  useEffect(() => {
    setTimeout(() => {
      onSubmit();
    }, 1000);
  }, [SuccessRef]);

  return (
      <div ref={SuccessRef} className="Success">
        <img src={Notification} alt="" />
        <div>{t("paymentSuccess")}</div>
      </div>
  );
};
export default Success;
