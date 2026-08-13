import React, { useState, useEffect } from "react";
import { ArrowDownOutlined, DownOutlined } from "@ant-design/icons";
import { Tooltip, Modal } from "antd";

import trade from "@/assets/swap/trade.svg";
import arrow from "@/assets/swap/arrow.svg";
import tips from "@/assets/swap/tips.svg";
import setup from "@/assets/swap/setup.svg";
import closureAsh from "@/assets/swap/closureAsh.svg";
import {initCheckout} from "@/utils/checkout"

import "./Pay.less";
import "./cc.css";
import {useNavigate} from "react-router-dom";
import { authFetch } from "@/requests/api";

export let BASE_URL = import.meta.env.VITE_API_BASE_URL;

const Pay = (props) => {
  const navigate = useNavigate();

  // 选中的框，false表示出售，true表示购买
  const [isBuy, setIsBuy] = useState(true);
  const [address, setAddress] = useState("0xc54e0b053a35a7eed93127332250925fa113f06e");
  const addressChange = ({ target: { value } }) => {
    setAddress(value);
  };

  const [chain, setChain] = useState("ARB");


    const confirmExchange = async() => {
        console.log("确认交换");
        let currency = 'USD';
        let amount = '1';

        const res = await (await authFetch(BASE_URL + "/openOrder?currency=" + currency + "&amount=" + amount)).json();
        console.log("下订单:", res);
        if (res.code === 200 && res.data) {
            // navigate('/order?orderNo=' + res.data);

            initCheckout(res.data);
        }
    };


  return (
      <div className="swap">
8888888
        <div class='container'>
          <div class="item">
            <label for="session">Session Token</label>
            <input type="text" placeholder="Session Token" id="session" name="session"/>
          </div>
          <div class="item">
            <label for="metchantID">Merchant Id</label>
            <input value="" type="text" placeholder="Merchant Id" id="metchantID"/>
          </div>
          <div class="item">
            <label for="metchantSiteID">Merchant Site Id</label>
            <input value="" type="text" placeholder="Merchant Site Id" id="metchantSiteID"/>
          </div>
          <div class="item100">
            <button class='pay-button' onClick={confirmExchange}>Initialize Checkout</button>
          </div>

        </div>

        <div class="container_for_checkout"></div>

</div>
)
  ;
};
export default Pay;
