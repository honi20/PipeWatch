import "./App.css";

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "./pages/Layout";

import { Home } from "./pages/Home";
import { AboutUs } from "./pages/AboutUs";
import { AboutService } from "./pages/AboutService";
import { AboutTeam } from "./pages/AboutTeam";
import { CreateModel } from "./pages/CreateModel";
import { ModelList } from "./pages/ModelList";
import { Contact } from "./pages/Contact";

import { Account } from "./pages/Account/Account";
import { LoginPage } from "./pages/Account/LoginPage";
import { FindPassword } from "./pages/Account/FindPassword";
import { SignUpPage } from "./pages/Account/SignUpPage";

import { EmpVerification } from "./pages/EmpVerification";
import { EmpView } from "./pages/EmpView";

import { ChangePassword } from "./pages/ChangePassword";
import { EditInfo } from "./pages/EditInfo";
import { DeleteAccount } from "./pages/DeleteAccount";
import { ManageAccount } from "./pages/ManageAccount";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />} />

            <Route path="/about-us/" element={<AboutUs />} />
            <Route path="/about-us/service" element={<AboutService />} />
            <Route path="/about-us/team" element={<AboutTeam />} />

            <Route path="/model-list" element={<ModelList />} />
            <Route path="/create-model" element={<CreateModel />} />
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
