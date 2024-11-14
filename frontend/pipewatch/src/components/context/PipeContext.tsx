// PipeContext.tsx
import React, { createContext, useContext, useState, ReactNode } from "react";

type PipeContextType = {
  selectedPipeId: number | null;
  setSelectedPipeId: React.Dispatch<React.SetStateAction<number | null>>;
};

const PipeContext = createContext<PipeContextType | undefined>(undefined);

export const PipeProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [selectedPipeId, setSelectedPipeId] = useState<number | null>(null);
  return (
    <PipeContext.Provider value={{ selectedPipeId, setSelectedPipeId }}>
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
