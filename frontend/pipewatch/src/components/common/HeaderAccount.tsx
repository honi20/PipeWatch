import { Link } from "react-router-dom";

import LogoHeaderDark from "@assets/images/logo_header_dark.png";
import LogoHeaderLight from "@assets/images/logo_header_light.png";

type Props = {
  currentTheme: string;
};

export const HeaderAccount = ({ currentTheme }: Props) => {
  const isDark = currentTheme === "dark";

  return (
    <header className="fixed top-0 left-0 right-0 z-20 flex items-center justify-start p-2 py-6 text-black dark:text-white">
      <Link className="p-2 hover:text-primary-200 " to="/">
        <img
          className="w-16 h-10"
          src={isDark ? LogoHeaderDark : LogoHeaderLight}
        />
      </Link>
    </header>
  );
};
