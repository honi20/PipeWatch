import { LoginPage } from "./LoginPage";
import { FindPassword } from "./FindPassword";
import { SignUpPage } from "./SignUpPage";

import { useLocation } from "react-router-dom";

export const Account = () => {
  const location = useLocation();
  const currentPath = location.pathname.split("/").pop();
  // console.log(currentPath);

  const currentPage = () => {
    switch (currentPath) {
      case "login":
        return <LoginPage />;
      case "find-pw":
        return <FindPassword />;
      case "sign-up":
        return <SignUpPage />;
    }
  };

  return (
    <div className="flex items-center justify-center w-full h-full">
      {/* <LoginPage /> :
      <FindPassword />
      <SignUpPage /> */}
      {currentPage()}
    </div>
  );
};
