const TOKEN_KEY = "TOKEY";
export const CARD = "CARD"
export const selectCrypto = "select_crypto"

export const cardInfo = "card_info"
export const vote = "vote"
export const subscribe = "subscribe"

export class Auth {
  static set token(value) {
    if (typeof localStorage !== "undefined" && localStorage) {
      localStorage[TOKEN_KEY] = value;
      localStorage.removeItem(vote);
    }
  }

  static get getToken() {
    if (typeof localStorage !== "undefined" && localStorage) {
      return localStorage[TOKEN_KEY];
    }
    return null;
  }

  static setLocal(name = TOKEN_KEY, value) {
    if (typeof localStorage !== "undefined" && localStorage) {
      localStorage[name] = value;
    }
  }

  static getLocal(name = TOKEN_KEY) {
    if (typeof localStorage !== "undefined" && localStorage) {
      return localStorage.getItem(name);
    }
    return null;
  }

  static remove(name = TOKEN_KEY) {
    if (typeof localStorage !== "undefined" && localStorage) {
      localStorage.removeItem(name);
    }
  }

  static get isLogin() {
    return !!this.getToken;
  }

  static clearAll() {
    if (typeof localStorage !== "undefined" && localStorage) {
      localStorage.clear();
    }
  }

  static clear() {
    if (typeof localStorage !== "undefined" && localStorage) {
      localStorage.removeItem(TOKEN_KEY);
    }
  }
}
