import React, { useState, useEffect, useCallback } from "react";
import { Button } from "antd";
import { useTranslation } from "react-i18next";
import Notification from "@/assets/img/Notification.svg";
import "./index.less";
export default ({handleCancel,show = false,tipsText}) => {
  const { t } = useTranslation();
  return (
    <div className={`ErrPopup ${!show && 'ErrPopupNone'}`}>
      <div className="ErrPopup_bg"></div>
      <div className="ErrPopup_commit">
        <div>
          <div className="ErrPopup_title">{t("PaymentError")}</div>
          <img src={Notification} alt="" />
          <div className="ErrPopup_tips">{tipsText}</div>
          <Button className="ErrPopupBotton" onClick={handleCancel} block>{t("close")}</Button>
        </div>
      </div>
    </div>
  );
};
