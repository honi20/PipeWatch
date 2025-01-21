import { SyncLoader } from "react-spinners";

export const Loading = () => {
  return (
    <div className="flex flex-col items-center justify-center w-full min-h-[800px] text-[24px] ">
      <p>Loading...</p>
      <SyncLoader
        color={"gray"}
        size={12}
        aria-label="Loading Spinner"
        data-testid="loader"
        className="my-[20px]"
      />
    </div>
  );
};
