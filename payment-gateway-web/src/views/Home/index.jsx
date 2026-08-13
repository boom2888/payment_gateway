import React, { useState, useEffect, useRef } from "react";
import { useTranslation } from "react-i18next";
import { useParams, useLocation } from "react-router-dom";
import { getOrderInfo } from "@/requests";
import ErrPopup from "@/components/ErrPopup";
import useUser from "@/store/user.js";

import Header from "@/components/Header";

import HideHome from "./HideHome";
import OrderCommit from "./OrderCommit";
import StripePay from "../StripePay";

import "./home.less";
import {Auth} from "@/requests/auth.js";
const Home = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const orderNo = queryParams.get("orderNo"); // 获取 ?name=value
  const token = queryParams.get("token"); // 获取 ?name=value


  const { setTiemNum, setLoading, setOrderInfo } = useUser();

  const timerRef = useRef(null);
  const [errPopup, setErrPopup] = useState(false);
  const [tipsText, setTipsText] = useState("");
  const [source, setSource] = useState("");

  useEffect(() => {
    if(token){
      localStorage.setItem("PAY_TOKEN", token)
      console.info("save token=" + token);
    }
  }, [token]);



  const [stopValue, setStopValue] = useState(1);

  function onAdd() {
    if(source === 'STRIPE' || source === 'STRIPE_CONNECT'){
      setStopValue(3);
    }else{
      setStopValue(2);
    }

    // setStopValue(stopValue + 1);
  }

  const formatTimerRef = () => {
    clearInterval(timerRef.current);
  };
  //倒计时
  const CountdownEvent = () => {
    let num = 5;
    setTiemNum(num)

    timerRef.current = setInterval(() => {
      num = num - 1
      setTiemNum(num);
      if (num == 0) {
        fetchOrderInfo();
        formatTimerRef()
      } 
    }, 1000);
  };
  //清除定时器

  const fetchOrderInfo = async () => {
    if (!orderNo) return;
    try {
      const orderInfoRs = await getOrderInfo({ orderNo: orderNo });
      setOrderInfo(orderInfoRs.data);
      setSource(orderInfoRs.data.source)
      CountdownEvent();
    } catch (err) {
      let { data, message, code } = err;
      if (code) {
        setTipsText(t("PaymentErrorTitle", { code: code, name: message }));
        setErrPopup(true);
      }
    } finally {
        setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrderInfo();
    return () => {
      formatTimerRef()
    };
  }, []);

  return (
    <div className="home">
      <ErrPopup
        tipsText={tipsText}
        show={errPopup}
        handleCancel={() => {
          setErrPopup(false);
        }}
      />
      <Header />
      <div className="home_main">
        {stopValue == 1 && <HideHome orderNo={orderNo} onConfig={onAdd} />}
        {stopValue == 2 && <OrderCommit orderNo={orderNo} />}
        {stopValue == 3 && <StripePay orderNo={orderNo} />}
      </div>
    </div>
  );
};
export default Home;
