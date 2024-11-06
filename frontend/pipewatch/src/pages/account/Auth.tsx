import { useLocation } from "react-router-dom";

import { LoginPage } from "@pages/account/LoginPage";
import { FindPassword } from "@pages/account/FindPassword";
import { SignUpPage } from "@pages/account/SignUpPage";
import { ResetPasswordPage } from "@pages/account/ResetPasswordPage";
import { CompletedContact } from "@pages/CompletedContact";
import { CompletedResetPassword } from "@pages/account/CompletedResetPassword";

export const Auth = () => {
  const location = useLocation();
  const currentPath = location.pathname.split("/").pop();

  const currentPage = () => {
    switch (currentPath) {
      case "login":
        return <LoginPage />;
      case "find-pw":
        return <FindPassword />;
      case "sign-up":
        return <SignUpPage />;
      case "reset-pw":
        return <ResetPasswordPage />;
      case "reset-pw-completed":
        return <CompletedResetPassword />;
      case "completed":
        return <CompletedContact />;
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
