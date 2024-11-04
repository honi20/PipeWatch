import {
  Description,
  Dialog,
  DialogPanel,
  DialogTitle,
} from "@headlessui/react";
import { IconButton } from "@components/common/IconButton";
import { useTranslation } from "react-i18next";

type ManageCookiesProps = {
  isOpen: boolean;
  onClose: () => void;
};

export const ManageCookies = ({ isOpen, onClose }: ManageCookiesProps) => {
  //   const [isOpen, setIsOpen] = useState(false);
  const { t } = useTranslation();

  return (
    <Dialog open={isOpen} onClose={onClose} className="relative z-50">
      <div className="fixed inset-0 flex items-center justify-center w-screen p-4 bg-black/80">
        <DialogPanel className="max-w-lg p-8 space-y-4 bg-white border rounded-[8px]">
          <DialogTitle className="font-bold text-[24px] ">
            {t("footer.manageCookies.title")}
          </DialogTitle>
          <Description className="text-[18px]">
            {t("footer.manageCookies.description")}
          </Description>
          <div className="flex justify-center gap-4">
            <IconButton
              handleClick={onClose}
              text={t("footer.manageCookies.buttonDeny")}
              color={"bg-gray-500"}
              hoverColor={"hover:bg-gray-500/80"}
            />
            <IconButton
              handleClick={onClose}
              text={t("footer.manageCookies.buttonAccept")}
              color={"bg-primary-500"}
              hoverColor={"hover:bg-primary-500/80"}
            />
          </div>
        </DialogPanel>
      </div>
    </Dialog>
  );
};
