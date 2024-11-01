import React from "react";
import { EnterpriseButton } from "@components/enterprise/EnterpriseButton";

interface VerificationData {
  id: number;
  name: string;
  email: string;
  employeeId: string;
  department: string;
  position: string;
}

interface TableProps {
  data: VerificationData[];
}

export const VerificationTable: React.FC<TableProps> = ({ data }) => {
  return (
    <div className="border border-block rounded-xl overflow-hidden max-w-[1080px] w-full">
      <table className="w-full border-collapse">
        <thead className="font-normal text-white bg-block">
          <tr>
            <th className="py-3 font-normal borde-b text-[15px]">Name</th>
            <th className="font-normal border-b text-[15px]">Email</th>
            <th className="font-normal border-b text-[15px]">Employee ID</th>
            <th className="font-normal border-b text-[15px]">Department</th>
            <th className="font-normal border-b text-[15px]">Position</th>
            <th className="px-5 font-normal border-b text-[15px]">
              Request Management
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
              <td className="px-5 border-t text-[15px]">{item.employeeId}</td>
              <td className="px-5 border-t text-[15px]">{item.department}</td>
              <td className="px-5 border-t text-[15px]">{item.position}</td>
              <td className="px-5 border-t text-[15px]">
                <div className="flex items-center justify-center h-full gap-2">
                  <EnterpriseButton
                    handleClick={() => console.log("button Clicked")}
                    text="승인"
                    color={"bg-primary-200"}
                    hoverColor={"hover:bg-primary-200/80"}
                  />
                  <EnterpriseButton
                    handleClick={() => console.log("button Clicked")}
                    text="거부"
                    color={"dark:bg-block bg-gray-500"}
                    hoverColor={"hover:dark:bg-block/80 hover:bg-gray-500/80"}
                  />
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
