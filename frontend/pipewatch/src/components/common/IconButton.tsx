import { ReactNode } from "react";

interface Props {
  handleClick: () => void;
  text: string;
  color: string;
  hoverColor: string;
  icon: ReactNode;
}

export const IconButton = ({
  handleClick,
  text,
  color,
  hoverColor,
  icon,
}: Props) => {
  return (
    <button
      onClick={handleClick}
      className={`focus:outline-none my-4 flex items-center justify-center gap-2 rounded-[30px] ${color} py-[16px] px-[16px] text-sm text-white ${hoverColor}`}
    >
      {icon}
      <div className="text-[16px]">{text}</div>
    </button>
  );
};
