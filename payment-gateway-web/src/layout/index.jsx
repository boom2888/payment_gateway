import React, { useState, useEffect, useMemo } from "react";

import { Outlet } from "react-router-dom";

import "./layout.less";

const Frame = () => {
  return (
    <div className="layout  min-h-[100vh] bg-c0">
      <Outlet />
    </div>
  );
};
export default Frame;
