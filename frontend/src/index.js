import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import LocalStorageConstant from "./constants/LocalStorageConstant";

window.Buffer = window.Buffer || require("buffer").Buffer;

let loginState = localStorage.getItem(LocalStorageConstant.PRIVATE_KEY);


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <App isLogin={loginState}/>
    </React.StrictMode>
);