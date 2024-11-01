import { VerificationTable } from "@src/components/enterprise/VerificationTable";

export const EmpVerification = () => {
  const verificationData = [
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
  ];
  return (
    <div className="flex flex-col gap-10">
      <div>
        <h2 className="font-bold text-[32px]">사원 인증</h2>
        <div className="">사원 인증 신청을 승인하거나 거부할 수 있습니다.</div>
      </div>
      <VerificationTable data={verificationData} />
    </div>
  );
};
