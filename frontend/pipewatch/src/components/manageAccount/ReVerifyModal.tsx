import {
  Description,
  Dialog,
  DialogPanel,
  DialogTitle,
} from "@headlessui/react";
import { IconButton } from "@components/common/IconButton";
import { useTranslation, Trans } from "react-i18next";
import { useState } from "react";

import { getApiClient } from "@src/stores/apiClient";

import { useUserStore } from "@src/stores/userStore";

type ManageCookiesProps = {
  isOpen: boolean;
  onClose: () => void;
};

export const ReVerifyModal = ({ isOpen, onClose }: ManageCookiesProps) => {
  const { t } = useTranslation();

  const [isRequested, setIsRequested] = useState(false);

  const { enterpriseName } = useUserStore();

  const apiClient = getApiClient();
  const roleRequest = async () => {
    try {
      const res = await apiClient.patch(`/api/users/role-request`);
      console.log("result", res);
      setIsRequested(true);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <Dialog open={isOpen} onClose={onClose} className="relative z-50">
      <div className="fixed inset-0 flex items-center justify-center w-screen p-4 bg-black/80">
        <DialogPanel className="max-w-lg p-8 space-y-4 bg-white border rounded-[8px]">
          {isRequested ? (
            <>
              <DialogTitle className="text-[20px] my-[10px]">
                {t(
                  "manageAccount.reverificationRequest.reverificationComplete"
                )}
              </DialogTitle>
              <div className="flex justify-center">
                <IconButton
                  handleClick={onClose}
                  text={t("manageAccount.reverificationRequest.close")}
                  color={"bg-gray-500"}
                  hoverColor={"hover:bg-gray-500/80"}
                />
              </div>
            </>
          ) : (
            <>
              <DialogTitle className="font-bold text-[24px]">
                {t("manageAccount.reverificationRequest.title")}
              </DialogTitle>
              <Description className="text-[18px]">
                <Trans
                  i18nKey="manageAccount.reverificationRequest.reverificationMessage"
                  values={{ enterpriseName }}
                  components={{ b: <b /> }}
                />
              </Description>
              <div className="flex justify-center gap-4">
                <IconButton
                  handleClick={onClose}
                  text={t("manageAccount.reverificationRequest.cancel")}
                  color={"bg-gray-500"}
                  hoverColor={"hover:bg-gray-500/80"}
                />
                <IconButton
                  handleClick={() => roleRequest()}
                  text={t(
                    "manageAccount.reverificationRequest.submitReverification"
                  )}
                  color={"bg-primary-500"}
                  hoverColor={"hover:bg-primary-500/80"}
                />
              </div>
            </>
          )}
        </DialogPanel>
      </div>
    </Dialog>
  );
};
