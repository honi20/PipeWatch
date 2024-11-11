import { useEffect, useState } from "react";

import { EmployInfoTable } from "@src/components/enterprise/EmployInfoTable";
import { useTranslation } from "react-i18next";

import { getApiClient } from "@src/stores/apiClient";

export const EmpView = () => {
  const [employeeList, setEmployeeList] = useState([]);

  const { t } = useTranslation();

  const apiClient = getApiClient();

  const getEmployeeList = async () => {
    try {
      const res = await apiClient.get(`/api/management`);

      console.log("Current Employees Data: ", res.data.body.employees);
      setEmployeeList(res.data.body.employees);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    getEmployeeList();
  }, []);

  return (
    <div className="flex flex-col gap-10">
      <div>
        <h2 className="font-bold text-[32px]">{t("enterprise.view.title")}</h2>
        <div className="">{t("enterprise.view.instruction")}</div>
      </div>
      <EmployInfoTable data={employeeList} />
      {/* {employeeList.length > 0 ? (
      <EmployInfoTable data={employeeList} />
      ) : (
        <div>데이터 없음</div>
      )} */}
    </div>
  );
};
