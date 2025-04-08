import { EqualApproximately } from "lucide-react";
import { apiClient } from "../CreateApi";

export const loginServie = {
  login: async (email, password) => {
    try {
      return await apiClient.post("/api/login", {
        email,
        password,
      });
    } catch (error) {
      console.error(error);
    }
  },
  signup: async (name, email, password) => {
    try {
      return await apiClient.post("/api/signUp", {
        name,
        email,
        password,
      });
    } catch (error) {
      console.error(error);
    }
  },
};
