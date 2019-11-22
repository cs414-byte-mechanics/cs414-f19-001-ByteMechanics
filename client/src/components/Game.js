import React, { Component } from 'react';
import { Container, Col, Row } from 'reactstrap';
import { Switch, Route, withRouter, Link } from "react-router-dom";
import Home from './Home.js'
import GameBoard from './GameBoard.js'
import Form from './Form.js'
import Header from './Header'
import {attemptLogin, attemptLogout, registerUser} from '../commObjects'
import './styles/Game.scss'
import Invitations from "./Invitations";

class Game extends Component {
    constructor(props){
        super(props);
      
        this.updateSearchResult = this.updateSearchResult.bind(this);
      
        this.state = {
          logIn: {},
          player1: ["ajeske"],
          player2: ["arictor"],
          next_turn: ["ajeske"],
          match_id: ["1"],
          games: [
            [
              ["g", "m", "e", "l", "e", "c", "z"],
              ["p", "p", "p", "p", "p", "p", "p"],
              ["", "", "", "", "", "", ""],
              ["", "", "", "", "", "", ""],
              ["", "", "", "", "", "", ""],
              ["P", "P", "P", "P", "P", "P", "P"],
              ["G", "M", "E", "L", "E", "C", "Z"]
            ]
          ],
          searchResult: []
        }

        this.connection = null;
        this.sendObject = this.sendObject.bind(this);
        this.logOut = this.logOut.bind(this);
        this.isLoggedIn = this.isLoggedIn.bind(this);
        this.updateSearchResult = this.updateSearchResult.bind(this);
        this.setCookie = this.setCookie.bind(this);
        this.checkCookie = this.checkCookie.bind(this);
    }

    componentDidMount() {
        this.checkCookie();
        this.connection = new WebSocket('ws://localhost:4444');
        this.connection.onopen = function () {
          console.log('Connected!');
        }.bind(this);

        this.connection.onerror = function (error) {
          console.log('WebSocket Error: ' + error);
          alert("Cannot reach server!")
        }.bind(this);
    
        this.connection.onmessage = function (e) {
          console.log('Server: ' + e.data);
          let update = JSON.parse(e.data);
          this.handleUpdate(update)
        }.bind(this);
    
        this.connection.onclose = function (e) {
          console.log('Connection lost');
          //THIS FUNCTION IS CALLED ON REFRESH IN FIREFOX BUT NOT CHROME
        };
    }

    handleUpdate(update) {
        switch(update.communicationType) {
            case "registrationSuccess": this.updateLogin(update); break;
            case "invitationSentStatus": case "error" : alert(update.statusMessage);break
            case "updateBoard": this.updateBoard(update); break;
            case "loginSuccess": case "logoutSuccess": this.updateLogin(update); break;
            case "searchResult": this.updateSearchResult(update); break;
            case "invitationSentStatus": this.updateInvitationSentStatus(update); break;
        }
    }

    updateLogin(update) {
        let new_login_state = update.communicationType === "logoutSuccess" ? {} : update;
        this.setState({logIn: new_login_state});
        this.setCookie(new_login_state);
        window.location.href = "/";
    }

    updateBoard(update){
        let state = this.state;
        state.games = [update.updatedBoard];
        state.next_turn = [update.whoseTurn];
        this.setState(state);
        console.log("game board " + this.state.games);
        console.log("next_turn " + this.state.next_turn);
    }

    setCookie(logIn, exdays=0) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = (exdays===0) ? "" : "expires="+d.toUTCString() + ";path=/";
        document.cookie = "logIn=" + JSON.stringify(logIn) + ";" + expires;
    }

    getCookie(cname) {
        let name = cname + "=";
        let ca = document.cookie.split(';');
        for(let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

    checkCookie() {
        let logIn_str = this.getCookie("logIn");
        if (logIn_str !== "") {
            let logIn = JSON.parse(logIn_str);
            this.setState({logIn: logIn});
        }
    }

    sendObject(obj){ console.log(JSON.stringify(obj)); this.connection.send(JSON.stringify(obj)); }

    logOut() { this.sendObject(attemptLogout); }

    isLoggedIn(){ return JSON.stringify(this.state.logIn)!=="{}"; }

    updateSearchResult(update) {
      this.setState({searchResult: update.searchResults});
    }

    updateInvitationSentStatus(update) {
      this.setState({invitationSentStatus: update});
      this.setState({showInvitationSentStatus: true});
      this.setState({showInvitePlayer: true});
    }


    render(){

        return(
            <div id="Application">
                <Header sendObject={this.sendObject} isLoggedIn={this.isLoggedIn} logOut={this.logOut}/>
                <div id="page_container">
                    <Switch location={window.location}>
                        <Route
                          exact
                          path="/invitations"
                          render={(props) => <Invitations isLoggedIn={this.isLoggedIn}
                                                          userName={this.state.logIn.userName}
                                                          sendObject={this.sendObject}
                                                          searchResult={this.state.searchResult}
                                                          showInvitePlayer={this.state.showInvitePlayer}
                                                          invitationSentStatus={this.state.invitationSentStatus}
                                                          showInvitationSentStatus={this.state.showInvitationSentStatus}

                          />}
                        />
                        <Route
                            exact
                            path="/"
                            render={(props) => <Home isLoggedIn={this.isLoggedIn} userName={this.state.logIn.userName}/>}
                        />
                        <Route
                            path="/register"
                            render={(props) => <Form {...props} title="Register an account" action={registerUser} isLoggedIn={this.isLoggedIn} sendToServer={this.sendObject}/>}
                        />
                        <Route
                            path="/login"
                            render={(props) => <Form {...props} title="Log in" action={attemptLogin} isLoggedIn={this.isLoggedIn} sendToServer={this.sendObject}/>}
                        />
                        <Route
                            path="/game"
                            render={(props) => <GameBoard game={this.state.games[0]} playerName = {this.state.next_turn[0]}
                                                            player1={this.state.player1[0]} player2={this.state.player2[0]}
                                                            match_id={this.state.match_id[0]} send={this.sendObject}/>}
                        />
                    </Switch>
                </div>
            </div>
        );
    }
}

export default withRouter(Game);

