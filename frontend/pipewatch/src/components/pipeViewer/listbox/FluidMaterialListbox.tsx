import { useTranslation } from "react-i18next";
import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import clsx from "clsx";
import { useState, useEffect } from "react";
import { MaterialType } from "@src/components/pipeViewer/Type/MaterialType";

interface FluidMaterialListboxProps {
  value: number;
  fluidMaterialList?: MaterialType[];
  onChange: (materialId: number) => void;
}

export const FluidMaterialListbox: React.FC<FluidMaterialListboxProps> = ({
  fluidMaterialList,
  value,
  onChange,
}) => {
  const { t } = useTranslation();
  // 초기 선택된 재질 설정
  const [selected, setSelected] = useState<number>(value || 4);

  // value가 변경될 때마다 selected 값을 업데이트
  useEffect(() => {
    setSelected(value || 4);
  }, [value, fluidMaterialList]);

  // materialId 선택 변경될 경우
  const handleChange = (materialId: number) => {
    const material = fluidMaterialList?.find(
      (item) => item.materialId === materialId
    );
    if (material) {
      setSelected(materialId);
      onChange(materialId);
    }
  };

  return (
    <div className="max-w-[250px] w-full">
      <Listbox value={selected} onChange={handleChange}>
        <div className="relative">
          <ListboxButton
            className={clsx(
              "relative w-full rounded-md bg-black/40 py-2 pr-3 pl-5  flex justify-between text-left text-sm/6 text-white",
              "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
            )}
          >
            {fluidMaterialList && fluidMaterialList.length > 0 ? (
              fluidMaterialList
                .filter((item) => item.materialId === selected)
                .map((filteredItem) => (
                  <div key={filteredItem.materialId}>
                    <p>
                      {t(
                        `PipeViewer.ModelProperty.fluidMaterialList.${filteredItem.materialId}`
                      )}
                    </p>
                  </div>
                ))
            ) : (
              <p>-</p> // 재질이 없을 때 기본 메시지
            )}
            <ExpandMoreIcon
              sx={{ color: "#5E5E5E" }}
              className="absolute transition-transform duration-200 group size-6 right-3"
            />
          </ListboxButton>
        </div>
        <ListboxOptions
          anchor="bottom"
          transition
          className={clsx(
            "w-[var(--button-width)] rounded-md border border-gray-800 bg-white dark:bg-black p-1 [--anchor-gap:var(--spacing-1)] focus:outline-none",
            "transition duration-100 ease-in data-[leave]:data-[closed]:opacity-0 z-10"
          )}
        >
          {fluidMaterialList &&
            fluidMaterialList.map((item, idx) => (
              <ListboxOption
                key={idx}
                value={item.materialId}
                className="group flex cursor-default items-center gap-2 rounded-lg py-1.5 px-3 select-none data-[focus]:bg-white/10"
              >
                <div className="dark:text-white text-sm/6">
                  {t(
                    `PipeViewer.ModelProperty.fluidMaterialList.${item.materialId}`
                  )}
                </div>
              </ListboxOption>
            ))}
        </ListboxOptions>
      </Listbox>
    </div>
  );
};
