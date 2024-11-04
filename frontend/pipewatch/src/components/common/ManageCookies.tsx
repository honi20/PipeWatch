import {
  Description,
  Dialog,
  DialogPanel,
  DialogTitle,
} from "@headlessui/react";

type ManageCookiesProps = {
  isOpen: boolean;
  onClose: () => void;
};

export const ManageCookies = ({ isOpen, onClose }: ManageCookiesProps) => {
  //   const [isOpen, setIsOpen] = useState(false);

  return (
    <Dialog open={isOpen} onClose={onClose} className="relative z-50">
      <div className="fixed inset-0 flex items-center justify-center w-screen p-4">
        <DialogPanel className="max-w-lg p-12 space-y-4 bg-white border">
          <DialogTitle className="font-bold">Manage Cookies</DialogTitle>
          <Description>
            We use our own cookies so that we can show you this website and
            understand how you use them to improve the services we offer.
          </Description>
          <div className="flex justify-center gap-4">
            <button onClick={onClose}>Accept All</button>
            <button onClick={onClose}>Deny All</button>
          </div>
        </DialogPanel>
      </div>
    </Dialog>
  );
};
