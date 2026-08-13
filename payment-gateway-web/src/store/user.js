import { create } from "zustand";
const useUser = create((set, get) => ({
  orderInfo: {
    fiatAmount: "",
    currency: "",
    walletAddress: "",
    cryptoAmount: "",
    status: null,
    cryptoCurrency: "",
  },
  orderNo: "", //订单变化
  loading: true, //初次获取数据
  tiemNum: 0,//定时刷新

  setOrderInfo: (order) => {
    set({ orderInfo: order });
  },
  setLoading: () => {
    set({ loading: false });
  },
  setTiemNum: (value) => {
    set({ tiemNum: value });
  },
}));

export default useUser;
