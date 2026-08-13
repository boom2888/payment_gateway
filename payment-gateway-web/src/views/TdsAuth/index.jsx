import React, { useState, useEffect, useRef } from "react";
import { useParams, useLocation } from "react-router-dom";
import { postMessage } from "@/utils/postMessage";
import { useTranslation } from "react-i18next";
import Notification from "@/assets/img/Notification.png";

import "./index.less";
const TdsAuth = () => {
  const { t } = useTranslation();

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const acsUrl = queryParams.get("acsUrl"); // 获取 ?name=value
  const cReq = queryParams.get("cReq"); // 获取 ?name=value

  const SuccessRef = useRef(null);
  useEffect(() => {
    setTimeout(() => {
      if (acsUrl) {
        // window.location.href = acsUrl;

        const form = document.createElement("form");
        form.method = "POST";
        form.action = acsUrl;

        // 创建 hidden input
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "creq";
        input.id = "creq";
        input.value = cReq;

        // 将 input 添加到表单
        form.appendChild(input);

        // 将表单添加到页面并提交
        document.body.appendChild(form);
        form.submit();
      } else {
        console.info("Error postMessage 33");
        postMessage("order_completed");
      }
    }, 1000);
  }, [SuccessRef, acsUrl, cReq]);
  return (
      <div ref={SuccessRef} className="Success">
        <img src={Notification} alt="" />
        <div>3ds${t("loadingInProgress")}</div>
      </div>
  );
};
export default TdsAuth;
