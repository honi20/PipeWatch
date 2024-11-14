import { useTranslation } from "react-i18next";
import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import clsx from "clsx";
import { FloorListboxProps } from "../Type/PipeType";

export const FloorListbox: React.FC<FloorListboxProps> = ({
  onFloorChange,
  floorList,
  selectedFloor,
}) => {
  const { t } = useTranslation();
  const handleChange = (floor: number) => {
    onFloorChange(floor);
  };

  return (
    <div className="w-[150px] ">
      <Listbox value={selectedFloor} onChange={handleChange}>
        <div className="relative">
          <ListboxButton
            className={clsx(
              "relative w-full rounded-lg bg-white dark:bg-white/5 py-1.5 px-3 flex justify-between text-left text-sm/6 text-black dark:text-white",
              "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25 border dark:border-none"
            )}
          >
            {selectedFloor !== null
              ? selectedFloor > 0
                ? `${selectedFloor}${t(
                    "PipeViewer.ModelProperty.listbox.floorLabel"
                  )}`
                : `${t(
                    "PipeViewer.ModelProperty.listbox.basementLabel"
                  )} ${-selectedFloor}${t(
                    "PipeViewer.ModelProperty.listbox.floorLabel"
                  )}`
              : "-"}{" "}
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
          {floorList &&
            floorList.length > 0 &&
            floorList.map((item, idx) => (
              <ListboxOption
                key={idx}
                value={item}
                className="group flex cursor-default items-center gap-2 rounded-lg py-1.5 px-3 select-none data-[focus]:bg-white/10"
              >
                <div className="dark:text-white text-sm/6">
                  {item > 0
                    ? `${item}${t(
                        "PipeViewer.ModelProperty.listbox.floorLabel"
                      )}`
                    : `${t(
                        "PipeViewer.ModelProperty.listbox.basementLabel"
                      )} ${-item}${t(
                        "PipeViewer.ModelProperty.listbox.floorLabel"
                      )}`}
                </div>
              </ListboxOption>
            ))}
        </ListboxOptions>
      </Listbox>
    </div>
  );
};
