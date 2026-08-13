import React, { useState, useEffect } from "react";
import { Select } from "antd";
import { useTranslation } from "react-i18next";
import { GlobalOutlined, CheckOutlined } from "@ant-design/icons";
import { postMessage } from "@/utils/postMessage"

import english from "@/assets/nationsvg/english.svg";
import arrow from "@/assets/svg/arrow.svg";

import close from "@/assets/img/close.svg";

import "./header.less";
const Header = () => {
  const { t, i18n } = useTranslation();
  const selectOption = [
    { value: "en", label: t("English"), emoji: english },
    // { value: "es", label: t("Spanish"), emoji: spain },
    // { value: "it", label: t("Italian"), emoji: italy },
    // { value: "hk", label: t("Chinese_traditional"), emoji: china },
    // { value: "jp", label: t("Japanese"), emoji: japan },
  ];

  const handleChange = (value) => {
    i18n.changeLanguage(value);
  };
  const labelRender = ({ label, value }) => {
    if (label) {
      return value;
    }
  };

  const optionRender = (option) => {
    return (
      <div className="select_option">
        <img src={option.data.emoji} alt="" />
        <span>{option.label}</span>
      </div>
    );
  };
  // 回复父窗口

  // window.addEventListener("message", (event) => {
  //   if (event.data.type === "messageFromParent") {
  //     console.log("Received from parent:", event.data.data);

  //     postMessage();
  //   }
  // });
  const onClose = () => {
    console.info("Error postMessage2");
    postMessage('order_cancelled')
  };
  useEffect(() => {}, []);
  return (
    <div className="header">
      <Select
        value={i18n.language}
        defaultValue={selectOption[0].value}
        onChange={handleChange}
        options={selectOption}
        listHeight={500}
        labelRender={labelRender}
        optionRender={optionRender}
        prefix={<GlobalOutlined />}
        suffixIcon={<img src={arrow} alt="" />}
        className="lang_select"
        popupClassName="custom-select-dropdown"
      />
      <img onClick={onClose} src={close} alt="" />
    </div>
  );
};
export default Header;
