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
                "matchID": "",
                "playerName": "",
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
    }

    //send move to server for validation and completion
    confirmSelection() {
        this.props.send(this.state.requestMove);
        this.clearSelection();
    }

    //clear currently selected piece and move
    clearSelection() {
        let state = this.state;
//        state.requestMove.pieceID = [];
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
        //prevent selection of current space 
//        if(this.state.requestMove.pieceID[0]===i && this.state.requestMove.pieceID[1]===j) return;
        //if(this.state.requestMove.pieceID===(i * 10 + j)) return;
        if(this.state.pieceLocation===(i * 10 + j)) return;
        
        //make selection
        if(this.state.selectionType==='pieceID') this.selectPiece(i,j);
        else this.selectMove(i,j);

        //after selecting a piece, change modes to select the piece's move
        if(this.state.selectionType=="pieceID" && this.props.game[i][j].length>0) this.changeSelectionType("desiredMoves");
    }
    selectPiece(i, j) {
        let state = this.state;
//        state.requestMove['pieceID'] = [i, j];

        let encodeLocation = i * 10 + j;
        //get alphanumeric representation of piece
        state.requestMove.pieceID = this.props.game[i][j];
        state.pieceLocation = encodeLocation;
        state.requestMove.desiredMoves.push(encodeLocation);
        this.setState(state);
    }
    selectMove(i, j) {
        //prevent pieces other than monkey from taking multiple moves
        //let piece = this.state.requestMove.pieceID;
        let piece = this.state.pieceLocation;
        let col = piece % 10;
        let row = (piece - col)/10;

//        if(this.props.game[piece[0]][piece[1]]!=='M' && this.state.requestMove['desiredMoves'].length>=1) return;
        console.log(this.props.game);
        console.log(row);
        console.log(col);
        console.log(this.props.game[row][col]);
        //if(this.props.game[row][col]!=='M' && this.state.requestMove['desiredMoves'].length>=1) return;
        if(this.props.game[row][col]!=='M' && this.state.requestMove['desiredMoves'].length>=2) return;

        let state = this.state;
        //state.requestMove[state.selectionType].push([i, j]);
        let encodeLocation = i * 10 + j;
        state.requestMove[state.selectionType].push(encodeLocation);
        console.log(state.requestMove[state.selectionType]);
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
        //let piece = this.state.requestMove.pieceID;
        let pieceLoc = this.state.pieceLocation;
        let moves = this.state.requestMove.desiredMoves;
        let numMoves = moves.length;
        let moveLoc = i * 10 + j;
        //return `${(i===piece[0] && j===piece[1]) ? "selected" : ""}${(moves.find((move)=>move[0]==i &&move[1]==j)) ? "move" : ""}`
        return `${(pieceLoc===moveLoc) ? "selected" : ""}${(moves.find((move)=>move==moveLoc) && (pieceLoc!=moveLoc)) ? "move" : ""}`
    }

    //generate classes for styling
    generatePieceClasses(i, j){
        return `piece ${this.state.selectionType} ${this.isInRiver(i,j)}${this.isInCastle(i,j)} ${this.isSelected(i,j)}`
    }


    render(){
        //map piece ids to unicode icons
        const pieces = {
            'P': <>&#x1f333;</>,
            'L': <>&#x1f981;</>,
            'G': <>&#x1f992;</>,
            'M': <>&#128018;</>,
            'E': <>&#x1F418;</>,
            'C': <>&#x1f40a;</>,
            'Z': <>&#129427;</>,
            'p': <>&#x1f334;</>,
            'l': <>&#x1f981;</>,
            'g': <>&#x1f992;</>,
            'm': <>&#x1F435;</>,
            'e': <>&#x1F418;</>,
            'c': <>&#x1f40a;</>,
            'z': <>&#129427;</>
        }

        //generate board from game state array
        let board = 
            <div className="board">
                {this.props.game.map((row, i)=>
                    <div className="board_row">{row.map((piece, j)=>
                        <div className={this.generatePieceClasses(i,j)} onClick={()=>this.select(i, j)}>{pieces[piece]}</div>
                        )}
                    </div>
                )}
            </div>
        
        //action buttons
        let buttons =
            <div className="buttons">
                <Button onClick={this.clearSelection}>Clear Move</Button>
                <Button onClick={this.confirmSelection}>Confirm Move</Button>
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

