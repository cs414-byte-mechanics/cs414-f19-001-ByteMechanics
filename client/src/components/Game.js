import React, { Component } from 'react';
import { Container, Col, Row } from 'reactstrap';
import { Switch, Route, withRouter, Link } from "react-router-dom";

import Home from './Home.js'
import GameBoard from './GameBoard.js'
import Form from './Form.js'
import Header from './Header'
import {attemptLogin, attemptLogout, registerUser} from '../commObjects'
import './styles/Game.scss'

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
          ]
        }

        this.connection = null;
        this.sendObject = this.sendObject.bind(this);
        this.logOut = this.logOut.bind(this);
        this.isLoggedIn = this.isLoggedIn.bind(this);

        this.setCookie = this.setCookie.bind(this);
        this.checkCookie = this.checkCookie.bind(this);
    }

    componentDidMount() {
        this.checkCookie();
        this.connection = new WebSocket('ws://localhost:4444');
        console.log(typeof this.connection)
        
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
          switch(update.communicationType) {
              case "registrationSuccess":
                  this.setState({logIn: update}, ()=>{console.log("Registration success")});
                  this.setCookie(update);
                  window.location.href = "/";
                  break;
              case "errorInvalidRegistration": alert(update.errorMessage); break;
              case "loginSuccess":
                  this.setState({logIn: update}, ()=>{console.log("Login success")});
                  this.setCookie(update);
                  window.location.href = "/";
                  break;
              case "errorInvalidLogin": alert(update.errorMessage); break;
              case "logoutSuccess":
                  this.setState({logIn: {}});
                  this.setCookie({});
                  window.location.href = "/";
                  break;
              case "logoutFailure": alert(update.errorMessage); break;


          }
          
        }.bind(this);
    
        this.connection.onclose = function (e) {
          console.log('Connection lost');
          //THIS FUNCTION IS CALLED ON REFRESH IN FIREFOX BUT NOT CHROME
        };
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

    render(){

        return(
            <div id="Application">
                <Header sendObject={this.sendObject} isLoggedIn={this.isLoggedIn} logOut={this.logOut}/>
                <div id="page_container">
                    <Switch location={window.location}>
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

