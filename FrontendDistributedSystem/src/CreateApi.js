import axios from "axios";
const createApiClient = (baseURL = "http://localhost:8080") => {
  const client = axios.create({
    baseURL,
    auth: {
      username: "admin",
      password: "admin123",
    },
    withCredentials: true,
  });

  // Add request interceptor for error handling
  client.interceptors.request.use(
    (config) => config,
    (error) => Promise.reject(error)
  );

  // Add response interceptor for error handling
  client.interceptors.response.use(
    (response) => response,
    (error) => {
      console.error("API Error:", error.response?.data || error.message);
      return Promise.reject(error);
    }
  );

  return client;
};

export const apiClient = createApiClient("https://distributedstorage.site/");
