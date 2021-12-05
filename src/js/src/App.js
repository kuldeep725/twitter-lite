// React core components
import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";

// User defined components
import Home from "./Component/Home";
import Login from "./Component/Login";

// Css
import "./App.css";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/home" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Navigate to="/home" />} />
      </Routes>
    </Router>
  );
}

export default App;
