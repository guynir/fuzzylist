import React from 'react';
import './App.css';
import {WelcomePage} from "./views/WelcomePage";
import {Route, Routes} from "react-router-dom";
import {ListPage} from "./views/ListPage";

function App() {
    return (
        <div className="app-container">
            <Routes>
                <Route path="/" element={<WelcomePage/>}/>
                <Route path="/lists/:listKey" element={<ListPage />}/>
            </Routes>
        </div>
    );
}

export default App;
