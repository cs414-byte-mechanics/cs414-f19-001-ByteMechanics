import React, { Component } from 'react';
import { Button } from 'reactstrap';

import './styles/GameBoard.scss';

class GameBoard extends Component {
    constructor(props){
        super(props);
        this.state = {
            selectionType: "pieceID",
            // piece: [],
            // move: [],
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
    }
    confirmSelection() {
        this.props.send(this.state.requestMove)
    }
    clearSelection() {
        let state = this.state;
        state.requestMove.pieceID = [];
        state.requestMove.desiredMoves = [];
        state.selectionType = "pieceID"
        this.setState(state);
    }
    changeSelectionType(type) {
        this.setState({selectionType: type})
    }
    select(i,j){
        let state = this.state;
        state.requestMove[state.selectionType] = [i, j];
        this.setState(state, ()=>{console.log(this.state)});
        if(state.selectionType=="pieceID" && this.props.game[i][j].length>0) this.changeSelectionType("desiredMoves");
    }
    isInRiver(i, j) {
        return (i===3) ? "river" : "";
    }

    isInCastle(i, j) {
        return (2<=j && j<=4 && !this.isInRiver(i,j)) ? "castle" : ""
    }
    isSelected(i, j) {
        let piece = this.state.requestMove.pieceID;
        let move = this.state.requestMove.desiredMoves;
        return `${(i===piece[0] && j===piece[1]) ? "selected" : ""}${(i===move[0] && j===move[1]) ? "move" : ""}`
    }
    generatePieceClasses(i, j){
        return `piece ${this.isInRiver(i,j)}${this.isInCastle(i,j)} ${this.isSelected(i,j)}`
    }


    render(){
        let board = 
            <div className="board">
                {this.props.game.map((row, i)=>
                    <div className="row">{row.map((piece, j)=>
                        <div className={this.generatePieceClasses(i,j)} onClick={()=>this.select(i, j)}>{piece}</div>
                        )}
                    </div>
                )}
            </div>
        
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

