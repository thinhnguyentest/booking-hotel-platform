import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App";
import { DarkModeContextProvider } from "./context/darkModeContext";
import { Provider } from 'react-redux'
import store from "./redux/store";
import 'mdb-react-ui-kit/dist/css/mdb.min.css';

const root = createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <DarkModeContextProvider>
        <App />
      </DarkModeContextProvider>
    </Provider>
  </React.StrictMode>
);
