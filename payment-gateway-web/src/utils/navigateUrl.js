export const getNavigateURLString = (url, params) => {
  let str = "";
  for (const key in params) {
    str += `${key}=${params[key]}&`;
  }
  return `${url}?${str.slice(0, -1)}`;
};
