import "./App.css";

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "./pages/Layout";

import { Home } from "./pages/Home";
import { AboutUs } from "./pages/AboutUs";
import { AboutService } from "./pages/AboutService";
import { AboutTeam } from "./pages/AboutTeam";
import { PipeGenerator } from "./pages/PipeGenerator";
import { PipeViewer } from "./pages/PipeViewer";
import { Contact } from "./pages/Contact";

import { Account } from "./pages/account/Account";
import { LoginPage } from "./pages/account/LoginPage";
import { FindPassword } from "./pages/account/FindPassword";
import { SignUpPage } from "./pages/account/SignUpPage";

import { EmpVerification } from "./pages/EmpVerification";
import { EmpView } from "./pages/EmpView";

import { ChangePassword } from "./pages/ChangePassword";
import { EditInfo } from "./pages/EditInfo";
import { DeleteAccount } from "./pages/DeleteAccount";
import { ManageAccount } from "./pages/ManageAccount";

import { TakePhoto } from "./components/pipeGenerator/TakePhoto";
import { UploadModel } from "./components/pipeGenerator/UploadModel";
import { InputData } from "./components/pipeGenerator/InputData";
import { Rendering } from "./components/pipeGenerator/Rendering";
import { Completed } from "./components/pipeGenerator/Completed";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />} />

            <Route path="/about-us" element={<AboutUs />} />
            <Route path="/about-us/service" element={<AboutService />} />
            <Route path="/about-us/team" element={<AboutTeam />} />

            <Route path="/pipe-viewer" element={<PipeViewer />} />
            <Route path="/pipe-generator" element={<PipeGenerator />}>
              <Route
                path="/pipe-generator/take-photo"
                element={<TakePhoto />}
              />
              <Route
                path="/pipe-generator/upload-model"
                element={<UploadModel />}
              />
              <Route
                path="/pipe-generator/input-data"
                element={<InputData />}
              />
              <Route path="/pipe-generator/rendering" element={<Rendering />} />
              <Route path="/pipe-generator/completed" element={<Completed />} />
            </Route>
            <Route path="/contact" element={<Contact />} />

            <Route path="/enterprise" element={<EmpVerification />}>
              <Route index element={<EmpVerification />} />
              <Route path="/enterprise/view" element={<EmpView />} />
            </Route>

            <Route path="/account/manage" element={<ManageAccount />} />
            <Route path="/account/manage/edit-info" element={<EditInfo />} />
            <Route
              path="/account/manage/change-pw"
              element={<ChangePassword />}
            />
            <Route
              path="/account/manage/delete-account"
              element={<DeleteAccount />}
            />

            <Route path="/account/auth" element={<Account />}>
              <Route path="/account/auth/login" element={<LoginPage />} />
              <Route path="/account/auth/find-pw" element={<FindPassword />} />
              <Route path="/account/auth/sign-up" element={<SignUpPage />} />
            </Route>
          </Route>
        </Routes>
      </Router>
    </>
  );
}

export default App;
