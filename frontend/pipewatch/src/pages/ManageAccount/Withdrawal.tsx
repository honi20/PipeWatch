import WithdrawalCard from "@src/components/manageAccount/WithdrawalCard";

export const Withdrawal = () => {
  return (
    <div
      className="flex items-start justify-center w-full"
      style={{ height: "calc(100vh - 220px)" }}
    >
      <WithdrawalCard />
    </div>
  );
};
