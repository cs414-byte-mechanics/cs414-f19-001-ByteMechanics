import React, { Component } from 'react';
import { Switch, Route, withRouter } from "react-router-dom";
import Home from './Home.js'
import GameBoard from './GameBoard.js'
import Form from './Form.js'
import Header from './Header'
import {attemptLogin, attemptLogout, registerUser} from '../commObjects'
import './styles/Game.scss'

class Game extends Component {
    constructor(props){
        super(props);
      
        this.updateSearchResult = this.updateSearchResult.bind(this);
      
        this.state = {
          logIn: {},
          player1: ["player1"],
          player2: ["player2"],
          next_turn: ["player1"],
          match_id: ["1"],
          games: [[]],
          status: "active",
          searchResult: [],
          searchGames: [],
          invitationLists: {
            sentToNames: [],
            sentToTimes: [],
            receivedFromNames: [],
            receivedFromTimes: []
          }
        }

        this.connection = null;
        this.sendObject = this.sendObject.bind(this);
        this.logOut = this.logOut.bind(this);
        this.isLoggedIn = this.isLoggedIn.bind(this);
        this.updateSearchResult = this.updateSearchResult.bind(this);
        this.setCookie = this.setCookie.bind(this);
        this.checkCookie = this.checkCookie.bind(this);
        this.updateSearchGamesResult = this.updateSearchGamesResult.bind(this);
        this.setInvitationsLists = this.setInvitationsLists.bind(this);
    }

    componentDidMount() {
        this.checkCookie();
        this.connection = new WebSocket('ws://localhost:4444');
        this.connection.onopen = function () {
          console.log('Connected!');
        }

        this.connection.onerror = function (error) {
          console.log('WebSocket Error: ' + error);
          alert("Cannot reach server!")
        }
    
        this.connection.onmessage = function (e) {
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
            case "invitationSentStatus": this.updateInvitationSentStatus(update); break;
            case "error" : alert(update.statusMessage); if(update.errorCode===105) window.location.href = "/"; break;
            case "updateBoard": this.updateBoard(update); break;
            case "loginSuccess": case "logoutSuccess": this.updateLogin(update); break;
            case "searchResult": this.updateSearchResult(update); break;
            case "endMatch" : this.endMatch(update); break;
            case "searchGamesResult": this.updateSearchGamesResult(update); break;
            case "sendUserInvsLists" : this.setInvitationsLists(update);break
            case "inviteAcceptStatus": break;
            default: break;
        }
    }

    updateSearchGamesResult(update) {
        this.setState({searchGames: update.searchResults});
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
        state.match_id = [update.matchID];
        state.player1 = [update.playerOneName];
        state.player2 = [update.playerTwoName];
        state.status = update.endCondition;
        console.log("status "+update.endCondition);
        this.setState(state);
    }

    endMatch(update){
        let state = this.state;
        state.games = [update.updatedBoard];
        state.status = update.endCondition;
        this.setState(state);
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

    sendObject(obj){
        let self = this;
        function waitForSocketConnection(callback){
            setTimeout(
                function () {
                    if (self.connection.readyState === 1) {
                        if (callback != null){
                            obj.userName = self.state.logIn.userName;
                            callback();
                        }
                    } else {

                        waitForSocketConnection(callback);
                    }

                }, 5); // wait 5 milisecond for the connection...
        }
        waitForSocketConnection(() => self.connection.send(JSON.stringify(obj)));
    }

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

    setInvitationsLists(update) {
      let newInvitationLists = {
        sentToNames: update.sentToNames,
        sentToTimes: update.sentToTimes,
        receivedFromNames: update.receivedFromNames,
        receivedFromTimes: update.receivedFromTimes
      };
      this.setState({invitationLists: newInvitationLists});
    }

    render(){

        return(
            <div id="Application">
                <Header sendObject={this.sendObject} userName={this.state.logIn.userName} isLoggedIn={this.isLoggedIn} logOut={this.logOut}/>
                <div id="page_container">
                    <Switch location={window.location}>
                        <Route
                            exact
                            path="/"
                            render={(props) => <Home
                                                    isLoggedIn={this.isLoggedIn}
                                                    userName={this.state.logIn.userName}
                                                    logOut={this.logOut}
                                                    sendObject={this.sendObject}
                                                    searchResult={this.state.searchResult}
                                                    showInvitePlayer={this.state.showInvitePlayer}
                                                    invitationSentStatus={this.state.invitationSentStatus}
                                                    showInvitationSentStatus={this.state.showInvitationSentStatus}
                                                    invitationLists={this.state.invitationLists}
                                                    gamesResults={this.state.searchGames}
                                                    showRefreshInvs={this.state.showRefreshInvs}
                                                />}
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
                            path="/game/:matchID"
                            render={(props) => <GameBoard {...props} game={this.state.games[0]} playerName = {this.state.next_turn[0]}
                                                            player1={this.state.player1[0]} player2={this.state.player2[0]}
                                                            match_id={this.state.match_id[0]} status={this.state.status}
                                                            userName={this.state.logIn.userName} send={this.sendObject}/>}
                        />
                    </Switch>
                </div>
            </div>
        );
    }
}

export default withRouter(Game);

