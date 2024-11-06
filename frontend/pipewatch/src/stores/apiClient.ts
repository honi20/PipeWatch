import axios from "axios";
const API_URL = import.meta.env.VITE_URL;

export const createApiClient = (accessToken: string | null) => {
  if (!accessToken) {
    console.error("createApiClient: accessToken이 제공되지 않았습니다.");
    throw new Error("Access token is required to create an API client.");
  }
  return axios.create({
    baseURL: API_URL,
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
};

const getAccessToken = () => localStorage.getItem("accessToken");
// const setAccessToken = (token: string) =>
//   localStorage.setItem("accessToken", token);
// const clearAccessToken = () => localStorage.removeItem("accessToken");

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
      originalRequest._retry = true;
      if (error.response?.data.httpStatus === 401 && !originalRequest._retry) {
        try {
          console.log("accessToken 다시");
          //   if (res.status == 200) {
          //     setAccessToken(res.data.body.accessToken);
          //     originalRequest.headers[
          //       "Authorization"
          //     ] = `Bearer ${getAccessToken()}`;
          //     return apiClient(originalRequest);
          //   } else {
          //     return Promise.reject(
          //       new Error("Token reissue failed, logging out.")
          //     );
          //   }
          // } catch (err) {
          //   return Promise.reject(err);
        } catch (err) {
          console.log(err);
        }
      } else {
        console.log(
          `error status: ${error.response.status}, httpStatus: ${error.response.data.httpStatus}`
        );
      }
      if (error.response?.status == 400) {
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
