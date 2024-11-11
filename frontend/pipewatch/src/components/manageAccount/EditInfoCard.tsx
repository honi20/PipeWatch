import { useTranslation } from "react-i18next";
import { Button, Input } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import { useEffect, useState, ChangeEvent } from "react";

import { getApiClient } from "@src/stores/apiClient";

interface Employee {
  empNo: number;
  department: string;
  empClass: string;
}

interface User {
  name: string;
  email: string;
  role: string;
  state: string;
  enterpriseName: string | null;
  employee: Employee;
}

const EditInfoCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [userInfo, setUserInfo] = useState<User | null>(null);

  const apiClient = getApiClient();

  const getUserInfo = async () => {
    try {
      const res = await apiClient.get(`/api/users/mypage`);
      console.log("userInfo: ", res.data.body);
      setUserInfo(res.data.body);
    } catch (err) {
      console.log(err);
    }
  };

  const updateUserInfo = async (department: string, empClass: string) => {
    try {
      const res = await apiClient.put(`/api/users/mypage`, {
        department: department,
        empClass: empClass,
      });
      console.log("회원정보 수정 성공: ", res.data.header.message);
      window.location.reload();
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    getUserInfo();
  }, []);

  useEffect(() => {
    if (userInfo?.employee?.department) {
      setDepartment(userInfo.employee.department);
    }
    if (userInfo?.employee?.empClass) {
      setEmpClass(userInfo.employee.empClass);
    }
  }, [userInfo]);

  const [empClass, setEmpClass] = useState("");
  const [department, setDepartment] = useState("");

  const handlePositionChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setEmpClass(value);
  };
  const handleDepartmentChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setDepartment(value);
  };

  const isFormValid: boolean =
    empClass !== "" &&
    department !== "" &&
    (userInfo?.employee?.department !== department ||
      userInfo?.employee?.empClass !== empClass);

  console.log(empClass, department);

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      <div className="flex justify-center font-semibold text-[28px]">
        {t("manageAccount.dashboard.editInfo")}
      </div>

      {/* 수정사항 전체 Box */}
      <div className="flex flex-col gap-[30px] my-[20px]">
        <div className="flex px-[20px] text-[16px]">
          <div className="flex-[3] font-bold">
            {t("manageAccount.editInfo.registrationEmail")}
          </div>
          <div className="flex-[4]">{userInfo?.email}</div>
        </div>

        <div className="flex px-[20px] text-[16px]">
          <div className="flex-[3] font-bold">
            {t("manageAccount.editInfo.companyName")}
          </div>
          <div className="flex-[4]">{userInfo?.enterpriseName}</div>
        </div>

        <div className="flex px-[20px] text-[16px]">
          <div className="flex-[3] font-bold">
            {t("manageAccount.editInfo.employeeId")}
          </div>
          <div className="flex-[4]">{userInfo?.employee.empNo}</div>
        </div>

        {/* 부서 Input */}
        <div className="flex items-center px-[20px] text-[16px]">
          <div className="flex-[3] font-bold">
            {t("manageAccount.editInfo.department")}
          </div>
          <div className="flex-[4]">
            <Input
              type="text"
              value={department}
              onChange={handleDepartmentChange}
              className="h-full w-full px-[20px] py-[10px] text-gray-800 rounded-[5px] focus:outline-success"
              required
            />
          </div>
        </div>

        {/* 직책 Input */}
        <div className="flex px-[20px] text-[16px]">
          <div className="flex-[3] font-bold">
            {t("manageAccount.editInfo.position")}
          </div>
          <div className="flex-[4]">
            <Input
              type="text"
              value={empClass}
              onChange={handlePositionChange}
              className="h-full w-full px-[20px] py-[10px] text-gray-800 rounded-[5px] focus:outline-success"
              required
            />
          </div>
        </div>
      </div>

      <div className="flex flex-col gap-[20px]">
        <Button
          className={`flex items-center justify-center h-[56px] w-full px-[30px] text-white rounded-[20px] 
            ${
              isFormValid
                ? "bg-button-background hover:bg-button-background/80"
                : "bg-gray-800 cursor-not-allowed"
            }`}
          // onClick={() => navigate("/account/manage/edit-info/completed")}
          onClick={() => updateUserInfo(department, empClass)}
          disabled={!isFormValid}
        >
          {t("manageAccount.editInfo.editPersonalInfo")}
        </Button>
      </div>

      <div
        className="flex justify-center gap-2 underline whitespace-normal cursor-pointer space-normal"
        onClick={() => navigate("/account/manage")}
      >
        {t("manageAccount.goToPreviousCard")}
      </div>
    </div>
  );
};

export default EditInfoCard;
