import { useState, useContext } from "react";
import { Link } from "react-router-dom";
import { assets } from "../assets/assets";
import { Menu, X } from "lucide-react";

import {
  SignedIn,
  SignedOut,
  UserButton,
  useClerk,
  useUser
} from "@clerk/clerk-react";

import { AppContext } from "../context/AppContext";

const Menubar = () => {

  const [menuOpen, setMenuOpen] = useState(false);

  const { openSignIn, openSignUp } = useClerk();
  const { user } = useUser();

  const { credit } = useContext(AppContext);

  const openRegister = () => {
    openSignUp({});
  };

  const openLogin = () => {
    openSignIn({});
  };

  return (
    <nav className="relative bg-white px-8 py-4 flex justify-between items-center">

      {/* Logo */}
      <Link to="/" className="flex items-center space-x-2">
        <img
          src={assets.logo}
          alt="logo"
          className="h-8 w-8 object-contain"
        />
        <span className="text-2xl font-semibold text-indigo-700">
          remove.<span className="text-gray-400">bg</span>
        </span>
      </Link>

      {/* Desktop */}
      <div className="hidden md:flex items-center space-x-4">

        <SignedOut>

          <button
            className="text-gray-700 hover:text-blue-500 font-medium"
            onClick={openLogin}
          >
            Login
          </button>

          <button
            className="bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium px-4 py-2 rounded-full"
            onClick={openRegister}
          >
            Sign up
          </button>

        </SignedOut>

        <SignedIn>

          <div className="flex items-center gap-2 sm:gap-3">

            {/* Credits */}
            <button className="flex items-center gap-2 bg-blue-100 px-4 sm:px-5 py-1.5 rounded-full">

              <img
                src={assets.credits}
                alt="credits"
                height={24}
                width={24}
              />

              <p className="text-xs sm:text-sm font-medium text-gray-600">
                Credits: {credit}
              </p>

            </button>

            <p className="text-gray-600 max-sm:hidden">
              Hi, {user?.fullName}
            </p>

          </div>

          <UserButton />

        </SignedIn>

      </div>

      {/* Mobile hamburger */}
      <div className="flex md:hidden">
        <button onClick={() => setMenuOpen(!menuOpen)}>
          {menuOpen ? <X size={28} /> : <Menu size={28} />}
        </button>
      </div>

      {/* Mobile menu */}
      {menuOpen && (

        <div className="absolute top-16 right-4 bg-white shadow-md rounded-md flex flex-col space-y-4 p-4 w-48 md:hidden">

          <SignedOut>

            <button
              className="text-gray-700 hover:text-blue-500 font-medium"
              onClick={openLogin}
            >
              Login
            </button>

            <button
              className="bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium px-4 py-2 rounded-full text-center"
              onClick={openRegister}
            >
              Sign up
            </button>

          </SignedOut>

          <SignedIn>

            {/* Credits (Mobile) */}
            <button className="flex items-center gap-2 bg-blue-100 px-4 py-2 rounded-full justify-center">

              <img
                src={assets.credits}
                alt="credits"
                height={24}
                width={24}
              />

              <p className="text-sm font-medium text-gray-600">
                Credits: {credit}
              </p>

            </button>

            <p className="text-center text-gray-600">
              Hi, {user?.fullName}
            </p>

            <div className="flex justify-center">
              <UserButton />
            </div>

          </SignedIn>

        </div>

      )}

    </nav>
  );
};

export default Menubar;