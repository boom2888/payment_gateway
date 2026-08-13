import Group from "@/assets/img/Group.png";
import React, { useState, useEffect, useCallback } from "react";
import { useTranslation } from "react-i18next";
import "./index.less";
const CreateOrderLoadoding = () => {
  const { t } = useTranslation();
  const [type, setType] = useState(false);

  useEffect(() => {
    const time = setInterval(() => {
      setType(!type);
    }, 2000);
    return () => {
      clearInterval(time);
      console.log("Interval 已清除");
    };
  }, []);
  return (
    <div className="CreateOrderLoadoding">
      <div className="CreateOrderLoadoding_main">
        <div>
          <img src={Group} alt="" />
        </div>

        <div className="CreatingYourOrderTitle">
          { type && t("ProcessingYourPayment")}
          { !type && t("FinalizingYourPurchase")}
        </div>
      </div>
    </div>
  );
};

export default CreateOrderLoadoding;
