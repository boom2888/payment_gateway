import React from "react";
import "./index.less"
export default function ({logo,title}){
    return(
        <div className="logo">
             <img src={logo} alt="" />
             <div className="title">{title}</div>
        </div>
    )
}