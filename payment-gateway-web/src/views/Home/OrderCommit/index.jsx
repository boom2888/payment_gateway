import React, { useState, useRef, useEffect, useCallback } from "react";

import Flag from "react-world-flags";
import country from "./country.js";
import { useNavigate } from "react-router-dom";
import { useStripe } from "@stripe/react-stripe-js";
import { postcodeValidator } from "postcode-validator";
import { useTranslation } from "react-i18next";
import dayjs from "dayjs";

import ErrPopup from "@/components/ErrPopup";
import useUser from "@/store/user.js";
import { payApi } from "@/requests";

import CreateOrderLoadoding from "../CreateOrderLoadoding";
import LogoCommit from "@/components/LogoCommit";
import { Button, Form, Input, Select, DatePicker } from "antd";

import PaymentSvg from "@/assets/img/Payment.png";
import cartPaySvg from "@/assets/img/cartPay.svg";
import googlePaySvg from "@/assets/img/GooglePay.svg";
import applePaySvg from "@/assets/img/applePay.svg";
import errSvg from "@/assets/img/err.svg";
import english from "@/assets/nationsvg/english.svg";
import spain from "@/assets/nationsvg/spain.svg";

import "./index.less";
import ThreeDSModal from "@/views/Home/ThreeDSModal/index.jsx";
import { getBrowserInfo } from "../../../utils/browserInfo.js";

const InputCom = (props) => {
  let {
    value,
    onChange,
    rules = false,
    placeholder,
    label,
    type,
    dateFormat,
  } = props;
  const [focus, setFocus] = useState(false);
  const { status, errors } = Form.Item.useStatus();


  const handleDateChange = (date, dateString) => {
    console.log("🆕 日期对象:", date);         // Dayjs 对象
    console.log("🆕 字符串格式:", dateString); // 格式化后的字符串，例如 "2025-05"
    onChange(dateString);
  };

  const handleChange = (e) => {
    if (type) {
      onChange(value);
    } else {
      onChange(e.target.value);
    } // 调用 onChange 更新表单值
  };
  const onFocus = (v) => {
    setFocus(true);
  };
  const onBlur = () => {
    setFocus(false);
  };
  return (
    <div
      className={`InputCom ${focus && "InputComs"} ${errors.length && "InputComsErr"
        }`}
    >
      <div>
        <div className="inputLabel">
          {rules && <span className="inputRules">*</span>}
          {label}
        </div>
        {type && type == "data" ? (
          <DatePicker
            defaultValue={value ? dayjs(value, dateFormat) : undefined}
            picker="month"
            format={dateFormat}
            onFocus={onFocus}
            onBlur={onBlur}
            onChange={handleDateChange}
            placeholder={placeholder}
          />
        ) : (
          <Input
            onChange={handleChange}
            value={value}
            onFocus={onFocus}
            onBlur={onBlur}
            placeholder={placeholder}
          />
        )}
      </div>
    </div>
  );
};

const SelectCom = (props) => {
  let { value, onChange, rules = false, placeholder, label, err } = props;
  const [focus, setFocus] = useState(false);
  const { status, errors } = Form.Item.useStatus();
  const { i18n } = useTranslation();
  const selectContainerRef = useRef(null);

  const selectOption = country.countryList;

  const optionRender = (option) => {
    return (
      <div className="select_option">
        <Flag className="Flag" code={option.value} />
        <span>{option.label[i18n.language]}</span>
      </div>
    );
  };
  const labelRender = (option) => {
    const selectedItem = selectOption.find(
      (item) => item.value === option.value
    );
    return (
      <div className="select_option">
        <Flag className="Flag" code={selectedItem ? selectedItem.value : ""} />
        <span>{selectedItem ? selectedItem.label[i18n.language] : ""}</span>
      </div>
    );
  };
  const handleChange = (e) => {
    onChange(e); // 调用 onChange 更新表单值
  };
  const onFocus = (v) => {
    setFocus(true);
  };
  const onBlur = () => {
    setFocus(false);
  };
  const filterOption = (input, option) => {
    //if()
    return (option?.label[i18n.language] ?? "")
      .toLowerCase()
      .includes(input.toLowerCase());
  };
  return (
    <div
      ref={selectContainerRef}
      className={`InputCom SelectCom ${focus && "InputComs"} ${errors.length && "InputComsErr"
        }`}
    >
      <div>
        <div className="inputLabel">
          {rules && <span className="inputRules">*</span>}
          {label}
        </div>
        <Select
          showSearch
          filterOption={filterOption}
          getPopupContainer={() => selectContainerRef.current}
          optionLabelProp="value"
          onChange={handleChange}
          options={selectOption}
          optionRender={optionRender}
          placement="topLeft"
          labelRender={labelRender}
          value={value}
          placeholder={placeholder}
        />
      </div>
    </div>
  );
};

