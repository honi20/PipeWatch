import { useEffect, useState } from "react";

import { VerificationTable } from "@src/components/enterprise/VerificationTable";
import { useTranslation } from "react-i18next";

import { getApiClient } from "@src/stores/apiClient";

export const EmpVerification = () => {
  const [waitingList, setWaitingList] = useState([]);

  const { t } = useTranslation();

  const apiClient = getApiClient();

  const getWaitingList = async () => {
    try {
      const res = await apiClient.get(`/api/employees/waiting`);

      console.log("Waiting Employees Data: ", res.data.body.employees);
      setWaitingList(res.data.body.employees);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (waitingList.length === 0) {
      getWaitingList();
      console.log("test1");
    }
  }, []);

  useEffect(() => {
    if (waitingList.length === 0) {
      console.log("test2");
      // 없음 값.
    }
  }, [waitingList]);

  return (
    <div className="flex flex-col gap-10">
      <div>
        <h2 className="font-bold text-[32px]">
          {t("enterprise.verification.title")}
        </h2>
        <div className="">{t("enterprise.verification.instruction")}</div>
      </div>

      <VerificationTable data={waitingList} />
      {/* {waitingList.length > 0 ? (
        <VerificationTable data={waitingList} />
      ) : (
        <div>데이터 없음</div>
      )} */}
    </div>
  );
};
