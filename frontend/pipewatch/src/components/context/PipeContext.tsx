import React, { createContext, useContext, useState, ReactNode } from "react";

type PipeContextType = {
  selectedPipeId: number | null;
  setSelectedPipeId: React.Dispatch<React.SetStateAction<number | null>>;
  isButtonClicked: boolean;
  setIsButtonClicked: React.Dispatch<React.SetStateAction<boolean>>;
};

const PipeContext = createContext<PipeContextType | undefined>(undefined);

export const PipeProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [selectedPipeId, setSelectedPipeId] = useState<number | null>(null);
  const [isButtonClicked, setIsButtonClicked] = useState<boolean>(false);

  return (
    <PipeContext.Provider
      value={{
        selectedPipeId,
        setSelectedPipeId,
        isButtonClicked,
        setIsButtonClicked,
      }}
    >
      {children}
    </PipeContext.Provider>
  );
};

export const usePipe = () => {
  const context = useContext(PipeContext);
  if (!context) {
    throw new Error("usePipe must be used within a PipeProvider");
  }
  return context;
};
