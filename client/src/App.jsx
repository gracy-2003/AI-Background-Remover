import Menubar from "./components/Menubar";
import Footer from "./components/Footer";
import Home from "./pages/Home";
import Result from "./pages/Result";
import UserSyncHandler from "./components/UserSyncHandler";

import { Routes, Route } from "react-router-dom";
import { Toaster } from "react-hot-toast";

import { SignedIn, SignedOut, RedirectToSignIn } from "@clerk/clerk-react";

const App = () => {
  return (
    <div>

      <UserSyncHandler />

      <Menubar />

      <Toaster />

      <Routes>

        <Route path="/" element={<Home />} />

        <Route
          path="/result"
          element={
            <>
              <SignedIn>
                <Result />
              </SignedIn>

              <SignedOut>
                <RedirectToSignIn />
              </SignedOut>
            </>
          }
        />

      </Routes>

      <Footer />

    </div>
  );
};

export default App;