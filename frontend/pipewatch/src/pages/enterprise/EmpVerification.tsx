import { useEffect, useState } from "react";

import { VerificationTable } from "@src/components/enterprise/VerificationTable";
import { useTranslation } from "react-i18next";

import { getApiClient } from "@src/stores/apiClient";

export const EmpVerification = () => {
  const [waitingList, setWaitingList] = useState([]);
  const [isEmpty, setIsEmpty] = useState(false);

  const { t } = useTranslation();

  const apiClient = getApiClient();

  const getWaitingList = async () => {
    try {
      const res = await apiClient.get(`/api/employees/waiting`);

      console.log("Waiting Employees Data: ", res.data.body.employees);
      if (res.data.body.employees.length === 0) {
        setIsEmpty(true);
      }
      setWaitingList(res.data.body.employees);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    getWaitingList();
  }, []);

  return (
    <div className="flex flex-col gap-10">
      <div>
        <h2 className="font-bold text-[32px]">
          {t("enterprise.verification.title")}
        </h2>
        <div className="">{t("enterprise.verification.instruction")}</div>
      </div>

      <VerificationTable data={waitingList} isEmpty={isEmpty} />
    </div>
  );
};
