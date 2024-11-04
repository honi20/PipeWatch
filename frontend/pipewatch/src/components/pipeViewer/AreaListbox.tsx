import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import clsx from "clsx";
import { useState } from "react";
import {
  AreaType,
  AreaListboxProps,
} from "@src/components/pipeViewer/PipeType";

export const AreaListbox = ({ onAreaChange }: AreaListboxProps) => {
  // api연결해서 해당 유저의 arealist 불러오기
  const areaList: AreaType[] = [
    { id: 0, area: "장소" },
    { id: 1, area: "역삼 멀티캠퍼스" },
    { id: 2, area: "경덕이네 집" },
  ];
  const handleChange = (area: AreaType) => {
    setSelected(area);
    onAreaChange(area);
  };
  const [selected, setSelected] = useState<AreaType>(areaList[0]);
  return (
    <div className="w-[150px] pb-5">
      <Listbox value={selected} onChange={handleChange}>
        <div className="relative">
          <ListboxButton
            className={clsx(
              "relative w-full rounded-lg bg-white dark:bg-white/5 py-1.5 px-3 flex justify-between text-left text-sm/6 text-black dark:text-white",
              "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
            )}
          >
            {selected.area}
            <ExpandMoreIcon
              sx={{ color: "#5E5E5E" }}
              className="pl-1 transition-transform duration-200 group size-6"
            />
          </ListboxButton>
        </div>
        <ListboxOptions
          anchor="bottom"
          transition
          className={clsx(
            "w-[var(--button-width)] rounded-lg border border-gray-800 bg-white dark:bg-black p-1 [--anchor-gap:var(--spacing-1)] focus:outline-none",
            "transition duration-100 ease-in data-[leave]:data-[closed]:opacity-0"
          )}
        >
          {areaList.map((item) => (
            <ListboxOption
              key={item.id}
              value={item}
              className="group flex cursor-default items-center gap-2 rounded-lg py-1.5 px-3 select-none data-[focus]:bg-white/10"
            >
              <div className="dark:text-white text-sm/6">{item.area}</div>
            </ListboxOption>
          ))}
        </ListboxOptions>
      </Listbox>
    </div>
  );
};
