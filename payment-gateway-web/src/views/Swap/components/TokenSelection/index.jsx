import React, { useState, useEffect } from "react";
import { SearchOutlined } from "@ant-design/icons";
import { Modal, Input } from "antd";
import useDebounce from "@/utils/useDebounce.js";

import arrowtwo from "@/assets/swap/arrowtwo.svg";
import arrowwhite from "@/assets/swap/arrowwhite.svg";
import closure from "@/assets/swap/closure.svg";
import search from "@/assets/swap/search.svg";

import "./tokenSelection.less";

const TokenSelection = ({ isBuy, tokenList, acToken, setAcToken,  selectedToken }) => {
  // 去除已经选择过的token
  const tokenListWithoutSelected = () => {
    console.log(tokenList,'tokenList',selectedToken)
    // if(Object.keys(selectedToken).length === 0){
    //   return tokenList;
    // }
    // const arr = tokenList.filter((item) => item.token !== selectedToken.token);
    return tokenList;
  };

  const isActoken = Object.keys(acToken).length === 0;

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [list, setList] = useState(tokenListWithoutSelected());
  // 搜索内容
  const [searchValue, setSearchValue] = useState("");
  const searchChange = ({ target: { value } }) => {
    setSearchValue(value);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const acTokenClick = (item) => {
    setAcToken(item);
    handleCancel();
  };

  const debouncedSearchValue = useDebounce(searchValue, 500);
  useEffect(() => {
    const regex = new RegExp(searchValue, "i");
    const arr = tokenList.filter(
      (item) =>
        regex.test(item.token) ||
        regex.test(item.symbol) ||
        regex.test(item.name)
    );
    setList(arr);
  }, [debouncedSearchValue]);
  useEffect(() => {
    const newList = tokenListWithoutSelected();
    setList(newList);
  }, [tokenList, selectedToken]);
  return (
    <div className="token_selection">
      <div
        className={isActoken ? "show_token show_token_no_data" : "show_token"}
        onClick={() => setIsModalOpen(true)}
      >
        {!isActoken ? (
          <span className="selected_token">
            <img src={acToken.icon} alt="" />
            <span>{acToken.name}</span>
          </span>
        ) : (
          <span>Select token</span>
        )}
        <span className="arrow_icon">
          <img
            src={isActoken ? arrowwhite : arrowtwo}
            alt=""
            className="arrow"
          />
        </span>
      </div>
      <Modal
        open={isModalOpen}
        onCancel={handleCancel}
        centered={true}
        closable={false}
        footer={null}
        className="token_modal"
      >
        <div className="token_header">
          <span>{isBuy ? 'Select  Currency' : 'Select Crypto'}</span>
          <img
            src={closure}
            alt=""
            className="closure_icon"
            onClick={handleCancel}
          />
        </div>
        <div className=" search_bar_con">
          <div className="search_bar">
            <img src={search} alt="" className="search_icon" />
            <input
              type="text"
              className="search_input"
              placeholder="Search"
              value={searchValue}
              onChange={searchChange}
            />
          </div>
        </div>
        <div className="token_list">
          {list.map((item, index) => {
            return (
              <div
                className="list_item"
                key={index}
                onClick={() => acTokenClick(item)}
              >
                <div className="item_icon">
                  <img src={item.icon} alt="" />
                </div>
                <div className="item_info">
                  <div className="item_name">{item.name}</div>
                  <div className="item_symbol">{item.symbol}</div>
                </div>
              </div>
            );
          })}
        </div>
      </Modal>
    </div>
  );
};
export default TokenSelection;
