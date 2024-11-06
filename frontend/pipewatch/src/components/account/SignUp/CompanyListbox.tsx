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
  CompanyType,
  CompanyListboxProps,
} from "@src/components/account/SignUp/inputType";

export const CompanyListBox = ({ onCompanyChange }: CompanyListboxProps) => {
  // api연결해서 해당 유저의 companyList 불러오기
  const companyList: CompanyType[] = [
    { id: 0, company: "개굴전자" },
    { id: 1, company: "너굴제약" },
    { id: 2, company: "꼬꼬에너지" },
  ];

  const handleChange = (company: CompanyType) => {
    setSelected(company);
    onCompanyChange(company);
  };
  const [selected, setSelected] = useState<CompanyType>(companyList[0]);
  return (
    <div className="w-full h-full">
      <Listbox value={selected} onChange={handleChange}>
        <div className="relative h-full">
          <ListboxButton
            className={clsx(
              "relative w-full h-full rounded-lg border-solid border-[1px] border-gray-800 bg-white dark:bg-white/5 py-1.5 px-5 flex justify-between items-center text-left text-[16px] text-black dark:text-white",
              "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
            )}
          >
            {selected.company}
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
          {companyList.map((item) => (
            <ListboxOption
              key={item.id}
              value={item}
              className="group h-[56px] font-[16px] flex cursor-default items-center gap-2 rounded-lg py-1.5 px-5 select-none data-[focus]:bg-white/10"
            >
              <div className="dark:text-white text-sm/6">{item.company}</div>
            </ListboxOption>
          ))}
        </ListboxOptions>
      </Listbox>
    </div>
  );
};
