import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";
import { ChevronDownIcon } from "@heroicons/react/20/solid";
import clsx from "clsx";
import { useState } from "react";

const roles = [
  { id: 1, role: "Admin" },
  { id: 2, role: "Staff" },
];

export const RoleListbox = () => {
  const [selected, setSelected] = useState(roles[1]);

  return (
    <div className="w-20">
      <Listbox value={selected} onChange={setSelected}>
        <ListboxButton
          className={clsx(
            "relative block w-full rounded-lg bg-white dark:bg-white/5 py-1.5 pr-8 pl-3 text-left text-sm/6 text-black dark:text-white",
            "focus:outline-none data-[focus]:outline-2 data-[focus]:-outline-offset-2 data-[focus]:outline-white/25"
          )}
        >
          {selected && selected.role}
          <ChevronDownIcon
            className="group pointer-events-none absolute top-2.5 right-2.5 size-4 dark:fill-white/60"
            aria-hidden="true"
          />
        </ListboxButton>
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
              <div className="dark:text-white text-sm/6">{role.role}</div>
            </ListboxOption>
          ))}
        </ListboxOptions>
      </Listbox>
    </div>
  );
};
