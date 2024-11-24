/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  darkMode: "class",
  theme: {
    extend: {
      animation: {
        shake: "shake 0.5s cubic-bezier(0.5, 0.1, 0.5, 1) infinite",
      },
      keyframes: {
        shake: {
          "0%, 100%": { transform: "rotate(0deg)" },
          "25%": { transform: "rotate(-2deg)" },
          "75%": { transform: "rotate(2deg)" },
        },
      },
    },
    fontFamily: {
      Pretendard: ["Pretendard"],
      Esamanru: ["Esamanru"],
      Samsung: ["Samsung"],
    },
    screens: {
      sm: "480px",
      md: "768px",
      lg: "976px",
      xl: "1440px",
    },
    colors: {
      primary: {
        // 200: "#447FC7",
        200: "#5C89F6",
        // 500: "#245795",
        300: "#5C89F6",
        500: "#1428A0",
      },
      white: "#EDEDED",
      whiteBox: "#FFFFFF",
      newBlock: "#F7F7FD",
      black: "#141414",
      gray: {
        200: "#D9D9D9",
        400: "#B4B4B4",
        500: "#9A9A9A",
        800: "#5E5E5E",
      },
      // block: "#1428A0",
      // block: "#FFFFFF",
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
