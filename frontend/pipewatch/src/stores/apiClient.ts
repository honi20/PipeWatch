import Config from "@src/constants/Config";
import axios from "axios";

const API_URL = Config.API_URL;
const PRODUCT_LOGIN_URL = Config.PRODUCT_LOGIN_URL;
const LOCAL_LOGIN_URL = Config.LOCAL_LOGIN_URL;

const LOGIN =
  process.env.NODE_ENV === "development" ? LOCAL_LOGIN_URL : PRODUCT_LOGIN_URL;

export const createApiClient = (accessToken: string | null) => {
  if (!accessToken && window.location.href !== LOGIN) {
    console.error("createApiClient: accessToken이 제공되지 않았습니다.");
    window.location.href = LOGIN;
  }

  return axios.create({
    baseURL: API_URL,
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
};

const getAccessToken = () => localStorage.getItem("accessToken");
const setAccessToken = (token: string) =>
  localStorage.setItem("accessToken", token);
const clearAccessToken = () => localStorage.removeItem("accessToken");

export const getApiClient = () => {
  const accessToken: string | null = getAccessToken();
  if (accessToken) {
    console.log("getApiClient: accessToken이 존재합니다.");
  }
  const apiClient = createApiClient(accessToken);

  apiClient.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const originalRequest = error.config;

      if (originalRequest._retry) {
        console.error(
          "Refresh token request failed again, stopping further requests."
        );
        return Promise.reject(new Error("Unauthorized, please log in again."));
      }

      if (error.response?.status === 401) {
        originalRequest._retry = true;
        try {
          console.log("accessToken 만료되었음");
          const newAccessToken = error.response.data.body;
          if (newAccessToken) {
            setAccessToken(newAccessToken);
            originalRequest.headers[
              "Authorization"
            ] = `Bearer ${newAccessToken}`;

            return apiClient(originalRequest);
          } else {
            console.error("새로운 accessToken이 응답에 포함되지 않음");
            clearAccessToken();
            window.location.href = "/account/auth/login";
          }
        } catch (err) {
          console.log(err);
          clearAccessToken();
          window.location.href = "/account/auth/login";
          return Promise.reject(err);
        }
      } else {
        console.log(
          `error status: ${error.response?.status}, httpStatus: ${error.response?.data?.httpStatus}`
        );
      }

      if (error.response?.status === 400) {
        console.error("Bad Request (400):", error.response.data);
        return Promise.reject(
          new Error(
            "Request failed with status 400, stopping further requests."
          )
        );
      }

      return Promise.reject(error);
    }
  );

  return apiClient;
};
