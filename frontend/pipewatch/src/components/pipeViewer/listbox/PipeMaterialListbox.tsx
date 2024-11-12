import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { MaterialType } from "@src/components/pipeViewer/Type/MaterialType";
import clsx from "clsx";
import { useState, useEffect } from "react";

interface PipeMaterialListboxProps {
  value: string;
  pipeMaterialList: MaterialType[];
  onChange: (material: string) => void;
}

export const PipeMaterialListbox: React.FC<PipeMaterialListboxProps> = ({
  pipeMaterialList,
  value,
  onChange,
}) => {
  // 초기 선택된 재질 설정
  console.log(pipeMaterialList);
  const [selected, setSelected] = useState<string>(value || "-");
  const language = localStorage.getItem("language");
  console.log(language);

  const handleChange = (material: string) => {
    setSelected(material); // selected를 문자열로 업데이트
    onChange(material); // onChange에 문자열 전달
  };

  useEffect(() => {
    if (pipeMaterialList && pipeMaterialList.length > 0) {
      setSelected(value || pipeMaterialList[0].koreanName); // 첫 번째 항목을 초기값으로 설정
    }
  }, [pipeMaterialList, value]);

  return (
    <div className="max-w-[250px] w-full">
      <Listbox value={selected} onChange={handleChange}>
        <div className="relative">
          <ListboxButton
            className={clsx(
              "relative w-full rounded-md bg-black/40 py-2 pr-3 pl-5 flex justify-between text-left text-sm/6 text-white",
              "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
            )}
          >
            {selected}
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
            "w-[var(--button-width)] rounded-md border border-gray-800 bg-white dark:bg-black p-1 focus:outline-none [--anchor-gap:var(--spacing-1)]",
            "transition duration-100 ease-in data-[leave]:data-[closed]:opacity-0 z-10"
          )}
        >
          {pipeMaterialList &&
            pipeMaterialList.map((item, idx) => (
              <ListboxOption
                key={idx}
                value={item.koreanName} // 여기서 item 전체 객체를 전달
                className="group flex cursor-default items-center gap-2 rounded-lg py-1.5 px-3 select-none data-[focus]:bg-white/10"
              >
                <div className="dark:text-white text-sm/6">
                  {item.koreanName}
                </div>
              </ListboxOption>
            ))}
        </ListboxOptions>
      </Listbox>
    </div>
  );
};
