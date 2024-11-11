import React from "react";
import { RoleListbox } from "@src/components/enterprise/RoleListbox";
import { useTranslation } from "react-i18next";

interface EmployeeData {
  id: number;
  name: string;
  email: string;
  empNo: string;
  department: string;
  empClass: string;
  role: string;
}

interface TableProps {
  data: EmployeeData[];
}

export const EmployInfoTable: React.FC<TableProps> = ({ data }) => {
  const { t } = useTranslation();

  return (
    <div className="border border-block rounded-xl overflow-hidden max-w-[1080px] w-full">
      <table className="w-full border-collapse">
        <thead className="font-normal text-white bg-block">
          <tr>
            <th className="py-3 font-normal borde-b text-[15px]">
              {t("enterprise.table.name")}
            </th>
            <th className="font-normal border-b text-[15px]">
              {t("enterprise.table.email")}
            </th>
            <th className="font-normal border-b text-[15px]">
              {t("enterprise.table.employeeId")}
            </th>
            <th className="font-normal border-b text-[15px]">
              {t("enterprise.table.department")}
            </th>
            <th className="font-normal border-b text-[15px]">
              {t("enterprise.table.position")}
            </th>
            <th className="px-5 font-normal border-b text-[15px]">
              {t("enterprise.table.requestManagement")}
            </th>
          </tr>
        </thead>
        <tbody className="text-center text-black dark:text-white">
          {data.map((item) => (
            <tr key={item.id} className="">
              <td className="py-3 px-5  border-t text-[15px]">{item.name}</td>
              <td className=" px-5 border-t text-[15px] break-all">
                {item.email}
              </td>
              <td className="px-5 border-t text-[15px]">{item.empNo}</td>
              <td className="px-5 border-t text-[15px]">{item.department}</td>
              <td className="px-5 border-t text-[15px]">{item.empClass}</td>
              <td className="px-5 border-t text-[15px]">
                <div className="flex items-center justify-center h-full gap-2">
                  <RoleListbox currentRole={item.role} />
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
