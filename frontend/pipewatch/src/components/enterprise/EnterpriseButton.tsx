interface Props {
  handleClick: () => void;
  text: string;
  color: string;
  hoverColor: string;
}

export const EnterpriseButton = ({
  handleClick,
  text,
  color,
  hoverColor,
}: Props) => {
  return (
    <button
      onClick={handleClick}
      className={`focus:outline border border-gray-500 flex items-center justify-center gap-2 rounded-[30px] ${color} py-[5px] px-[12px] text-white ${hoverColor}  h-[30px]`}
    >
      <div className="text-[12px]">{text}</div>
    </button>
  );
};
