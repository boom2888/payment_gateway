import React, {useState} from "react";
import {Button} from "antd";
import {copyText} from "@/utils/copy";
import {useTranslation} from "react-i18next";
import useUser from "@/store/user.js";

import LogoCommit from "@/components/LogoCommit";
import TermsAndConditions from "@/components/TermsAndConditions";
import Authenticating from "../Authenticating";

import logoPng from "@/assets/img/moresia.png";
import Group1Svg from "@/assets/img/Group_1.svg";
import checkedSvg from "@/assets/img/checked.svg";
import copySvg from "@/assets/img/copy.svg";

import {formatMiddleString} from "@/utils/formatting.js";
import "./index.less";

const HideHome = (props) => {
  const { onConfig, orderNo } = props;
  const { t } = useTranslation();
    const {loading,orderInfo,tiemNum} = useUser();

  const [disabled, setDisabled] = useState(true);
  const [showPopup, setShowPopup] = useState(false);

  const onCreateOrder = () => {
    onConfig();
  };



  if (loading) {
    return <Authenticating></Authenticating>;
  }

  return (
    <div className="HideHome">
  
      <LogoCommit logo={logoPng} title={t("ConfirmOrder")} />
      <div className="HideHomeContent">
        <div className="HideHomeCart HideHome_address">
          <div>You Whant to buy {orderInfo.productName} </div>
          <div className="marginTop">Order No: {orderInfo.orderNo} </div>
          <img src={Group1Svg} alt="" />
          {/*<div className="HideHomeCopy">
            <div>{formatMiddleString(orderInfo.walletAddress)}</div>
            <div
              className="HideHomeCopy_an"
              onClick={() => {
                copyText(orderInfo.walletAddress);
              }}
            >
              <img src={copySvg} alt="" />
              {t("Copy")}
            </div>
          </div>*/}
        </div>

        <div>
          <div className="justify_between">
            <div>{t("Summary")}</div>
            <div className="justify_right">
              {t("QuoteUpdatesIn")} <span>{tiemNum}s</span>
            </div>
          </div>
          <div className="HideHomeContentFooter HideHomeCart">
            <div className="HideHomeContentFooter_header">
              {/*{t("YouGet")}*/}
              Need Pay
              <span className="HideHomeContentFooter_kuai">
                {orderInfo.fiatAmount}
              </span>
              <span className="curr">{orderInfo.currency}</span>

              {/*{t("for")}
              <span className="curr">{orderInfo.currency}</span>
              <span className="HideHomeContentFooter_kuai">
                {orderInfo.fiatAmount}
              </span>*/}
            </div>
            {/*<div>
              <div>
                <span>{orderInfo.cryptoAmount}</span>
                <span className="spans">{orderInfo.crypto}</span>
                <span className="spans"> @ </span>
                <span>{orderInfo.fiatAmount}</span>
                <span>{orderInfo.currency}</span>
              </div>
              <div>
                <span>{orderInfo.fiatAmount}</span>
                <span>{orderInfo.currency}</span>
              </div>
            </div>*/}
          </div>
        </div>
      </div>
      <div className="HideHomeFooter">
      <div className="HideHomeFooter_checked">
          <button
            className={`botton_checked ${!disabled && "checked"}`}
            onClick={() => setDisabled(!disabled)}
          >
            {!disabled && <img src={checkedSvg} alt="" />}
          </button>
          <div className="HideHomeFooter_tips">
            {t("tips1")}
            <span onClick={() => setShowPopup(true)}> {t("tips2")} </span>
            {t("tips3")}
          </div>
        </div>
        <Button
          block
          disabled={disabled}
          onClick={() => onCreateOrder()}
          className={`HideHomeBotton ${disabled ? "delivering_disabled" : ""}`}
        >
          {t("ProceedToPayment")}
        </Button>
      </div>

      {showPopup && (
        <TermsAndConditions
          popus={showPopup}
          handleCancel={() => setShowPopup(false)}
        />
      )}
    </div>
  );
};

export default HideHome;
