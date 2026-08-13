import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import closeSvg from "@/assets/svg/close.svg";
import EnCommit from './en.jsx'

import "./index.less";
export default (props) => {
  const { handleCancel } = props;
  const { t } = useTranslation();
  return (
    <div className="TermsAndConditions">
      <div className="TermsAndConditions_bg"></div>
      <div className="TermsAndConditions_main">
        <div className="TermsAndConditions_title">
          {t("TermsAndConditions")}
          <img onClick={handleCancel} src={closeSvg} alt="" />
        </div>
        <div className="TermsAndConditions_mains">
          <div className="TermsAndConditions_mainsss">
            <EnCommit />
            </div>
        </div>
      </div>
    </div>
  );
};
