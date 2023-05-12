import React from 'react'
import ReactDOM from 'react-dom/client'
import Login from './components/login/Login.jsx'
import {ChakraProvider, Text} from "@chakra-ui/react"
import { createStandaloneToast } from '@chakra-ui/react'
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import AuthProvider from "./components/context/AuthContext.jsx"
import ProtectedRoute from "./components/shared/ProtectedRoute.jsx"
import SignUp from "./components/signup/SignUp.jsx"
import './index.css'
import Customer from "./Customer.jsx";
import Home from "./Home.jsx";


const { ToastContainer } = createStandaloneToast()

const router = createBrowserRouter([
    {
        path: "/",
        element: <Login />
    },
    {
        path: "dashboard",
        element: <ProtectedRoute><Home /></ProtectedRoute>
    },
    {
        path: "/signup",
        element: <SignUp />
    },
    {
        path: "dashboard/customers",
        element:  <ProtectedRoute>
            <Customer />
        </ProtectedRoute>
    }
])

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
      <ChakraProvider>
          <AuthProvider>
              <RouterProvider router={router} />
          </AuthProvider>
          <ToastContainer />
      </ChakraProvider>
  </React.StrictMode>,
)
