import { defineConfig } from "vite";

// 开发环境配置
export default defineConfig({
    server: {
    port: 5173, // 本地端口（可选）
    proxy: {
      // 1. 匹配 /api 开头的请求
      // '/api': {
      //   target: 'https://pay.deepcard.ai', // 你的后端地址
      //   changeOrigin: true, // 修改 origin 头为目标地址
      //  // rewrite: (path) => path.replace(/^\/api/, '') // 去掉 /api 前缀
      // },
      '/flags': {
        target: 'https://lc-v2.pg.blockdance.io', // 你的后端地址
        changeOrigin: true, // 修改 origin 头为目标地址
       // rewrite: (path) => path.replace(/^\/api/, '') // 去掉 /api 前缀
      },
    },
  },
});
