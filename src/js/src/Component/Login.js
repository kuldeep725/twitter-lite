import React, {useState} from "react";
import {setUserSession} from "../Utils/Common";
import {login} from "../Utils/Rest";
import {useNavigate} from "react-router-dom";

const Login = () => {
    const [loading, setLoading] = useState(false);
    const usernameInput = useFormInput("");
    const passwordInput = useFormInput("");
    const [error, setError] = useState(null);
    let navigate = useNavigate();

    const handleLogin = async () => {
        console.log("handleLogin called...");
        setError(null);
        setLoading(true);
        try {
            const response = await login(usernameInput.value, passwordInput.value);
            setLoading(false);
            console.log({response});
            if (!response.error) {
                setUserSession(response.token, response.user);
                navigate("/home");
            } else {
                if ([400, 401].includes(response.status)) setError(response.message);
                else setError("Something went wrong. Please try again later.");
            }

        } catch (error) {
            console.log({error});
            setLoading(false);
            // if ([400, 401].includes(error.response.status))
            //   setError(error.response.message);
            // else setError("Something went wrong. Please try again later.");
        }
    }

    return (
        <div>
            <h1>Login</h1>
            <form>
                <div>
                    <label htmlFor="username">Username</label>
                    <input type="text" {...usernameInput} id="username" name="username" autoComplete="new-password"/>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        {...passwordInput}
                        id="password"
                        name="password"
                        autoComplete="new-password"
                    />
                </div>
                <div>
                    {error && (
                        <div>
                            <small style={{color: "red"}}>{error}</small>
                        </div>
                    )}
                </div>
                <div>
                    <input
                        type="submit"
                        value={loading ? "Loading..." : "Log in"}
                        disabled={loading}
                        onClick={handleLogin}
                    />
                </div>
            </form>
        </div>
    );
}

const useFormInput = (initalValue) => {
    const [value, setValue] = useState(initalValue);
    return {
        value,
        onChange: (e) => setValue(e.target.value),
    };
}

export default Login;
