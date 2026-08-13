  
import React, { useState, useEffect, useCallback } from "react";
import { useTranslation } from "react-i18next";
import loadingSvg from "@/assets/svg/loading.svg";
import "./index.less";

  const Authenticating = () => {
    const { t } = useTranslation();
    return (
      <div className="Authenticating">
        <div className="Authenticating_main">
          <img src={loadingSvg} alt="" />

          <div className="AuthenticatingTitle">{t("Authenticating")}</div>
          <div className="AuthenticatingTips">{t("AuthenticatingTips")}</div>
        </div>
      </div>
    );
  };
  export default Authenticating