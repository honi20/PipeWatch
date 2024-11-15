import { useEffect } from "react";
import { useLocation } from "react-router-dom";

import "./App.css";

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "@pages/Layout";

import { Home } from "@pages/Home";
import { AboutUs } from "@pages/AboutUs";
import { AboutService } from "@pages/AboutService";
import { AboutTeam } from "@pages/AboutTeam";
import { PipeGenerator } from "@pages/PipeGenerator";
import { PipeViewer } from "@pages/PipeViewer";
import { Contact } from "@pages/Contact";

import { TermsAndPolicy } from "@pages/TermsAndPolicy";

import { Auth } from "@pages/account/Auth";
import { LoginPage } from "@pages/account/LoginPage";
import { FindPassword } from "@pages/account/FindPassword";
import { FindPasswordPending } from "@pages/account/FindPasswordPending";
import { SignUpPage } from "@pages/account/SignUpPage";
import { ResetPasswordPage } from "@pages/account/ResetPasswordPage";
import { CompletedResetPassword } from "@pages/account/CompletedResetPassword";

import { Enterprise } from "@pages/Enterprise";
import { EmpView } from "@pages/enterprise/EmpView";
import { EmpVerification } from "@pages/enterprise/EmpVerification";

import { UpdatePassword } from "@pages/ManageAccount/UpdatePassword";
import { EditInfo } from "@pages/ManageAccount/EditInfo";
import { Withdrawal } from "@pages/ManageAccount/Withdrawal";
import { ManageAccount } from "@pages/ManageAccount/ManageAccount";
import { CompletedManageAccount } from "@pages/ManageAccount/CompletedManageAccount";

import { TakePhoto } from "@components/pipeGenerator/TakePhoto";
import { UploadModel } from "@components/pipeGenerator/UploadModel";
import { InputData } from "@components/pipeGenerator/InputData";
import { Rendering } from "@components/pipeGenerator/Rendering";
import { Completed } from "@components/pipeGenerator/Completed";

import { CompletedContact } from "@pages/CompletedContact";

import { getApiClient } from "@src/stores/apiClient";
import { useUserStore } from "@src/stores/userStore";

function App() {
  const ScrollToTop = () => {
    const { pathname } = useLocation();

    useEffect(() => {
      window.scrollTo(0, 0);
    }, [pathname]);

    return null;
  };

  const { setName, setEnterpriseName, setRole, setUserState } = useUserStore();

  const saveUserInfo = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient.get("/api/users/profile");
      const userInfo = res.data.body;
      console.log("userInfo in App.tsx: ", userInfo);
      setName(userInfo.name);
      setRole(userInfo.role);
      setUserState(userInfo.state);
      setEnterpriseName(userInfo.enterpriseName);
    } catch (err) {
      console.error("UserInfo 저장 실패", err);

      throw err;
    }
  };

  useEffect(() => {
    const accessToken = sessionStorage.getItem("accessToken");
    if (!accessToken) {
      console.log("accessToken이 없습니다.");
      return;
    }
    saveUserInfo();
  }, []);

  return (
    <>
      <Router>
        <ScrollToTop />
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />} />

            <Route path="/about-us" element={<AboutUs />} />
            <Route path="/about-us/service" element={<AboutService />} />
            <Route path="/about-us/team" element={<AboutTeam />} />
            <Route path="/terms-and-policy" element={<TermsAndPolicy />} />

            <Route path="/pipe-viewer" element={<PipeViewer />} />
            <Route path="/pipe-generator" element={<PipeGenerator />}>
              <Route
                path="/pipe-generator/take-photo"
                element={<TakePhoto />}
              />
              <Route
                path="/pipe-generator/upload-model/:action"
                element={<UploadModel />}
              />
              <Route
                path="/pipe-generator/upload-model"
                element={<UploadModel />}
              />
              <Route
                path="/pipe-generator/input-data"
                element={<InputData />}
              />
              {/* <Route path="/pipe-generator/rendering" element={<Rendering />} /> */}
              <Route
                path="/pipe-generator/rendering/:modelId"
                element={<Rendering />}
              />
              <Route path="/pipe-generator/completed" element={<Completed />} />
            </Route>

            <Route path="/contact">
              <Route index element={<Contact />} />
              <Route path="/contact/completed" element={<CompletedContact />} />
            </Route>

            <Route path="/enterprise" element={<Enterprise />}>
              <Route index element={<EmpVerification />} />
              <Route path="verification" element={<EmpVerification />} />
              <Route path="view" element={<EmpView />} />
            </Route>

            <Route path="/account/manage" element={<ManageAccount />} />
            <Route path="/account/manage/edit-info" element={<EditInfo />} />
            <Route
              path="/account/manage/update-pw"
              element={<UpdatePassword />}
            />
            <Route path="/account/manage/withdrawal" element={<Withdrawal />} />
            <Route
              path="/account/manage/:action/completed"
              element={<CompletedManageAccount />}
            />

            <Route path="/account/auth" element={<Auth />}>
              <Route path="/account/auth/login" element={<LoginPage />} />
              <Route path="/account/auth/find-pw" element={<FindPassword />} />
              <Route
                path="/account/auth/find-pw/pending"
                element={<FindPasswordPending />}
              />
              <Route path="/account/auth/sign-up" element={<SignUpPage />} />
              <Route
                path="/account/auth/reset-pw"
                element={<ResetPasswordPage />}
              />
              <Route
                path="/account/auth/reset-pw-completed"
                element={<CompletedResetPassword />}
              />
              <Route
                path="/account/auth/completed"
                element={<CompletedContact />}
              />
            </Route>
          </Route>
        </Routes>
      </Router>
    </>
  );
}

export default App;
