# Game Interchange Protocol (GameIP)
## Version 1

This file will specify the standard format for data exchanged and stored between systems of the software. These formats will be used in the 
following system interactions:

* Clients and server
* Server and Database (if a database is used)
* Server and files (if files are used)

The format for all data to be exchanged will be that of JSON objects (https://www.json.org/). This means that whenever the client and
server exchange information or the server stores and reads information from a database or text file, a JSON object will be sent with 
the required information. 

This document will be updated throughout the duration of the project as needed. 

## requestMove

This object will primarily be sent by clients when communicating with the server. It will be used to communicate to the server that a player wishes to move a piece to a certain position on the board. It will ultimately be up to the server to decide whether this move is valid or not. 

```javascript
{
  "communicationType": "requestMove",
  "communicationVersion": 1,
  "playerName": "",
  "pieceID": 3,
  "desiredPosition: []
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `playerName` is a string and will be the name of the player who is requesting the move to be made.
* `pieceID` is an int and will be the ID of the piece that the player wishes to move. This is not simply a name because many pieces on the board have a duplicate so a unique identifier is needed for each piece.
* `desiredPosition` will be a 2D array with the coordinates of the square or position on the board that the player would like to move the piece to.

## errorInvalidMove

This object will primarily be sent by the server when communicating with clients. It will be sent by the server to a client after the server has determined that a requested move from the client is invalid. This will then indicate that the client must pick a different spot to move to.

```javascript
{
  "communicationType": "errorInvalidMove",
  "communicationVersion": 1,
  "playerName": "",
  "pieceID": 3,
  "desiredPosition": [],
  "errorMessage": "The move requested by the player cannot be made."
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `playerName` is a string and will be the name of the player who requested the invalid move.
* `pieceID` is an int and will be the ID of the piece that the player wished to move to an invalid space
* `desiredPosition` will be a 2D array with the coordinates of the invalid square or position on the board that the player wanted to move the piece to.
* `errorMessage` is a string and will contain a message stating that the requested was was invalid and could not be made. This string
could also be altered to include the specifics of why the move was invalid such as it being out of bounds, conflicting with the rules
of the game, etc..

## updateBoard

This object will primarily be sent by the server when communicating with clients. It will be sent after the server has determined 
that a requested move by a client was in fact valid and so the move was made and the state of the board was updated. The server will
send the updated board to the clients so that they can display it so the players.

```javascript
{
  "communicationType": "updateBoard",
  "communicationVersion": 1,
  "playerName": "",
  "pieceID": 3,
  "newPosition: [],
  "updatedBoard": [],
  "successMessage": "The player's move was valid and the board has been updated"
}
```

* `communicationType` is a string and will specify what the type of the JSON object is and so what information it should contain.
* `communicationVersion` is an int and will specify the version of this document that the object's structure is based on.
* `playerName` is a string and will be the name of the player who made the valid move that updated the board.
* `pieceID` is an int and will be the ID of the piece that the player moved.
* `newPosition` is a 2D array with the coordinates of the the position to which the piece was moved.
* `updatedBoard` is a 2D array with the information containing the new state of the game board after the piece was moved.
* `successMessage` is a string containing a message stating that the requested move was valid and the board has been updated.





