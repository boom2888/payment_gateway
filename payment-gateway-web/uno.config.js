import {
  defineConfig,
  presetUno,
  presetAttributify,
  presetWind,
  transformerVariantGroup,
} from "unocss";

import presetRemToPx from "@unocss/preset-rem-to-px";

function hexToRgb(hex) {
  hex = hex.replace("#", "");
  const r = parseInt(hex.substring(0, 2), 16);
  const g = parseInt(hex.substring(2, 4), 16);
  const b = parseInt(hex.substring(4, 6), 16);
  return `${r}, ${g}, ${b}`;
}

function isHexColor(color) {
  // 匹配 # 开头，后跟 3 或 6 位十六进制字符（不区分大小写）
  return /^#([0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})$/.test(color);
}
export default defineConfig({
  presets: [
    presetUno(),
    presetAttributify(),
    presetWind(),
    presetRemToPx({
      baseFontSize: 4,
    }),
  ],
  transformers: [transformerVariantGroup()],

  rules: [
    // 圆角
    [
      /^bdr-?(\d*)$/,
      ([, d]) => ({
        "border-radius": `${d}px`,
      }),
    ],
    ["rotate", ([angle]) => ({ transform: `rotate(${angle})` })],
    [
      "bg-to1",
      {
        background:
          "linear-gradient(180deg, var(--van-primary-color) 0%, var(--primary-color2) 100%)",
      },
    ],
  ],
  theme: {
    // 自定义颜色
    colors: {
      c0: "var(--color)",

      br: "var(--border)",
    },
    backgroundColor: {
      bg: "var(--bg)",
    },
  },
  shortcuts: {
    fc: "flex justify-center",
    fyc: "flex items-center",
    fxc: "flex flex-col items-center",
    fcc: "flex justify-center items-center",
    pe: "pointer-events-none",
    "t-hide": "truncate",
    tac: "text-center",
  },
});
