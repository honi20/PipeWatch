import { useState, useEffect } from "react";
import { getApiClient } from "@src/stores/apiClient";

const useRole = () => {
  const [role, setRole] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const fetchRole = async () => {
    try {
      const apiClient = getApiClient();
      const res = await apiClient.get("/api/users/profile");
      const userInfo = res.data.body;
      setRole(userInfo.role);
    } catch (err) {
      console.error("Failed to fetch role:", err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchRole();
  }, []);

  return { role, isLoading };
};

export default useRole;
