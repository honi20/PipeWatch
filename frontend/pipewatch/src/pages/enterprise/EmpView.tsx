import { EmployInfoTable } from "@src/components/enterprise/EmployInfoTable";
export const EmpView = () => {
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
        <h2 className="font-bold text-[32px]">사원 정보 조회/변경</h2>
        <div className="">
          사원의 관리자 권한을 설정하고 관리할 수 있습니다.
        </div>
      </div>
      <EmployInfoTable data={employData} />
    </div>
  );
};
