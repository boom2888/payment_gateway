import { lazy } from "react";
import { createBrowserRouter } from "react-router-dom";
const Home = lazy(() => import("@/views/Home"));
const Swap = lazy(() => import("@/views/Swap"));
const Pay = lazy(() => import("@/views/Pay"));
const TdsAuth = lazy(() => import("@/views/TdsAuth"));
const Success = lazy(() => import("@/views/Success"));
const Error = lazy(() => import("@/views/Error"));

const router = createBrowserRouter([
  {
    path: "/order",
    element: <Home />,
  },
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/pay",
    element: <Pay />,
  },
  {
    path: "/swap",
    element: <Swap />,
  },

  {
    path: "/auth",
    element: <TdsAuth />,
  },
  {
    path: "/success",
    element: <Success />,
  },
  {
    path: "/error",
    element: <Error />,
  },
]);

export default router;
