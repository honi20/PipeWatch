import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

import { Button } from "@headlessui/react";
import { motion, AnimatePresence } from "framer-motion";
import DoubleArrowIcon from "@mui/icons-material/DoubleArrow";

import HomePipe from "@assets/images/home_pipe.png";
import Upload1_ko from "@assets/images/home/upload_1_ko.png";
import Upload2_ko from "@assets/images/home/upload_2_ko.png";
import Upload3_ko from "@assets/images/home/upload_3_ko.png";
import Manage1_ko from "@assets/images/home/manage_1_ko.png";
import Manage2_ko from "@assets/images/home/manage_2_ko.png";
import Manage3_ko from "@assets/images/home/manage_3_ko.png";
import Upload1_en from "@assets/images/home/upload_1_en.png";
import Upload2_en from "@assets/images/home/upload_2_en.png";
import Upload3_en from "@assets/images/home/upload_3_en.png";
import Manage1_en from "@assets/images/home/manage_1_en.png";
import Manage2_en from "@assets/images/home/manage_2_en.png";
import Manage3_en from "@assets/images/home/manage_3_en.png";

import { useUserStore } from "@src/stores/userStore";

export const Home = () => {
  const { t, i18n } = useTranslation();

  const isKorean = i18n.language === "ko";
  const currentLanguage = isKorean ? "한국어" : "English";

  const tabs = [
    {
      label: t("home.introduction.tabs.allPipe"),
      url: currentLanguage === "한국어" ? Manage1_ko : Manage1_en,
    },
    {
      label: t("home.introduction.tabs.individualPipe"),
      url: currentLanguage === "한국어" ? Manage2_ko : Manage2_en,
    },
    {
      label: t("home.introduction.tabs.memo"),
      url: currentLanguage === "한국어" ? Manage3_ko : Manage3_en,
    },
  ];

  const [selectedTab, setSelectedTab] = useState(tabs[0]);
  const { isLogin, role } = useUserStore();

  const animation_variants_image1 = {
    first: {
      opacity: 0,
      y: 50,
    },
    whileInView: {
      opacity: 1,
      y: 0,
    },
  };

  const item = {
    hidden: { opacity: 0, y: 50 },
    whileInView: { opacity: 1, y: 0 },
  };

  return (
    <div className="flex flex-col items-center justify-center h-full">
      <div className="flex flex-col items-center justify-center mb-[100px]">
        <h1 className="font-bold text-[80px]">{t("home.greeting1")}</h1>
        <h1 className="font-bold text-[80px]">{t("home.greeting2")}</h1>

        {isLogin ? (
          role === "ADMIN" || role === "ENTERPRISE" ? (
            <div className="my-6">
              <Link className="" to="/pipe-generator">
                <Button className="flex items-center gap-2 px-6 py-3 border-black border-solid border-[1px] rounded-lg text-[24px] bg-transparent dark:bg-white dark:text-black hover:text-primary-500">
                  {t("home.introduction.create")}
                  <DoubleArrowIcon sx={{ fontSize: "30px" }} />
                </Button>
              </Link>
            </div>
          ) : role === "EMPLOYEE" ? (
            <div className="my-6">
              <Link className="" to="/pipe-generator">
                <Button className="flex items-center gap-2 px-6 py-3 border-black border-solid border-[1px] rounded-lg text-[24px] bg-transparent dark:bg-white dark:text-black hover:text-primary-500">
                  {t("home.introduction.viewPage")}
                  <DoubleArrowIcon sx={{ fontSize: "30px" }} />
                </Button>
              </Link>
            </div>
          ) : (
            <div className="my-6"></div>
          )
        ) : (
          <div className="flex gap-2 my-6">
            <Link className="" to="/account/auth/login">
              <Button className="px-6 py-3 border-[1px] bg-white dark:bg-black border-black border-solid rounded-lg text-s dark:border-white hover:text-primary-200">
                {t("header.login")}
              </Button>
            </Link>
            <Link className="" to="/account/auth/sign-up">
              <Button className="px-6 py-3 rounded-lg bg-primary-200 b text-s dark:border-white hover:text-primary-500">
                {t("home.signUp")}
              </Button>
            </Link>
          </div>
        )}

        <motion.div
          variants={animation_variants_image1}
          initial="first"
          whileInView="whileInView"
          viewport={{ once: false }}
          transition={{
            ease: "easeIn",
            duration: 0.7,
          }}
        >
          <img src={HomePipe} width={"1000px"} />
        </motion.div>
      </div>

      <div className="flex my-[100px] justify-center w-full px-[50px] gap-[100px]">
        <div className="text-left">
          <div className="font-bold text-[50px]">
            {t("home.introduction.uploadData.title")}
          </div>
          <div className="text-[24px] whitespace-pre-line">
            {t("home.introduction.uploadData.description")}
          </div>
        </div>

        <div className="wrap">
          <motion.div
            variants={item}
            initial="hidden"
            whileInView="whileInView"
            transition={{
              duration: 0.8,
              delay: 0.5,
              ease: [0, 0.71, 0.2, 1.01],
            }}
          >
            <img
              src={currentLanguage === "한국어" ? Upload1_ko : Upload1_en}
              width={"400px"}
              className="my-[10px]"
            />
          </motion.div>
          <motion.div
            variants={item}
            initial="hidden"
            whileInView="whileInView"
            transition={{
              duration: 0.8,
              delay: 0.6,
              ease: [0, 0.71, 0.2, 1.01],
            }}
          >
            <img
              src={currentLanguage === "한국어" ? Upload2_ko : Upload2_en}
              width={"400px"}
              className="my-[10px]"
            />
          </motion.div>
          <motion.div
            variants={item}
            initial="hidden"
            whileInView="whileInView"
            transition={{
              duration: 0.8,
              delay: 0.7,
              ease: [0, 0.71, 0.2, 1.01],
            }}
          >
            <img
              src={currentLanguage === "한국어" ? Upload3_ko : Upload3_en}
              width={"400px"}
              className="my-[10px]"
            />
          </motion.div>
        </div>
      </div>

      <div className="flex flex-col my-[100px] gap-[50px]">
        <motion.div
          variants={animation_variants_image1}
          initial="first"
          whileInView="whileInView"
          viewport={{ once: false }}
          transition={{
            ease: "easeIn",
            duration: 0.7,
          }}
        >
          <div className="flex flex-col items-center text-center">
            <div className="font-bold text-[50px]">
              {" "}
              {t("home.introduction.managePipes.title")}
            </div>
            <div className="text-[24px] text-center whitespace-pre-line">
              {t("home.introduction.managePipes.description")}
            </div>
          </div>
        </motion.div>
        <div className="flex flex-col items-center">
          <nav>
            <ul className="flex mb-[20px]">
              {tabs.map((item) => (
                <li
                  key={item.label}
                  className={`py-4 px-6 ${
                    item.label === selectedTab.label
                      ? "selected bg-primary-500/80 rounded-lg"
                      : ""
                  }`}
                  onClick={() => setSelectedTab(item)}
                >
                  {item.label}
                </li>
              ))}
            </ul>
          </nav>
          <main>
            <AnimatePresence mode="wait">
              <motion.div
                key={selectedTab ? selectedTab.label : "empty"}
                initial={{ y: 10, opacity: 0 }}
                animate={{ y: 0, opacity: 1 }}
                exit={{ y: -10, opacity: 0 }}
                transition={{ duration: 0.2 }}
              >
                {selectedTab ? (
                  <img
                    src={selectedTab.url}
                    width={"1000px"}
                    className="object-fill rounded-[16px]"
                  />
                ) : (
                  "😋"
                )}
              </motion.div>
            </AnimatePresence>
          </main>
        </div>
      </div>
    </div>
  );
};
