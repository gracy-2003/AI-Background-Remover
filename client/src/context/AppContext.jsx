import { createContext, useState } from "react";
import { useAuth, useUser, useClerk } from "@clerk/clerk-react";
import axios from "axios";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";

export const AppContext = createContext();

const AppContextProvider = ({ children }) => {

  const backendUrl = import.meta.env.VITE_BACKEND_URL;

  const [credit, setCredit] = useState(0);
  const [image, setImage] = useState(null);
  const [resultImage, setResultImage] = useState(null);

  const { getToken } = useAuth();
  const { isSignedIn } = useUser();
  const { openSignIn } = useClerk();

  const navigate = useNavigate();

  // Load credits
  const loadUserCredits = async () => {

    try {

      const token = await getToken();

      const response = await axios.get(
        `${backendUrl}/api/users/credits`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      if (response.data.success) {
        setCredit(response.data.data.credits);
      }

    } catch (error) {

      console.error("Credits error:", error);

    }

  };

  // Remove background
  const removeBg = async (selectedImage) => {

    try {

      if (!isSignedIn) {
        return openSignIn();
      }

      setImage(selectedImage);
      setResultImage(null);

      navigate("/result");

      const token = await getToken();

      const formData = new FormData();
      formData.append("file", selectedImage);

      const response = await axios.post(
        `${backendUrl}/api/images/remove-background`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data"
          }
        }
      );

      const base64Image = response.data;

      setResultImage(`data:image/png;base64,${base64Image}`);

      setCredit((prev) => prev - 1);

    } catch (error) {

      console.error("Remove BG error:", error);
      toast.error("Background removal failed");

    }

  };

  const value = {
    credit,
    setCredit,
    image,
    setImage,
    resultImage,
    setResultImage,
    backendUrl,
    loadUserCredits,
    removeBg
  };

  return (
    <AppContext.Provider value={value}>
      {children}
    </AppContext.Provider>
  );

};

export default AppContextProvider;