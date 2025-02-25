import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { Tab, TabGroup, TabList, TabPanel, TabPanels } from "@headlessui/react";

// import PhotoCameraIcon from "@mui/icons-material/PhotoCamera";
import DriveFolderUploadIcon from "@mui/icons-material/DriveFolderUpload";
import KeyboardIcon from "@mui/icons-material/Keyboard";
import ViewInArIcon from "@mui/icons-material/ViewInAr";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";

import { useNavigate, useLocation } from "react-router-dom";
// import { TakePhoto } from "@components/pipeGenerator/TakePhoto";
import { UploadModel } from "@components/pipeGenerator/UploadModel";
import { InputData } from "@components/pipeGenerator/InputData";
import { Rendering } from "@components/pipeGenerator/Rendering";
import { Completed } from "@components/pipeGenerator/Completed";

import { AccessBlocked } from "@src/components/common/AccessBlocked";
import { Loading } from "@src/components/common/Loading";

import useRole from "@src/hooks/useRole";

export const PipeGenerator = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const location = useLocation();

  const { role, isLoading } = useRole();

  useEffect(() => {
    if (location.pathname === "/pipe-generator") {
      navigate("/pipe-generator/upload-model", { replace: true });
    }
  }, [location.pathname, navigate]);

  const menus = [
    // {
    //   key: "takePhoto",
    //   icon: PhotoCameraIcon,
    //   component: <TakePhoto />,
    //   path: "/pipe-generator/take-photo",
    // },
    {
      key: "uploadModel",
      icon: DriveFolderUploadIcon,
      component: <UploadModel />,
      path: "/pipe-generator/upload-model",
    },
    {
      key: "inputData",
      icon: KeyboardIcon,
      component: <InputData />,
      path: "/pipe-generator/input-data",
    },
    {
      key: "rendering",
      icon: ViewInArIcon,
      component: <Rendering />,
      path: "/pipe-generator/rendering/:modelId",
    },
    {
      key: "completed",
      icon: CheckCircleIcon,
      component: <Completed />,
      path: "/pipe-generator/completed",
    },
  ];

  const activeIndex = menus.findIndex((menu) => {
    const menuPath = menu.path.replace(/:modelId/, "");
    return location.pathname.startsWith(menuPath);
  });

  // const handleTabClick = (path: string) => {
  //   const modelId = "1";
  //   const finalPath = path.replace(":modelId", modelId);
  //   navigate(finalPath);
  // };

  if (role === null && isLoading) {
    return <Loading />;
  }

  if (role !== "ENTERPRISE" && role !== "ADMIN") {
    <AccessBlocked />;
  }

  return (
    <>
      <div className="mx-6">
        <h2 className="font-bold text-[40px]">{t("pipeGenerator.title")}</h2>
        <p className="text-[24px]">{t("pipeGenerator.description")}</p>
      </div>

      <div className="flex items-center justify-center h-[640px] my-10 ">
        <TabGroup
          selectedIndex={activeIndex}
          // onChange={(index) => handleTabClick(menus[index].path)}
          className="flex h-full rounded-lg shadow-lg shadow-gray-500 dark:shadow-none"
        >
          <TabList className="flex flex-col w-[300px] bg-whiteBox p-8 rounded-s-lg">
            <div className="py-3 text-[16px] text-primary-500">
              {t("pipeGenerator.procedure")}
            </div>
            <div className="h-[1px] bg-primary-500 my-4" />
            {menus.map((menu, index) => {
              const IconComponent = menu.icon;
              return (
                <Tab
                  key={menu.key}
                  id={index.toString()}
                  className="flex gap-2 items-center p-5 text-[16px] text-left text-gray-500 cursor-default focus:outline-none rounded-xl bg-whiteBox data-[selected]:bg-primary-500 data-[selected]:text-white"
                  // className="flex gap-2 items-center p-5 text-[16px] text-left text-gray-500 cursor-default focus:outline-none rounded-xl bg-block data-[selected]:bg-white data-[selected]:text-gray-800"
                >
                  <IconComponent className="w-5 h-5" />
                  {t(`pipeGenerator.menus.${menu.key}`)}
                </Tab>
              );
            })}
          </TabList>
          <TabPanels className="w-[800px] h-full bg-white text-black rounded-e-lg">
            {menus.map((menu, index) => {
              return (
                <TabPanel
                  className="h-full"
                  key={menu.key}
                  id={index.toString()}
                >
                  {menu.component}
                </TabPanel>
              );
            })}
          </TabPanels>
        </TabGroup>
      </div>
    </>
  );
};
