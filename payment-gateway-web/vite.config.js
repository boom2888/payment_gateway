import { defineConfig } from "vite";

import viteBase from "./vite.base.config.js";
import viteDev from "./vite.dev.config.js";
import viteProd from "./vite.prod.config.js";

const viteConfig = {
  serve: () => {
    // console.log("开发环境");
    return { ...viteDev, ...viteBase };
  },
  build: () => {
    // console.log("生产环境");
    return { ...viteProd, ...viteBase };
  },
};

export default defineConfig(({ command }) => {
  return viteConfig[command]();
});