const ErrCommit = ({ text }) => {
  return (
    <div className="ErrCommit">
      <img src={errSvg} alt="" />
      <span>{text}</span>
    </div>
  );
};

const useIsAppleSafari = () => {
  const [isSafari, setIsSafari] = useState(false);

  useEffect(() => {
    const isAppleDevice = /Mac|iPhone|iPad|iPod/i.test(navigator.platform);
    const ua = navigator.userAgent;
    const vendor = navigator.vendor;
    const isSafariBrowser =
      /^Apple/i.test(vendor) &&
      /Safari/i.test(ua) &&
      !/CriOS|FxiOS|EdgiOS/i.test(ua);

    setIsSafari(isAppleDevice && isSafariBrowser);
  }, []);

  return isSafari;
};

const OrderCommit = (props) => {
  const { orderNo } = props;
  const navigate = useNavigate();

  const stripe = useStripe();
  const paymentsClientRef = useRef(null);

  const isAppleSafari = useIsAppleSafari();

  const order = useUser((state) => state.orderInfo);
  const { t } = useTranslation();
  const [form] = Form.useForm(); // 获取表单实例

  const [errPopup, setErrPopup] = useState(false);
  const [tipsText, setTipsText] = useState("");
  const [disabled, setDisabled] = useState(false);
  const [loading, setLoading] = useState(false);

  const [showAuth3dsModal, setShowAuth3dsModal] = useState(false);
  const [acsUrl, setAcsUrl] = useState('');
  const [cReq, setCReq] = useState('');

  // const [orderInfo, setOrderInfo] = useState({
  //   paymentType: "card", //1card 2、google_pay, apple_pay
  //   cardNumber: "5545060700002228",
  //   expiryDate: "2027/08",
  //   cvv: "123",
  //   firstName: "CL-",
  //   lastName: "BRW2",
  //   addressLine1: "Flat 5, 10 Downing Street",
  //   city: "London",
  //   state: "",
  //   zip: "SW1A 2AA",
  //   country: "GB",
  // });

  const [orderInfo, setOrderInfo] = useState({
    paymentType: "card", //1card 2、google_pay, apple_pay
    cardNumber: "",
    email: "",
    expiryDate: "",
    cvv: "",
    firstName: "",
    lastName: "",
    addressLine1: "",
    city: "",
    state: "",
    zip: "",
    country: "GB",
  });

  // const [orderInfo, setOrderInfo] = useState({
  //   paymentType: "card", //1card 2、google_pay, apple_pay
  //   cardNumber: "4596610155666488",
  //   expiryDate: "2027/10",
  //   cvv: "264",
  //   firstName: "YIRAN",
  //   lastName: "GU",
  //   addressLine1: "1 Konstanz Close, London, SW14 7EU, United Kingdom",
  //   city: "London",
  //   state: "",
  //   zip: "SW1A 2AA",
  //   country: "GB",
  // });


  const onDateChange = (expiryDateNew) => {
    console.log(expiryDateNew, "expiryDateNew");
    setOrderInfo(...orderInfo, ...expiryDateNew);
  };

  const [countryCode, setCountryCode] = useState(orderInfo.country);
  const onCountryChange = (value) => {
    setCountryCode(value)
  };

  //"paymentType": "card",  google_pay apple_pay
  const [paymentType, setPaymentType] = useState("card");
  const [paysType, setPaysType] = useState(false);

  const onValuesChange = (changedValues, allValues) => {
    console.log(changedValues, "changedValues");
    setOrderInfo(prev => ({
      ...prev,
      ...changedValues
    }));
  };

  useEffect(() => {
    if (
      orderInfo.paymentType == "card" ||
      orderInfo.paymentType == "google_pay"
    ) {
      setPaymentType(true);
    } else {
      setPaymentType(false);
    }
    if (
      orderInfo.paymentType == "card" ||
      orderInfo.paymentType == "apple_pay"
    ) {
      setPaysType(true);
    } else {
      setPaysType(false);
    }
  }, [orderInfo]);

  const onCreateOrder = () => {
    console.log(order, "baseCardPaymentMethod");
    form
      .validateFields()
      .then(async (value) => {
        console.log(value, "value");

        let expiryMonth = "";
        let expiryYear = "";
        if (value.expiryDate) {
          const date = dayjs(value.expiryDate + "-01");
          expiryMonth = date.month() + 1; // 2025
          expiryYear = date.year(); // 6 （注意：month 从 0 开始计数）
        }
        if (orderInfo.paymentType === "card") {
          doCreateOrder({
            merchantId: "1",
            sign: "test",
            cardInfo: {
              cardNumber: value.cardNumber,
              email: value.email,
              firstName: value.firstName,
              lastName: value.lastName,
              expiryMonth: expiryMonth + "",
              expiryYear: expiryYear + "",
              cvv: value.cvv,
            },
            addressInfo: {
              country: value.country,
              city: value.city,
              state: value.state,
              zip: value.zip,
              addressLine1: value.addressLine1,
            },
          });
        } else if (orderInfo.paymentType === "google_pay") {
          GooglePay(value);
        } else {
          applePay({
            cardInfo: {
              cardNumber: value.cardNumber,
              expiryMonth: expiryMonth,
              expiryYear: expiryYear,
              cvv: value.cvv,
            },
            addressInfo: {
              addressLine1: value.addressLine1,
            },
          });
        }
      })
      .catch((err) => {
        console.log(err, "err");
      });
  };

  useEffect(() => {
    if (window.google && !paymentsClientRef.current) {
      paymentsClientRef.current = new window.google.payments.api.PaymentsClient(
        {
          environment: "TEST", // or 'PRODUCTION'
        }
      );
    }
  }, []);
  //谷歌调用支付
  const GooglePay = async (value) => {
    const paymentsClient = paymentsClientRef.current;
    if (!paymentsClient) return;
    //定义创建配置时引用的谷歌Pay API的版本
    const baseRequest = {
      apiVersion: 2,
      apiVersionMinor: 0,
    };
    //您的站点和网关支持的卡网络
    const allowedCardNetworks = [
      "VISA",
      "MASTERCARD",
      "AMEX",
      "DISCOVER",
      "JCB",
    ];

    //您的站点和网关支持的卡身份验证方法
    const allowedCardAuthMethods = ["PAN_ONLY", "CRYPTOGRAM_3DS"];

    //为您的付款服务机构申请付款令牌
    const tokenizationSpecification = {
      type: "PAYMENT_GATEWAY",
      parameters: {
        gateway: "nuveidigital", //Payment gateway
        gatewayMerchantId: "googletest", //'googletest' for test or 'nuveidigital' for production
      },
    };
    //指定支持的支付卡网络,将您支持的身份验证方法和支持的支付卡网络结合使用
    const baseCardPaymentMethod = {
      type: "CARD",
      parameters: {
        allowedAuthMethods: allowedCardAuthMethods,
        allowedCardNetworks: allowedCardNetworks,
      },
    };
    console.log(order, "baseCardPaymentMethod");
    //支付数据
    const transactionInfo = {
      // countryCode: value.country,
      // currencyCode: order.currency,
      // totalPriceStatus: "FINAL",
      // totalPrice: order.fiatAmount,
      countryCode: "US",
      currencyCode: order.currency,
      totalPriceStatus: "FINAL",
      totalPrice: order.fiatAmount,
    };
    //描述你的网站对CARD支付方式的支持，包括可选字段
    const cardPaymentMethod = Object.assign({}, baseCardPaymentMethod, {
      tokenizationSpecification: tokenizationSpecification,
    });

    function getGoogleIsReadyToPayRequest() {
      return Object.assign({}, baseRequest, {
        allowedPaymentMethods: [baseCardPaymentMethod],
      });
    }
    function getGooglePaymentDataRequest() {
      const paymentDataRequest = Object.assign({}, baseRequest);
      paymentDataRequest.allowedPaymentMethods = [cardPaymentMethod];
      paymentDataRequest.transactionInfo = transactionInfo;
      paymentDataRequest.merchantInfo = {
        merchantName: import.meta.env.VITE_GOOGLE_PAY_MERCHANT_NAME, //test
        merchantId: import.meta.env.VITE_GOOGLE_PAY_MERCHANT_ID, //test
        // merchantId: 'BCR2DN4TX6WNHXJR', //正式环境参数
        // merchantName: 'Moresia LIMITED',//正式环境参数
      };
      return paymentDataRequest;
    }

    const paymentDataRequest = getGooglePaymentDataRequest();
    paymentDataRequest.transactionInfo = transactionInfo;

    try {
      console.log(paymentsClient, "paymentsClient");
      const paymentData = await paymentsClient.loadPaymentData(
        paymentDataRequest
      );
      console.log(
        "用户支付成功，token:",
        paymentData.paymentMethodData.tokenizationData.token
      );
      doCreateOrder({
        addressInfo: {
          country: value.country,
          city: value.city,
          zip: value.zip,
          addressLine1: value.addressLine1,
        },
        googlePayToken: paymentData.paymentMethodData,
      });
      // ✅ 把 token 发到后端
    } catch (err) {
      console.error("用户取消或支付失败:", err);
    }
  };
  //手机支付
  const applePay = async (value) => {
    if (!stripe) return;
    const paymentRequest = stripe.paymentRequest({
      country: "US",
      currency: order.currency,
      total: {
        label: "示例订单",
        amount: order.fiatAmount,
      },
      requestPayerName: true,
      requestPayerEmail: true,
    });

    const result = await paymentRequest.canMakePayment();
    if (!result?.applePay) {
      console.log("当前设备不支持 Apple Pay");
      return;
    }
    const prSession = await paymentRequest.show();
    prSession.on("paymentmethod", async (ev) => {
      console.log("Apple Pay 支付方式：", ev.paymentMethod);

      // ✅ TODO：将 ev.paymentMethod.id 传到后端用于创建 PaymentIntent
      // await fetch('/api/confirm-payment', { method: 'POST', body: ... })

      ev.complete("success");
      //  alert("支付成功！感谢您的提交");
      doCreateOrder({
        ...value,
        applePayToken: ev.paymentMethod.id,
      });
    });

    prSession.on("cancel", () => {
      console.log("用户取消 Apple Pay 支付");
    });
  };

  const doCreateOrder = (value) => {
    setDisabled(true);
    setLoading(true);
    const browserInfo = getBrowserInfo();
    payApi({
      browserInfo,
      paymentType: orderInfo.paymentType,
      orderNo: orderNo,
      ...value,
    })
      .then((res) => {
        console.log(res, 'res==')
        let { code, data, msg } = res;
        const isSucc = code === 200;
        setTimeout(async () => {
          console.log(data)
          if (isSucc) {
            const is3ds = data['need3ds'];
            if (is3ds) {
              const auth3ds = data['auth3ds'];
              //打开DIALOG
              // setAcsUrl(auth3ds.acsUrl);
              // setCReq(auth3ds.cReq);
              // setShowAuth3dsModal(true);

              navigate(`/auth?acsUrl=${auth3ds.acsUrl}&cReq=${auth3ds.cReq}`);
            } else {
              navigate("/success");
            }
          } else {
            // navigate(`/Error?message=${msg}`);
            setTipsText(msg);
            setErrPopup(true);
          }
        }, 1000);
      })
      .catch((err) => {
        let { data, msg, code } = err;
        setTipsText(msg);
        setErrPopup(true);
        // if(code < 0){ //可以继续支付的情况
        //   // debugger
        //   setTipsText(msg);
        //   setErrPopup(true);
        // }else{
        //   setTimeout(() => {
        //     // debugger
        //     // navigate(`/Error?message=${msg}`);
        //     setTipsText(msg);
        //     setErrPopup(true);
        //     // console.log("22")
        //   }, 1000);
        // }
      })
      .finally(() => {
        setTimeout(() => {
          // debugger
          setDisabled(false);
          setLoading(false);
        }, 1000);
      });
  };

  function getInfo() {
    setTimeout(() => {
      //  setLoading(false);
    }, 2000);
  }

  getInfo();

  const onFocus = (v) => {
    console.log("onFocus", v);
  };
  const onBlur = () => {
    console.log("onBlur");
  };
  if (loading) {
    return <CreateOrderLoadoding></CreateOrderLoadoding>;
  }
  return (
    <div className="OrderCommit">
      <ErrPopup
        tipsText={tipsText}
        show={errPopup}
        handleCancel={() => {
          setErrPopup(false);
        }}
      />
      <div>
        <LogoCommit logo={PaymentSvg} title={t("Payment")} />
        <div
          className={`OrderCommitPay ${isAppleSafari && "OrderCommitPayApple"}`}
        >
          <div
            onClick={() => {
              setOrderInfo({ ...orderInfo, paymentType: "card" });
            }}
            className={`Order_Card ${orderInfo.paymentType == "card" && "Order_Card_Select"
              }`}
          >
            <div>
              <img src={cartPaySvg} alt="" />
            </div>
            <div className="lable">{t("card")}</div>
          </div>
          <div
            onClick={() => {
              setOrderInfo({ ...orderInfo, paymentType: "google_pay" });
            }}
            className={`Order_Card ${orderInfo.paymentType == "google_pay" && "Order_Card_Select"
              }`}
          >
            <div>
              <img src={googlePaySvg} alt="" />
            </div>
            <div className="lable">{t("GooglePay")}</div>
          </div>
          {isAppleSafari && (
            <div
              onClick={() => {
                setOrderInfo({ ...orderInfo, paymentType: "apple_pay" });
              }}
              className={`Order_Card ${orderInfo.paymentType == "apple_pay" && "Order_Card_Select"
                }`}
            >
              <div>
                <img src={applePaySvg} alt="" />
              </div>
              <div className="lable">{t("ApplePay")}</div>
            </div>
          )}
        </div>
        <div className="OrderCommit_center">
          <Form
            form={form}
            initialValues={orderInfo}
            onValuesChange={onValuesChange}
            layout="vertical"
          >
            {paysType && (
              <Form.Item
                name="cardNumber"
                label={t("CardNumber")}
                className="order_form_item"
                rules={[
                  {
                    required: true,
                    message: (
                      <ErrCommit
                        text={t("PleaseEnterThe", { name: t("CardNumber") })}
                      />
                    ),
                  },
                ]}
              >
                <InputCom
                  label={t("CardNumber")}
                  rules={true}
                  placeholder="8888 8888 8888 8888"
                />
              </Form.Item>
            )}

            {paysType && (
                <Form.Item
                    name="email"
                    label={t("Card Bind Email")}
                    className="order_form_item"
                    rules={[
                      {
                        required: true,
                        message: (
                            <ErrCommit
                                text={t("PleaseEnterThe", { name: t("email") })}
                            />
                        ),
                      },
                    ]}
                >
                  <InputCom
                      label={t("Card Bind Email")}
                      rules={true}
                      placeholder="xxxx@yy.com"
                  />
                </Form.Item>
            )}

            {paysType && (
              <div className="grid">
                <Form.Item
                  name="expiryDate"
                  label={t("ExpiryDate")}
                  rules={[
                    {
                      required: true,
                      message: (
                        <ErrCommit
                          text={t("PleaseEnterThe", { name: t("ExpiryDate") })}
                        />
                      ),
                    },
                  ]}
                >
                  <InputCom
                    label={t("ExpiryDate")}
                    dateFormat="YYYY/MM"
                    type="data"
                    rules={true}
                    placeholder="MM/YY"
                    onChange={onDateChange}
                  />
                </Form.Item>

                <Form.Item
                  name="cvv"
                  label="CVC"
                  rules={[
                    {
                      required: true,
                      message: (
                        <ErrCommit
                          text={t("PleaseEnterThe", { name: t("CVC") })}
                        />
                      ),
                    },
                  ]}
                >
                  <InputCom label={t("CVC")} rules={true} placeholder="CVC" />
                </Form.Item>
              </div>
            )}
            {orderInfo.paymentType == "card" && (
              <div className="grid">
                <Form.Item
                  name="firstName"
                  label="FirstName"
                  rules={[
                    {
                      required: true,
                      message: (
                        <ErrCommit
                          text={t("PleaseEnterThe", {
                            name: t("FirstName"),
                          })}
                        />
                      ),
                    },
                  ]}
                >
                  <InputCom
                    label={t("FirstName")}
                    rules={true}
                    placeholder="FirstName"
                  />
                </Form.Item>

                <Form.Item
                  name="lastName"
                  label="LastName"
                  rules={[
                    {
                      required: true,
                      message: (
                        <ErrCommit
                          text={t("PleaseEnterThe", {
                            name: t("LastName"),
                          })}
                        />
                      ),
                    },
                  ]}
                >
                  <InputCom
                    label={t("LastName")}
                    rules={true}
                    placeholder="LastName"
                  />
                </Form.Item>
              </div>
            )}

            <Form.Item
              name="addressLine1"
              label="AddressLine1"
              rules={[
                {
                  required: true,
                  message: (
                    <ErrCommit
                      text={t("PleaseEnterThe", { name: t("AddressLine1") })}
                    />
                  ),
                },
              ]}
            >
              <InputCom
                label={t("AddressLine1")}
                rules={true}
                placeholder="AddressLine"
              />
            </Form.Item>
            {paymentType && (
              <div className="grid">
                <Form.Item
                  name="city"
                  label="City"
                  rules={[
                    {
                      validator: (_, value) => {
                        // if (!value) {
                        //   return Promise.resolve();
                        // }
                        // if(value){
                        //   return Promise.reject(<ErrCommit
                        //   text={t("Invalid", { name: t("City") })}
                        // />);
                        // }
                        return Promise.resolve();
                      },
                    },
                  ]}
                >
                  <InputCom label={t("City")} placeholder="City" />
                </Form.Item>
                <Form.Item
                  name="zip"
                  label="ZIPCode"
                  rules={[
                    ({ getFieldValue }) => ({
                      validator: (_, value) => {
                        // const country = getFieldValue("country");
                        //   );
                        // console.log(
                        //   postcodeValidator(value, country),
                        //   value,
                        //   country
                        // );
                        if (
                          !value || value === ''
                        ) {
                          return Promise.reject(
                            <ErrCommit
                              text={t("Invalid", { name: t("ZIPCode") })}
                            />
                          );
                        }
                        return Promise.resolve();
                      },
                    }),
                  ]}
                >
                  <InputCom label={t("ZIPCode")} placeholder="ZIP CODE" />
                </Form.Item>
              </div>
            )}
            {paymentType && (
              <Form.Item
                name="country"
                label="country"
                rules={[
                  {
                    required: true,
                    message: (
                      <ErrCommit
                        text={t("PleaseEnterThe", { name: t("Country") })}
                      />
                    ),
                  },
                ]}
              >
                <SelectCom
                  label={t("Country")}
                  rules={true}
                  placeholder="Country"
                  onChange={onCountryChange}
                />
              </Form.Item>
            )}

            {countryCode === 'US' && (
              <Form.Item
                name="state"
                label="State"
                rules={[
                  {
                    required: true,
                    message: (
                      <ErrCommit
                        text={t("PleaseEnterThe", {
                          name: t("State"),
                        })}
                      />
                    ),
                  },
                ]}
              >
                <InputCom
                  label={t("State")}
                  rules={true}
                  placeholder=""
                />
              </Form.Item>
            )}

          </Form>
        </div>
        <div className="OrderCommit_Footer">
          {/* disabled={disabled} */}
          <Button
            block
            disabled={disabled}
            onClick={() => onCreateOrder()}
            className={`HideHomeBotton ${disabled ? "delivering_disabled" : ""
              }`}
          >
            {t("Pay")}
          </Button>
        </div>
      </div>


      <ThreeDSModal
        visible={showAuth3dsModal}
        onClose={() => setShowAuth3dsModal(false)}
        acsUrl={acsUrl}
        cReq={cReq}
      />

    </div>
  );
};

export default OrderCommit;
