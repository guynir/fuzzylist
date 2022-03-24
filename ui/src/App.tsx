import React from 'react';
import './App.css';
import {WelcomePage} from "./views/MainPage";
import {Route, Routes} from "react-router-dom";
import {ListPage} from "./views/ListPage";

function App() {
    return (
        <div className="App">
            <Routes>
                <Route path="/" element={<WelcomePage/>}/>
                <Route path="/lists/:listKey" element={<ListPage />}/>
            </Routes>
        </div>
    );
}

export default App;
