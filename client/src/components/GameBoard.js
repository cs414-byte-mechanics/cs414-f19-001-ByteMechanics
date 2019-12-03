import React, { Component } from 'react';
import { Button } from 'reactstrap';

import './styles/GameBoard.scss';

class GameBoard extends Component {
    constructor(props){
        super(props);
        this.state = {
            selectionType: "pieceID",
            pieceLocation: -1,
            requestMove: {
                "communicationType": "requestMoves",
                "communicationVersion": 1,
                "matchID": this.props.match.params.matchID,
                "playerName": this.props.playerName,
                "pieceID" : "",
                "desiredMoves": [],
                "playerOneName": this.props.player1,
                "playerTwoName": this.props.player2
              }
        }
        this.select = this.select.bind(this);
        this.changeSelectionType = this.changeSelectionType.bind(this);
        this.clearSelection = this.clearSelection.bind(this);
        this.confirmSelection = this.confirmSelection.bind(this);
        this.selectPiece = this.selectPiece.bind(this);
        this.selectMove = this.selectMove.bind(this);
        this.getGameStatus = this.getGameStatus.bind(this);
    }

    getGameStatus(){
        setTimeout(() => {
            let searchObject = {
                communicationType: "requestGameLoad",
                communicationVersion: this.state.requestMove.communicationVersion,
                matchID: this.props.match.params.matchID
            };
            this.props.send(searchObject);
        }, 1500)
        }

    componentDidMount(){
        this.getGameStatus();
    }

    //send move to server for validation and completion
    confirmSelection() {
        let moveObject = {
            communicationType: this.state.requestMove.communicationType,
            communicationVersion: this.state.requestMove.communicationVersion,
            matchID: this.props.match.params.matchID,
            pieceID: this.state.requestMove.pieceID,
            desiredMoves: this.state.requestMove.desiredMoves,
            userName: this.props.userName,
            playerName: this.props.playerName,
            playerOneName: this.props.player1,
            playerTwoName: this.props.player2
        };
        this.props.send(moveObject);
        this.clearSelection();
    }

    //clear currently selected piece and move
    clearSelection() {
        let state = this.state;
        state.requestMove.pieceID = "";
        state.requestMove.desiredMoves = [];
        state.selectionType = "pieceID"
        state.pieceLocation = -1;
        this.setState(state);
    }

    //update selection mode (between piece selection and move selection)
    changeSelectionType(type) {
        this.setState({selectionType: type})
    }

    //select a piece or move
    select(i,j){
        //only allow any item to be selected if game is active - i.e. not in "win" or "quit" state
        if (this.props.status != "active") return;

        //prevent selection of current space
        if(this.state.pieceLocation===(i * 10 + j)) return;
        
        //make selection
        if(this.state.selectionType==='pieceID') this.selectPiece(i,j);
        else this.selectMove(i,j);

        //after selecting a piece, change modes to select the piece's move
        if(this.state.selectionType==="pieceID" && this.props.game[i][j].length>0) this.changeSelectionType("desiredMoves");
    }
    selectPiece(i, j) {
        let state = this.state;

        let encodeLocation = i * 10 + j;
        //get alphanumeric representation of piece
        state.requestMove.pieceID = this.props.game[i][j];
        state.pieceLocation = encodeLocation;
        state.requestMove.desiredMoves.push(encodeLocation);
        this.setState(state);
    }
    selectMove(i, j) {
        //prevent pieces other than monkey from taking multiple moves
        let piece = this.state.pieceLocation;
        let col = piece % 10;
        let row = (piece - col)/10;

        if(this.props.game[row][col]!=='M' && this.state.requestMove['desiredMoves'].length>=2) return;

        let state = this.state;
        let encodeLocation = i * 10 + j;
        state.requestMove[state.selectionType].push(encodeLocation);
        this.setState(state);
    }

    //basic logical determinations for pieces
    isInRiver(i, j) {
        return (i===3) ? "river" : "";
    }
    isInCastle(i, j) {
        return (2<=j && j<=4 && !this.isInRiver(i,j)) ? "castle" : ""
    }
    isSelected(i, j) {
        let pieceLoc = this.state.pieceLocation;
        let moves = this.state.requestMove.desiredMoves;
        //let numMoves = moves.length;
        let moveLoc = i * 10 + j;
        return `${(pieceLoc===moveLoc) ? "selected" : ""}${(moves.find((move)=>move===moveLoc) && (pieceLoc!==moveLoc)) ? "move" : ""}`
    }
    player(i, j) {
        return this.props.game[i][j] !== this.props.game[i][j].toUpperCase() ? "one" : "two"
    }

    //generate classes for styling
    generatePieceClasses(i, j){
        return `piece ${this.state.selectionType} ${this.isInRiver(i,j)}${this.isInCastle(i,j)} ${this.isSelected(i,j)} ${this.player(i, j)}`
    }

    generateMessage(){
        switch(this.props.status) {
            case "won": return " won!  Game over!";
            case "quit": return " quit.  Game halted!";
            default : return "'s move";
        }
    }

    //generate icon & message to indicate which player's move is expected next
    generateGameStatusMessage(nextPlayer, thisPlayer){
        if (nextPlayer === thisPlayer){
            if (thisPlayer === this.props.player1){
                return <p><>&#127810;</>{this.props.player1}{this.generateMessage()}<>&#x1f334;</></p>
            }
            else return <p><>&#127809;</>{this.props.player2}{this.generateMessage()}<>&#x1f333;</></p>
        }
        return;
    }

    generateClearMoveButton(){
        if (this.props.status === "active"){
            return <Button onClick={this.clearSelection}>Clear Move</Button>
        }
    }

    generateConfirmMoveButton(){
        if (this.props.status === "active"){
            return <Button onClick={this.confirmSelection}>Confirm Move</Button>
        }
    }

    render(){
        //map piece ids to unicode icons
        const pieces = {
            'P': <>&#127809;</>,
            'L': <>&#x1f981;</>,
            'G': <>&#x1f992;</>,
            'M': <>&#128018;</>,
            'E': <>&#x1F418;</>,
            'C': <>&#x1f40a;</>,
            'Z': <>&#129427;</>,
            'p': <>&#127810;</>,
            'l': <>&#x1f981;</>,
            'g': <>&#x1f992;</>,
            'm': <>&#x1F435;</>,
            'e': <>&#x1F418;</>,
            'c': <>&#x1f40a;</>,
            'z': <>&#129427;</>,

            /*Added for superPawn*/
            's': <>&#x1f334;</>,
            'S': <>&#x1f333;</>
        }

        //generate board from game state array
        let board =
            <div className="board">
                <div className="player1">
                    {this.generateGameStatusMessage(this.props.playerName, this.props.player1)}
                </div>
                {this.props.game.map((row, i)=>
                    <div className="board_row">{row.map((piece, j)=>
                        <div className={this.generatePieceClasses(i,j)} onClick={()=>this.select(i, j)}>{pieces[piece]}</div>
                        )}
                    </div>
                )}
                <div className="player2">
                    {this.generateGameStatusMessage(this.props.playerName, this.props.player2)}
                </div>
            </div>
        
        //action buttons
        let buttons =
            <div className="buttons">
                {this.generateClearMoveButton()}
                {this.generateConfirmMoveButton()}
            </div>

        return (
            <div id="gameboard">
                {board}
                {buttons}
            </div>
        );
    }
}

export default GameBoard;

