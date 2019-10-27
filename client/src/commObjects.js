let registerUser = {
    "communicationType": "registerUser",
    "communicationVersion": 1,
    "userName": "",
    "userEmail": "",
    "userPassword": ""
}
let attemptLogin = {
    "communicationType": "attemptLogin",
    "communicationVersion": 1,
    "userEmail": "",
    "userPassword": ""
}
let attemptLogout = {
    "communicationType": "attemptLogout",
    "communicationVersion": 1,
    "userEmail": ""
}

export {
         registerUser,
         attemptLogin,
         attemptLogout
       };