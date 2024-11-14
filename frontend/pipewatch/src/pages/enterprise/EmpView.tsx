import { useEffect, useState, ChangeEvent } from "react";

import { EnterpriseButton } from "@components/enterprise/EnterpriseButton";

import { Input } from "@headlessui/react";

import { EmployInfoTable } from "@src/components/enterprise/EmployInfoTable";
import { useTranslation } from "react-i18next";

import { getApiClient } from "@src/stores/apiClient";

import { StatusBar } from "@components/common/StatusBar";

import { statusStore } from "@src/stores/statusStore";

export const EmpView = () => {
  const { isSuccess } = statusStore();
  const [isEmpty, setIsEmpty] = useState(false);

  const [employeeList, setEmployeeList] = useState([]);
  const [tempEmployeeList, setTempEmployeeList] = useState([]);

  const { t } = useTranslation();

  const getEmployeeList = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient.get(`/api/employees`);

      console.log("Current Employees Data: ", res.data.body.employees);
      if (res.data.body.employees.length === 0) {
        setIsEmpty(true);
      }
      setEmployeeList(res.data.body.employees);
      setTempEmployeeList(res.data.body.employees);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    getEmployeeList();
  }, []);

  const [searchKeyword, setSearchKeyword] = useState("");

  const handleKeywordChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchKeyword(value);
  };

  const searchEmployee = async (keyword: string) => {
    try {
      const res = await apiClient.get(`/api/employees/search`, {
        params: { keyword: keyword },
      });
      console.log("검색 성공: ", res.data.body.employees);
      setEmployeeList(res.data.body.employees);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="flex flex-col gap-6">
      {isSuccess && (
        <StatusBar
          text={t("enterprise.view.status.updateComplete")}
          icon={""}
          // icon={<CancelIcon sx={{ fontSize: "20px" }} />}
          color={"bg-success"}
        />
      )}
      <div>
        <h2 className="font-bold text-[32px]">{t("enterprise.view.title")}</h2>
        <div className="">{t("enterprise.view.instruction")}</div>
      </div>
      <div className="flex items-center justify-end max-w-[1080px] w-full gap-4">
        <Input
          type="text"
          value={searchKeyword}
          onChange={handleKeywordChange}
          className="h-full w-[250px] px-[10px] py-[10px] text-gray-800 rounded-[5px] border-[1px] border-gray border-solid focus:outline-success"
        />
        <EnterpriseButton
          handleClick={() => searchEmployee(searchKeyword)}
          text={t("enterprise.view.buttons.search.label")}
          color={"dark:bg-block bg-gray-500"}
          hoverColor={"hover:dark:bg-block/80 hover:bg-gray-500/80"}
        />
        <EnterpriseButton
          handleClick={() => setEmployeeList(tempEmployeeList)}
          text={t("enterprise.view.buttons.search.viewAllEmployees")}
          color={"dark:bg-primary-500 bg-primary-200"}
          hoverColor={"hover:dark:bg-primary-500/80 hover:bg-primary-200/80"}
        />
      </div>
      <EmployInfoTable data={employeeList} isEmpty={isEmpty} />
    </div>
  );
};
