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

import { getApiClient } from "@src/stores/apiClient";
import { statusStore } from "@src/stores/statusStore";

type Role = {
  id: number;
  role: string;
};

type Props = {
  currentRole: string;
  uuid: string;
};

export const RoleListbox = ({ currentRole, uuid }: Props) => {
  const { setIsSuccess } = statusStore();
  const apiClient = getApiClient();

  const { t, i18n } = useTranslation();
  const isKorean = i18n.language === "ko";

  const roles = [
    { id: 1, role: "ADMIN" },
    { id: 2, role: "EMPLOYEE" },
  ];

  const [selected, setSelected] = useState(
    roles.find((r) => r.role === currentRole) || roles[0]
  );

  const handleChange = (selected: Role) => {
    setSelected(selected);
  };

  const handleRoleUpdate = async (newRole: string, uuid: string) => {
    try {
      const res = await apiClient.patch(`/api/employees/role`, {
        userUuid: uuid,
        newRole: newRole,
      });
      console.log("권한 업데이트 성공:", res.data);
      setIsSuccess(true);
      window.location.reload();
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="flex items-center justify-center gap-2">
      <div className={isKorean ? "w-[90px]" : "w-[120px]"}>
        <Listbox value={selected} onChange={handleChange}>
          <div className="relative">
            <ListboxButton
              className={clsx(
                "relative w-full rounded-lg bg-white dark:bg-white/5 py-1.5 px-3 flex justify-between text-left text-sm/6 text-black dark:text-white",
                "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
              )}
            >
              {selected && selected.role === "ADMIN"
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
                <div className=" dark:text-white text-sm/6">
                  {role.role === "ADMIN"
                    ? t("enterprise.view.buttons.admin")
                    : t("enterprise.view.buttons.staff")}
                </div>
              </ListboxOption>
            ))}
          </ListboxOptions>
        </Listbox>
      </div>
      <EnterpriseButton
        handleClick={() => handleRoleUpdate(selected.role, uuid)}
        text={t("enterprise.view.buttons.update")}
        color={
          currentRole !== selected.role
            ? "bg-primary-200 dark:bg-primary-500"
            : "bg-gray-500 dark:bg-block"
        }
        hoverColor={
          currentRole !== selected.role
            ? "hover:dark:bg-primary-200/80 hover:bg-primary-500/80"
            : "hover:dark:bg-block/80 hover:bg-gray-500/80"
        }
        disabled={currentRole === selected.role}
      />
    </div>
  );
};
