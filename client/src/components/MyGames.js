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

    renderText() {
        return (
          <div id="title">
              View your games in progress and select one to play!
          </div>
        );
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
          <div>
              <div>
                <div id="subtitle">Search your active games</div>
                <div id="search_input">
                    <Input type="search" placeholder="Filter on opponent..." onChange={this.updateSearchString} onKeyDown={this.listenForEnter}/>
                    <Button onClick={this.submitSearchString}>Search</Button>
                </div>
              </div>

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
        console.log("match ID " + this.state.matchID);
//        window.location.href = "/game/"+this.state.matchID;
    }

    playGame(id){
        console.log("id " + id);
//        window.location.href = "/game/"+id;
        window.open(`/game/${id}`);
//        this.setState({matchID: id},this.goToGamePage());
//        this.setState({matchID: id});
        console.log(" after set match ID " + this.state.matchID);
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

//return <tr><td><a className="nav-link" href="/game">{data_array[0]}</a></td><td>{data_array[1]}</td><td>{data_array[2]}</td></tr>
//            return <tr><td><div onClick={e => this.playGame(data_array[0])}>{data_array[0]}</div></td>
//                        <td>{data_array[1]}</td><td>{data_array[2]}</td></tr>

//                     <Trash onClick={e => this.abandonGame(data_array[0])}/></td></tr>
//                    <td><Play href={"/game/"+data_array[0]} onClick={e => this.playGame(data_array[0])}/></td>

    renderTableData(games){
        console.log(games);
        return this.props.gamesResults.map((data) => {
            let data_array = data.split(',');
            return <tr>
                    <td>{data_array[1]}</td><td>{data_array[2]}</td>
                    <td><Play href={"/game/"+data_array[0]} onClick={e => this.playGame(data_array[0])}/></td>
                    <td><Confirm onClick={e => this.abandonGame(data_array[0])} button=<Trash/> reason="abandon"/></td></tr>

        })
    }

    render () {
        return (
            <div id="mygames">
                {this.renderText()}
                <div id="viewsearch">
                    <div id="games">
                        <div id="subtitle">Games in Progress</div>
                        <div id="game_data">
                        <table id='activeGames'>
                            <tbody>
                                <tr>{this.renderTableHeader()}</tr>
                                {this.renderTableData(this.props.gamesResults)}
                            </tbody>
                        </table>
                        </div>
                    </div>
                    <div id="search">
                        {this.renderSearchInputs()}
                    </div>
                </div>
            </div>
        );
    }
}
export default MyGames;
