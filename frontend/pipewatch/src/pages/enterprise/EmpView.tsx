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
      role: "staff",
    },
    {
      id: 2,
      name: "장지현",
      email: "jihyeon@paori.com",
      employeeId: "00000002",
      department: "-",
      position: "Director",
      role: "staff",
    },
    {
      id: 3,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 4,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 5,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 6,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 7,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 8,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 9,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 10,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 11,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 12,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 13,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 14,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
    },
    {
      id: 15,
      name: "최예헌",
      email: "honey@paori.com",
      employeeId: "00000003",
      department: "Management Support",
      position: "Manager",
      role: "staff",
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
