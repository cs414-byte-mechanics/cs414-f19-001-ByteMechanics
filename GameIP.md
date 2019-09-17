# Game Interchange Protocol (GameIP)
## Version 1

This file will specify the standard format for data exchanged and stored between systems of the software. These formats will be used in the following system interactions:

* Clients to Server
* Server to Clients

The format for all data to be exchanged will be that of JSON objects (https://www.json.org/). This means that whenever clients and
the server exchange information a JSON object will be sent with the required information. 

This document will be updated throughout the duration of the project as needed. Objects will be removed and added over the course of development.

## requestMove

This object will primarily be sent by clients when communicating with the server. It will be used to communicate to the server that a player wishes to move a piece to a certain position on the board. It will ultimately be up to the server to decide whether this move is valid or not. 

```javascript
{
  "communicationType": "requestMove",
  "communicationVersion": 1,
  "matchID": "",
  "playerName": "",
  "pieceID": 3,
  "desiredPosition: []
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `matchID` is the identifier of the match that is object is regarding.
* `playerName` is a string and will be the name of the player who is requesting the move to be made.
* `pieceID` is an int and will be the ID of the piece that the player wishes to move. This is not simply a name because many pieces on the board have a duplicate so a unique identifier is needed for each piece.
* `desiredPosition` will be a 1D array with the coordinates of the square or position on the board that the player would like to move the piece to.

## errorInvalidMove

This object will primarily be sent by the server when communicating with clients. It will be sent by the server to a client after the server has determined that a requested move from the client is invalid. This will then indicate that the client must pick a different spot to move to.

```javascript
{
  "communicationType": "errorInvalidMove",
  "communicationVersion": 1,
  "matchID": "",
  "playerName": "",
  "pieceID": 3,
  "desiredPosition": [],
  "whoseTurn": "",
  "errorMessage": "The move requested by the player cannot be made."
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `matchID` is the identifier of the match that is object is regarding.
* `playerName` is a string and will be the name of the player who requested the invalid move.
* `pieceID` is an int and will be the ID of the piece that the player wished to move to an invalid space
* `desiredPosition` will be a 1D array with the coordinates of the invalid square or position on the board that the player wanted to move the piece to.
* `whoseTurn` is a string and is the name of the player who is allowed to make the next move. Because the previous move was invalid, the player who made the last move is try and move again.
* `errorMessage` is a string and will contain a message stating that the requested was was invalid and could not be made. This string
could also be altered to include the specifics of why the move was invalid such as it being out of bounds, conflicting with the rules
of the game, etc..

## updateBoard

This object will be sent by the server when communicating with clients. It will be sent after the server has determined 
that a requested move by a client was in fact valid and so the move was made and the state of the board was updated. The server will
send the updated board to the clients so that they can display it so the players.

```javascript
{
  "communicationType": "updateBoard",
  "communicationVersion": 1,
  "matchID": "",
  "playerName": "",
  "pieceID": 3,
  "newPosition: [],
  "updatedBoard": [][],
  "whoseTurn": "",
  "successMessage": "The player's move was valid and the board has been updated"
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `matchID` is the identifier of the match that is object is regarding.
* `playerName` is a string and will be the name of the player who made the valid move that updated the board.
* `pieceID` is an int and will be the ID of the piece that the player moved.
* `newPosition` is a 1D array with the coordinates of the the position to which the piece was moved.
* `updatedBoard` is a 2D array with the information containing the new state of the game board after the piece was moved.
* `whoseTurn` is a string and is the name of the player who is allowed to make the next move. In this case it will be the name of the player who did not make the most recent move.
* `successMessage` is a string containing a message stating that the requested move was valid and the board has been updated.

## registerUser

This object will be sent by clients when communicating with the server. It will be sent by a client to the server whenever a user is registering to the site. The server will take the data in this object and use it to create a new entry with the user's informatino in the database.

```javascript
{
  "communicationType": "registerUser",
  "communicationVersion": 1,
  "userName": "",
  "userPassword": "",
  "userEmail": ""
}
```
* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `userName` is a string and is the display name that the person creating the account would like to use.
* `userPassword` is a string and is the password that the person creating the account would like to use to secure their account.
* `userEmail` is a string and is the email address of the person creating the account.

## errorInvalidRegistration

This object will be sent by the server when communicating with a client. It will be sent by the server to a client when the client submits invalid information when trying to register a new account. It is up to the server to determine what information is invalid. Invalid information could be something like a duplicate user name, invalid email address, etc...

```javascript
{
  "communicationType": "errorInvalidRegistration",
  "communicationVersion": 1,
  "userName": "",
  "userPassword": "",
  "userEmail": "",
  "errorMessage": "Information that was used in the attempted registration is invalid"
}
```
* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `userName` is a string and is the display name that the person attempting to create the account would like to use.
* `userPassword` is a string and is the password that the person attempting to create the account would like to use.
* `userEmail` is a string and is the email address of the person attemping to create the account would like to use.

## registrationSuccess

This object will be sent by the server when communicating with a client. It will be sent by the server to a client when the client submits valid information when trying to register a new account and so a new account has been created for the user. It is up to the server to determine if the information was valid in the account creation.

```javascript
{
  "communicationType": "registrationSuccess",
  "communicationVersion": 1,
  "userName": "",
  "userEmail": "",
  "message": "User account has been successfully created."
}
```
* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `userName` is a string and is the new display name of the person who created the account.
* `userEmail` is a string and is the email address of the person who has created a new account.
* `message` is a string with a message stating that an account has been successfully created for the user.

## requestBeginNewMatch

This object will be sent by clients to the server. It will be sent to the server by a client whenever a user is creating a new match. This will tell the server to allocate resources for the match and begin the processes required for the clients to play it.

```javascipt
{
  "communicationType": "requestBeginNewMatch",
  "communicationVersion": 1,
  "playerOneName": "",
  "playerTwoName": ""
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `playerOneName` is a string and is the username of the player who created the match by inviting playerTwo. They are the opponent of playerTwo.
* `playerTwoName` is a string and is the username of the player who was invited to the match by playerOne. They are the opponent of playerOne.

## beginNewMatch

This object will be sent by the server to clients. This object will be sent by the server to the two clients who recently began a new match. It represents the beginning of a new match. 

```javascript
{
  "communicationType": beginNewMatch",
  "communicationVersion": 1,
  "matchID": "",
  "initialBoard": [][],
  "whoseTurn": "",
  "matchBeginTime": ""
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `matchID` is a string and is the identifier of the new match that has just been created. 
* `initialBoard` is a 2D array and is the initial state of the board at the start of a new match.
* `whoseTurn` is a string and is the name of the player who is allowed to make the next move. At the start of the match this will be the player who created the match unless therwise stated in the rules.
* `matchBeginTime` is a string and contains the time that the match was created. 

## invitation

This object will both be sent from clients to the server and from the server to the clients. A client who wishes to start a new match with a player will send the the invitation to the server. The server will then pass the invitation on to the addressed client who will then either accept or reject the invitation.
  
```javascript
{
  "communicationType": "invitation",
  "communicationVersion": 1,
  "invitationFrom": "",
  "invitationTo": "",
  "invitationTime": ""
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `invitationFrom` is a string and is the name of the player who is sending the invitation.
* `invitationTo` is a string and is the name of the player who is receiving the invitation.
* `invitationTime` is a string containing the time at which the invitation was created.

## invitationResponse

This object will be sent from clients to the server. It will be sent to notify the server of a client's response to an invitation to a new match. The server will then pass this object on to the client that sent the original invitation.

```javascript
{
  "communicationType": "invitationResponse",
  "communicationVersion": 1,
  "invitationFrom": "",
  "invitationTo": "",
  "invitationAccepted": true
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `invitationFrom` is a string and is the name of the player who sent the invitation.
* `invitationTo` is a string and is the name of the player who received the invitation.
* `invitationAccepted` is a boolean and indicates whether the player specified in `invitationTo` accepted or declined the invitation. `true` indicates that the client accepted the invitation while `false` indicates the client rejected the invitation.

## quitMatch

This object will be sent from clients to the server. It will be sent to notify the server that the client wishes to forfeit the match and leave, thus ending the game. The client who sends this request will automatically lose the match and the opponent will win. 

```javasctipt
{
  "communicationType": "quitMatch",
  "communicationVersion": 1,
  "matchID": "",
  "playerQuitting": ""
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `matchID` is a string and is the identifier of the match which the client wishes to leave. 
* `playerQuitting` is the username of the player who is quitting the match.

## endMatch

This object will be sent from the server to the clients. It will be sent when either a client's last move has won the match or a player has quit the match. It will also notify the clients of the results.

```javasctipt
{
  "communicationType": "endMatch",
  "communicationVersion": 1,
  "matchID": "",
  "endCondition": ["won", "quit"],
  "winnerName": "",
  "loserName": "",
  "matchEndTime": ""
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `matchID` is a string and is the identifier of the match that has ended.
* `endCondition` can be one of two string values. `won` indicates that a player has won the match by making a final move which won the game. `quit` indicates that the game has ended because one of the players has quit and left the match.
* `winnerName` is a string and is the name of player who has won the match either be making the winning move or remaining in the game after the other player has quit.
* `loserName` is a string and is the name of the player who has lost the match either by the other player making the winning move or by quitting the match.
* `matchEndTime` is a string and contains the time that the match was ended. 

## unregisterUser

This object will be sent by clients when communicating with the server. It will be sent by a client to the server whenever a user would like to unregister from the site. The server will then remove the user's data associated with their account from the database.

```javascript
{
  "communicationType": "unregisterUser",
  "communicationVersion": 1,
  "userName": ""
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `userName` is a string and is the name of the user who would like to unregister from the site.

## attemptLogin

This object will be sent by clients when communicating with the server. It will be sent by a client to the server whenever a user is attempting to log into their account.

```javascript
{
  "communicationType": "attemptLogin",
  "communicationVersion": 1,
  "userEmail": "",
  "userPassword": ""
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `userEmail` is a string and is the email address of the user who is attempting to log into their account. This will be the primary identifier of the account.
* `userPassword` is a string and is the password of the user attempint to log into their account. It is the primary form of authentication for the user.  

## errorInvalidLogin

This object will be sent by the server when communicating with clients. It will be sent by the server to the client whenever the client attempts to login to their account with an incorrect password or the account does not exist.

```javascript
{
  "communicationType": "errorInvalidLogin",
  "communicationVersion": 1,
  "userEmail": "",
  "errorMessage": ""
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `userEmail` is a string and is the email address that the user has entered when trying to login. 
* `errorMessage` is a string containing the reason that the login attempt has failed. It could either state that the password was incorrect or that the account does not exist. 

## loginSuccess

This object will be sent by the server when communicating with clients. It will be sent by the server when a user has successfully logged in to their account. It will contain the user's account information so that it may be displayed to the user and interacted with.

```javascript
{
  "communicationType": "loginSuccess",
  "communicationVersion": 1,
  "invitations": [{"invitationFrom": "", "invitationTime": ""}, {"invitationFrom": "", "invitationTime": ""}, ...]
  "matchesInProgress": [{"matchID: "", "gameBoard": [][], "opponentName": "", "whoseTurn": "", "matchBeginTime": ""}, {"matchID: "",                             "gameBoard": [][], "opponentName": "", "whoseTurn": "", "matchBeginTime": ""}, ...],
  "matchesCompleted": [{"matchID: "", "opponentName": "", "matchBeginTime": "", "matchWinner": "", "matchEndTime": ""}, {"matchID: "",                          "opponentName": "", "matchBeginTime": "", "matchWinner": "", "matchEndTime": ""}, ...]
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `invitations` is an array of objects. These objects contain information regarding the open invitations sent to this user.
* `matchesInProgress` is an array of objects. These objects contain information regarding matches that the player is currently playing.
* `matchesCompleted` is an array of objects. These objects contain information regarding past matches that have ended. 

# Update History
## Sprint 1
* 9/16/2019 zachklau finished adding first set/version of GameIP objects. 

# Notes
* The intial set of objects is based off the user description of the desired system in P1.pdf. They are meant to represent interactions discussed in this description.