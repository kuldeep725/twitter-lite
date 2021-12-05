const env = require("dotenv").config({path: "../.env.local"});
console.log({env});

// const URLS = {
//     LOGIN_API_URL:
// }

const login = async (username, password) => {
    // return axios.post(process.env.LOGIN_API_URL, { username, password });
    console.log("Calling login api...");
    // return axios.post("http://localhost:8090/api/users/login", { username, password });
    return fetch("http://localhost:8090/api/users/login", {
        method: "POST",
        // mode: 'cors', // no-cors, *cors, same-origin
        // cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        // credentials: 'same-origin', // include, *same-origin, omit
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password })
    })
        .then(response => response.json());
    // .then(data => console.log(data));
};

export {login};
