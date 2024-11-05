import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

import { Button } from "@headlessui/react";
import { motion, AnimatePresence } from "framer-motion";

import HomePipe from "@assets/images/home_pipe.png";
import Upload1 from "@assets/images/home/upload_1.png";
import Upload2 from "@assets/images/home/upload_2.png";
import Upload3 from "@assets/images/home/upload_3.png";
import Manage1 from "@assets/images/home/manage_1.png";
import Manage2 from "@assets/images/home/manage_2.png";
import Manage3 from "@assets/images/home/manage_3.png";

const tabs = [
  { icon: "🍅", label: "전체 파이프 모델", url: Manage1 },
  { icon: "🥬", label: "개별 파이프 선택", url: Manage2 },
  { icon: "🧀", label: "메모 작성", url: Manage3 },
];

export const Home = () => {
  const { t } = useTranslation();
  const [selectedTab, setSelectedTab] = useState(tabs[0]);

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
    <div className="flex flex-col items-center justify-center">
      <h1 className="font-bold text-[80px]">{t("home.greeting1")}</h1>
      <h1 className="font-bold text-[80px]">{t("home.greeting2")}</h1>
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

      <div className="flex my-[100px] w-full justify-center gap-[100px] ">
        <div className="text-left">
          <div className="font-bold text-[50px]">가장 편리한 데이터 업로드</div>
          <div className="text-[24px]">파이프 데이터를 업로드하고</div>
          <div className="text-[24px]">자동화된 세그멘테이션 기술을</div>
          <div className="text-[24px]">경험해보세요.</div>
        </div>

        <div className="wrap">
          <motion.div
            variants={item}
            initial="hidden"
            whileInView="whileInView"
          >
            <img src={Upload1} width={"400px"} className="my-[10px]" />
          </motion.div>
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
            <img src={Upload2} width={"400px"} className="my-[10px]" />
          </motion.div>
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
            <img src={Upload3} width={"400px"} className="my-[10px]" />
          </motion.div>
        </div>
      </div>

      <div className="flex flex-col my-[100px]">
        <div className="text-left">
          <div className="font-bold text-[50px]">건물별 파이프 통합 관리</div>
          <div className="text-[24px]">
            각 건물, 층, 구간 별 파이프를 한 곳에서 관리하세요.
          </div>
          <div className="text-[24px]">
            파이프의 속성을 실시간으로 수정하고, 메모를 남길 수도 있습니다.
          </div>
        </div>
        <div className="flex flex-col items-center">
          <nav>
            <ul className="flex">
              {tabs.map((item) => (
                <li
                  key={item.label}
                  className={item === selectedTab ? "selected" : ""}
                  onClick={() => setSelectedTab(item)}
                >
                  {`${item.icon} ${item.label}`}
                  {/* {item === selectedTab ? <motion.div /> : null} */}
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
                  <img src={selectedTab.url} width={"300px"} />
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
