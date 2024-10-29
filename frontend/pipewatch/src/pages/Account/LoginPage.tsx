import { Link } from "react-router-dom";

export const LoginPage = () => {
  return (
    <div>
      <h1>LoginPage</h1>

      <Link to="/account/auth/find-pw">비밀번호 찾기</Link>
      <Link to="/account/auth/sign-up">SignUp</Link>
    </div>
  );
};
