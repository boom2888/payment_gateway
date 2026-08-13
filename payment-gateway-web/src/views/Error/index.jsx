import React, { useState, useEffect, useRef } from "react";
import { useParams, useLocation } from "react-router-dom";
import { postMessage } from "@/utils/postMessage";
import { useTranslation } from "react-i18next";
import Notification from "@/assets/img/Notification.svg";

import "./index.less";
const ErrorCommit = () => {
  const { t } = useTranslation();

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const text = queryParams.get("message"); // 获取 ?name=value

  const ErrorRef = useRef(null);
  const onSubmit = () => {
    console.info("Error postMessage 00");
    postMessage('order_cancelled');
  };
  useEffect(() => {
    setTimeout(() => {
      onSubmit();
    }, 1000);
  }, [ErrorRef]);
  return (
    <div ref={ErrorRef} className="Error">
      <img src={Notification} alt="" />
      <div>{t("paymentFailure")}</div>
      <button onClick={onSubmit}>{text}</button>
    </div>
  );
};
export default ErrorCommit;
