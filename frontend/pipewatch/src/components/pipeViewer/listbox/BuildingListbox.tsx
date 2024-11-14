import { useTranslation } from "react-i18next";
import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import clsx from "clsx";
import { useState } from "react";
import { BuildingListboxProps } from "../Type/PipeType";

export const BuildingListbox = ({
  onBuildingChange,
  buildingList,
}: BuildingListboxProps) => {
  const { t } = useTranslation();
  const enhancedBuildingList = [
    { building: t("PipeViewer.ModelProperty.listbox.place") },
    ...buildingList,
  ];

  // 장소 클릭한 함수
  const handleChange = (building: string) => {
    setSelected(building);
    if (building === t("PipeViewer.ModelProperty.listbox.place")) {
      onBuildingChange(null);
    } else {
      onBuildingChange(building);
    }
  };

  const [selected, setSelected] = useState<string>(
    t("PipeViewer.ModelProperty.listbox.place")
  );

  return (
    <div className="w-[150px]">
      <Listbox value={selected} onChange={handleChange}>
        <div className="relative">
          <ListboxButton
            className={clsx(
              "relative w-full rounded-lg bg-white dark:bg-white/5 py-1.5 px-3 flex justify-between text-left text-sm/6 text-black dark:text-white truncate",
              "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25 border dark:border-none"
            )}
          >
            {selected === "building" || selected === "장소"
              ? t("PipeViewer.ModelProperty.listbox.place")
              : selected}
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
          {enhancedBuildingList.map((item, idx) => (
            <ListboxOption
              key={idx}
              value={item.building}
              className="group flex cursor-default items-center gap-2 rounded-lg py-1.5 px-3 select-none data-[focus]:bg-white/10"
            >
              <div className="w-full overflow-hidden truncate dark:text-white text-sm/6 whitespace-nowrap">
                {item.building}
              </div>
            </ListboxOption>
          ))}
        </ListboxOptions>
      </Listbox>
    </div>
  );
};
