/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  darkMode: "class",
  theme: {
    extend: {},
    fontFamily: {
      Pretendard: ["Pretendard"],
      Esamanru: ["Esamanru"],
    },
    screens: {
      sm: "480px",
      md: "768px",
      lg: "976px",
      xl: "1440px",
    },
    colors: {
      primary: {
        200: "#447FC7",
        500: "#245795",
      },
      white: "#EDEDED",
      whiteBox: "#FFFFFF",
      black: "#141414",
      gray: {
        200: "#D9D9D9",
        400: "#B4B4B4",
        500: "#9A9A9A",
        800: "#5E5E5E",
      },
      block: "#313F4F",
      warn: "#FF5353",
      warnBackground: "#F1CCCC",
      success: "#499B50",
      button: {
        stroke: "#3D444D",
        background: "#151B23",
      },
      transparent: "transparent",
    },
  },
  plugins: [],
};
