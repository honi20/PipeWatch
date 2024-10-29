import { useTranslation } from "react-i18next";
import { Tab, TabGroup, TabList, TabPanel, TabPanels } from "@headlessui/react";
import {
  CameraIcon,
  ArrowUpTrayIcon,
  PencilIcon,
  CubeIcon,
  CheckCircleIcon,
} from "@heroicons/react/24/solid";

export const PipeGenerator = () => {
  const { t } = useTranslation();
  const menus = [
    { key: "menu1", icon: CameraIcon },
    { key: "menu2", icon: ArrowUpTrayIcon },
    { key: "menu3", icon: PencilIcon },
    { key: "menu4", icon: CubeIcon },
    { key: "menu5", icon: CheckCircleIcon },
  ];

  return (
    <div className="h-full">
      <div className="mx-6">
        <h2 className="font-bold text-[40px]">{t("pipeGenerator.title")}</h2>
        <p className="text-[24px]">{t("pipeGenerator.description")}</p>
      </div>

      <div className="flex items-center justify-center my-10 ">
        <TabGroup className="flex h-[640px] rounded-lg shadow-lg shadow-gray-500 dark:shadow-none">
          <TabList className="flex flex-col w-[300px] bg-block p-8 rounded-s-lg">
            <div className="py-3 text-[16px] text-white ">모델 생성 절차</div>
            <div className="h-[1px] bg-white my-4" />
            {menus.map((menu, index) => {
              const IconComponent = menu.icon;
              return (
                <Tab
                  id={index.toString()}
                  className="flex gap-2 items-center p-5 text-[16px] text-left text-gray-500 focus:outline-none rounded-xl bg-block data-[selected]:bg-white data-[selected]:text-gray-800 data-[hover]:bg-white/5 data-[selected]:data-[hover]:bg-gray-200"
                >
                  <IconComponent className="w-5 h-5" />
                  {t(`pipeGenerator.menus.${menu.key}`)}
                </Tab>
              );
            })}
          </TabList>
          <TabPanels className="w-[800px] bg-white text-black rounded-e-lg">
            {menus.map((menu, index) => {
              return <TabPanel id={index.toString()}>{menu.key}</TabPanel>;
            })}
          </TabPanels>
        </TabGroup>
      </div>
    </div>
  );
};
