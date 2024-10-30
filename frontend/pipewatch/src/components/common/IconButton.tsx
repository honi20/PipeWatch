import { Button } from "@headlessui/react";
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
    <Button
      onClick={handleClick}
      className={`flex items-center justify-center gap-2 rounded-[30px] bg-${color} py-[16px] px-[16px] text-sm text-white data-[hover]:bg-${hoverColor}`}
    >
      {icon}
      <div className="text-[16px]">{text}</div>
    </Button>
  );
};
