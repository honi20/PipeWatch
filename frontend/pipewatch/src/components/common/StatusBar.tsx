import { ReactNode } from "react";

interface Props {
  text: ReactNode;
  color: string;
  icon?: ReactNode;
}

export const StatusBar = ({ text, color, icon }: Props) => {
  return (
    <div
      className={`flex justify-center items-center gap-2 p-[6px] text-[20px] w-full text-white ${color}`}
    >
      {icon}
      {text}
    </div>
  );
};
