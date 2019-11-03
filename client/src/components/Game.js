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
        this.state = {
          logIn: {},
          games: [
            [
              ["G", "M", "E", "L", "E", "C", "Z"],
              ["P", "P", "P", "P", "P", "P", "P"],
              ["", "", "", "", "", "", ""],
              ["", "", "", "", "", "", ""],
              ["", "", "", "", "", "", ""],
              ["P", "P", "P", "P", "P", "P", "P"],
              ["G", "M", "E", "L", "E", "C", "Z"]
            ]
          ],
          searchResult: {
            userName: "",
            userFound: false
          },
          showInvitePlayer: false,
          showInvitationSentStatus: false,
          invitationSent: false,
          invitationsFrom: "",
          invitationTimes: ""
        }

        this.connection = null;
        this.sendObject = this.sendObject.bind(this);
        this.logOut = this.logOut.bind(this);
        this.isLoggedIn = this.isLoggedIn.bind(this);
        this.updateSearchResult = this.updateSearchResult.bind(this);
        this.updateInvitationsAndTimes = this.updateInvitationsAndTimes.bind(this);
        this.sendInvitationsRequest = this.sendInvitationsRequest.bind(this);

        this.setCookie = this.setCookie.bind(this);
        this.checkCookie = this.checkCookie.bind(this);
    }

    componentDidMount() {
        this.checkCookie();
        this.connection = new WebSocket('ws://localhost:4444');
        this.connection.onopen = function () {
          console.log('Connected!');
          this.sendInvitationsRequest();
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
            case "errorInvalidRegistration": alert(update.errorMessage); break;
            case "loginSuccess": this.updateLogin(update); break;
            case "errorInvalidLogin": alert(update.errorMessage); break;
            case "logoutSuccess": this.updateLogin(update); break;
            case "logoutFailure": alert(update.errorMessage); break;
            case "searchResult": this.updateSearchResult(update); break;
            case "invitationSentStatus": this.updateInvitationSentStatus(update); break;
            case "loadInvitations": this.updateInvitationsAndTimes(update); break;
        }
    }

    updateLogin(update) {
        let new_login_state = update.communicationType === "logoutSuccess" ? {} : update;
        this.setState({logIn: new_login_state}, ()=>{console.log("success")});
        this.setCookie(new_login_state);
        window.location.href = "/";
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

    sendObject(obj){ this.connection.send(JSON.stringify(obj)); }

    logOut() { this.sendObject(attemptLogout) }

    isLoggedIn(){ return JSON.stringify(this.state.logIn)!=="{}"; }

    updateSearchResult(update) {
      this.setState({searchResult: update});
      this.setState({showInvitePlayer: true});
      this.setState({showInvitationSentStatus: false});
    }

    updateInvitationSentStatus(update) {
      this.setState({invitationSent: update.invitationSent});
      this.setState({showInvitationSentStatus: true});
    }

    updateInvitationsAndTimes(update) {
      this.state.invitationsFrom = update.invitationsFrom;
      this.state.invitationTimes = update.invitationTimes;
    }

    sendInvitationsRequest() {
      let requestObject = {
        communicationType: "requestInvitations",
        userName: this.state.logIn.userName
      };
      this.sendObject(requestObject);
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
                                                          invitationSent={this.state.invitationSent}
                                                          showInvitationSentStatus={this.state.showInvitationSentStatus}
                                                          invitationsFrom={this.state.invitationsFrom}
                                                          invitationTimes={this.state.invitationTimes}
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
                            render={(props) => <GameBoard game={this.state.games[0]} send={this.sendObject}/>}
                        />
                    </Switch>
                </div>
            </div>
        );
    }
}

export default withRouter(Game);

