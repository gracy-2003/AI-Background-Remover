import { useEffect, useContext, useState } from "react";
import axios from "axios";
import { useAuth, useUser } from "@clerk/clerk-react";
import { AppContext } from "../context/AppContext";
import { toast } from "react-hot-toast";

const UserSyncHandler = () => {

  const { isLoaded, isSignedIn, getToken } = useAuth();
  const { user } = useUser();

  const { backendUrl, loadUserCredits } = useContext(AppContext);

  const [synced, setSynced] = useState(false);

  useEffect(() => {

    const syncUser = async () => {

      if (!isLoaded || !isSignedIn || !user || synced) return;

      try {

        const token = await getToken();

        const userData = {
          clerkId: user.id,
          email: user.primaryEmailAddress?.emailAddress,
          firstName: user.firstName,
          lastName: user.lastName,
          photoUrl: user.imageUrl
        };

        await axios.post(
          `${backendUrl}/api/users`,
          userData,
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );

        console.log("User synced to backend");

        setSynced(true);

        // load credits AFTER sync
        await loadUserCredits();

      } catch (error) {

        console.error("User sync failed:", error);
        toast.error("User sync failed");

      }

    };

    syncUser();

  }, [isLoaded, isSignedIn, user]);

  return null;
};

export default UserSyncHandler;