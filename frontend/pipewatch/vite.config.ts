import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { resolve } from "path";
import tsconfigPaths from "vite-tsconfig-paths";

// https://vite.dev/config/
export default defineConfig({
  base: "/",
  plugins: [react(), tsconfigPaths()],
  resolve: {
    alias: [
      {
        find: "@locales",
        replacement: resolve(__dirname, "locales"),
      },
      {
        find: "@src",
        replacement: resolve(__dirname, "src"),
      },
      {
        find: "@components",
        replacement: resolve(__dirname, "src/components"),
      },
      {
        find: "@pages",
        replacement: resolve(__dirname, "src/pages"),
      },
      {
        find: "@assets",
        replacement: resolve(__dirname, "src/assets"),
      },
    ],
  },
});
