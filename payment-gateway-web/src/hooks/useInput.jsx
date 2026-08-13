import { useState } from "react";

export const useInput = (initialValue = "") => {
  const [value, setValue] = useState(initialValue);
  const handleChange = (value) => {
    setValue(value);
  };
  return [value, handleChange];
};
