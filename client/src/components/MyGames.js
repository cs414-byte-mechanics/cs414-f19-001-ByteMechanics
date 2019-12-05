import React from 'react';
import Dashboard from "./Dashboard";
import './styles/MyGames.scss'
import {Form, FormGroup, Label, Input, Button, InputGroup, InputGroupButtonDropdown, Row, Col} from 'reactstrap';
import { FaTrashAlt as Trash, FaPlay as Play} from 'react-icons/fa'
import { Switch, Route, withRouter, Link } from "react-router-dom";
import GameBoard from './GameBoard.js'
import Confirm from './Confirm.js'

class MyGames extends React.Component {

    constructor(props) {
        super(props);
        console.log(props);

        this.submitSearchString = this.submitSearchString.bind(this);
        this.updateSearchString = this.updateSearchString.bind(this);
        this.renderSearchInputs = this.renderSearchInputs.bind(this);
        this.listenForEnter = this.listenForEnter.bind(this);
        this.goToGamePage = this.goToGamePage.bind(this);
        this.abandonGame = this.abandonGame.bind(this);

        this.state = {
          searchString: '',
          matchID: 1
        };
    }

    getGames(){
        setTimeout(() => {
            let searchObject = {
                communicationType: "searchGames",
                userName: this.props.userName,
                playerTwoName: ""
            };
            this.props.sendObject(searchObject);
        }, 1500)
    }

    componentDidMount(){
        this.getGames();
    }


    renderSearchButton() {
        return (
          <Button onClick={this.submitSearchString}>Search</Button>
        );
    }

    listenForEnter(event) {
        if (event.keyCode === 13)
            this.submitSearchString(event);
    }

    renderSearchInputs() {
        return (
            <div id="search_input">
                <Input type="search" placeholder="Filter on opponent..." onChange={this.updateSearchString} onKeyDown={this.listenForEnter}/>
                <Button onClick={this.submitSearchString}>Search</Button>
            </div>
        );
    }

    updateSearchString(event) {
      this.setState({
        searchString: event.target.value
      });
    }

    submitSearchString() {
        let searchObject = {
          communicationType: "searchGames",
          userName: this.props.userName,
          playerTwoName: this.state.searchString
        };
        this.props.sendObject(searchObject);
    }

    renderTableHeader() {
        let header = ["Opponent", "Started"]
        return header.map((title) => {
            return <th>{title}</th>
        })
    }

    goToGamePage(){
        window.location.href = "/game";
    }

    playGame(id){
        this.setState({matchID: id},this.goToGamePage());
    }
    
    abandonGame(matchID){
      let abandonObject = {
        communicationType: "quitMatch",
        matchID: matchID,
        playerQuitting: this.props.userName
      }
     this.props.sendObject(abandonObject)
     alert("Game sucessfully abandoned")
    }

    renderTableData(games){
        if(this.props.gamesResults.length===0) return (<p>No matches found</p>);

        return this.props.gamesResults.map((data) => {
        let data_array = data.split(',');
        return (
            <div className="result">
                <p><b>{data_array[1]}</b></p><p>Last updated {data_array[2]}</p>
                <div className="game_buttons">
                    <Play title="Play" className="game_buttons" onClick={e => this.playGame(data_array[0])}/>
                    <Confirm title="Abandon" className="game_buttons" onClick={e => this.abandonGame(data_array[0])} button=<Trash className="game_buttons"/> reason="Abandon Game"/>
                </div>
            </div>);
        })
    }

    render () {
        return (
            <div id="mygames">
                <div id="wrapper">
                    <div id="games">
                        <div id="subtitle">Games in Progress</div>
                        {this.renderSearchInputs()}
                        <div id="game_data">
                            {this.renderTableData(this.props.gamesResults)}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
export default MyGames;
