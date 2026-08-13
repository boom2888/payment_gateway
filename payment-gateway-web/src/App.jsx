import "./App.less";
import { RouterProvider } from "react-router-dom";
import { Elements } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import Loading from "@/components/Loading";
import { Suspense, useEffect, useState } from "react";
import router from "@/router";

const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PUBLIC_KEY);
function App() {
  return (
    <div className="App">
      <Elements stripe={stripePromise}>
        <Suspense fallback={<Loading />}>
          <RouterProvider router={router} />
        </Suspense>
      </Elements>
    </div>
  );
}

export default App;
