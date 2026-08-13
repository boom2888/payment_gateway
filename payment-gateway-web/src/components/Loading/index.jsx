import React from "react";
import { Spin } from "antd";
import "./loading.less";
const Loading = () => {
  return (
    <div className="app-container">
      <Spin />
    </div>
  );
};

export default Loading;
