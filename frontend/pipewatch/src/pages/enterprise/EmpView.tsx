import { EmployInfoTable } from "@src/components/enterprise/EmployInfoTable";
import { useTranslation } from "react-i18next";

export const EmpView = () => {
  const { t } = useTranslation();

  const employData = [
    {
      id: 1,
      name: "고민혁",
      email: "thinking@paori.com",
      employeeId: "00000001",
      department: "Management Support",
      position: "Staff",
    },
    {
      id: 2,
      name: "장지현",
      email: "jihyeon@paori.com",
      employeeId: "00000002",
      department: "-",
      position: "Director",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Managerment Support",
      position: "Manager",
    },
  ];
  return (
    <div className="flex flex-col gap-10">
      <div>
        <h2 className="font-bold text-[32px]">{t("enterprise.view.title")}</h2>
        <div className="">{t("enterprise.view.instruction")}</div>
      </div>
      <EmployInfoTable data={employData} />
    </div>
  );
};
