import React, { Component } from 'react';
import { Button } from 'reactstrap';

import './styles/GameBoard.scss';

class GameBoard extends Component {
    constructor(props){
        super(props);
        this.state = {
            selectionType: "pieceID",
            requestMove: {
                "communicationType": "requestMove",
                "communicationVersion": 1,
                "matchID": "",
                "playerName": "",
                "pieceID": [],
                "desiredMoves": []
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
        this.props.send(this.state.requestMove)
    }

    //clear currently selected piece and move
    clearSelection() {
        let state = this.state;
        state.requestMove.pieceID = [];
        state.requestMove.desiredMoves = [];
        state.selectionType = "pieceID"
        this.setState(state);
    }

    //update selection mode (between piece selection and move selection)
    changeSelectionType(type) {
        this.setState({selectionType: type})
    }

    //select a piece or move
    select(i,j){
        //prevent selection of current space 
        if(this.state.requestMove.pieceID[0]===i && this.state.requestMove.pieceID[1]===j) return;
        
        //make selection
        if(this.state.selectionType==='pieceID') this.selectPiece(i,j);
        else this.selectMove(i,j);

        //after selecting a piece, change modes to select the piece's move
        if(this.state.selectionType=="pieceID" && this.props.game[i][j].length>0) this.changeSelectionType("desiredMoves");
    }
    selectPiece(i, j) {
        let state = this.state;
        state.requestMove['pieceID'] = [i, j];
        this.setState(state);
    }
    selectMove(i, j) {
        //prevent pieces other than monkey from taking multiple moves
        let piece = this.state.requestMove.pieceID;
        if(this.props.game[piece[0]][piece[1]]!=='M' && this.state.requestMove['desiredMoves'].length>=1) return; 

        let state = this.state;
        state.requestMove[state.selectionType].push([i, j]);
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
        let piece = this.state.requestMove.pieceID;
        let moves = this.state.requestMove.desiredMoves;
        return `${(i===piece[0] && j===piece[1]) ? "selected" : ""}${(moves.find((move)=>move[0]==i &&move[1]==j)) ? "move" : ""}`
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
            'Z': <>&#129427;</>  
        }

        //generate board from game state array
        let board = 
            <div className="board">
                {this.props.game.map((row, i)=>
                    <div className="row">{row.map((piece, j)=>
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
            <div className="App">
                {board}
                {buttons}
            </div>
        );
    }
}

export default GameBoard;

