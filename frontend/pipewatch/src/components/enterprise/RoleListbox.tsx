import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { EnterpriseButton } from "@components/enterprise/EnterpriseButton";
import clsx from "clsx";
import { useState } from "react";
import { useTranslation } from "react-i18next";

type Role = {
  id: number;
  role: string;
};

type Props = {
  currentRole: string;
};

export const RoleListbox = ({ currentRole }: Props) => {
  const { t } = useTranslation();
  const roles = [
    { id: 1, role: "admin" },
    { id: 2, role: "staff" },
  ];
  const [selected, setSelected] = useState(roles[1]);
  const [isRoleChanged, setIsRoleChanged] = useState(false);

  const handleChange = (selected: Role) => {
    setSelected(selected);
    if (currentRole !== selected.role) {
      // console.log("Role Changed");
      setIsRoleChanged(true);
    } else {
      setIsRoleChanged(false);
    }
  };

  return (
    <div className="flex items-center justify-center gap-2">
      <div className="w-[90px]">
        <Listbox value={selected} onChange={handleChange}>
          {/* <Listbox value={selected} onChange={setSelected}> */}
          <div className="relative">
            <ListboxButton
              className={clsx(
                "relative w-full rounded-lg bg-white dark:bg-white/5 py-1.5 px-3 flex justify-between text-left text-sm/6 text-black dark:text-white",
                "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
              )}
            >
              {selected && selected.role === "admin"
                ? t("enterprise.view.buttons.admin")
                : t("enterprise.view.buttons.staff")}
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
            {roles.map((role) => (
              <ListboxOption
                key={role.role}
                value={role}
                className="group flex cursor-default items-center gap-2 rounded-lg py-1.5 px-3 select-none data-[focus]:bg-white/10"
              >
                <div className="dark:text-white text-sm/6">
                  {role.role === "admin"
                    ? t("enterprise.view.buttons.admin")
                    : t("enterprise.view.buttons.staff")}
                </div>
              </ListboxOption>
            ))}
          </ListboxOptions>
        </Listbox>
      </div>
      <EnterpriseButton
        handleClick={() => console.log("button Clicked")}
        text={t("enterprise.view.buttons.update")}
        color={
          isRoleChanged
            ? "bg-primary-200 dark:bg-primary-500"
            : "bg-gray-500 dark:bg-block"
        }
        hoverColor={
          isRoleChanged
            ? "hover:dark:bg-primary-200/80 hover:bg-primary-500/80"
            : "hover:dark:bg-block/80 hover:bg-gray-500/80"
        }
      />
    </div>
  );
};
