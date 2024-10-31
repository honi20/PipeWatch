import { CompletedCard } from "@src/components/common/CompletedCard";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { Button } from "@headlessui/react";
export const CompletedContact = () => {
  const { t } = useTranslation();
  return (
    <div
      className="flex flex-col gap-[20px] items-center justify-center"
      style={{ height: "calc(100vh - 220px)" }}
    >
      <CompletedCard />
      <Link className="" to="/">
        <Button className="px-4 py-2 bg-white border-[1px] border-black border-solid rounded-lg text-s text-black hover:text-primary-200">
          {t("verification.toHome")}
        </Button>
      </Link>
    </div>
  );
};
