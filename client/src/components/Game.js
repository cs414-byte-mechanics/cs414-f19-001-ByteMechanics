import React, { Component } from 'react';
import { Container, Col, Row } from 'reactstrap';
import { Switch, Route, withRouter, Link } from "react-router-dom";

import Home from './Home.js'
import GameBoard from './GameBoard.js'
import Form from './Form.js'
import {attemptLogin, registerUser} from '../commObjects'
import './styles/Game.scss'
import { GiLion as CongoIcon } from 'react-icons/gi'

class Game extends Component {
    constructor(props){
        super(props);
        this.state = {
          loggedOn: false,
          player: {
            name: "test",
            email: "test@gmail.com"
          },
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
        this.updateScreen = this.updateScreen.bind(this);
    }

    componentDidMount() {
        this.connection = new WebSocket('ws://localhost:4444');
        console.log(typeof this.connection)
        
        this.connection.onopen = function () {
          console.log('Connected!');
          this.connection.send("Hello World!")

        }.bind(this);

        this.connection.onerror = function (error) {
          console.log('WebSocket Error ' + error);
          alert("Cannot reach server!")
        }.bind(this);
    
        this.connection.onmessage = function (e) {
          console.log('Server: ' + e.data);
          //let message = JSON.parse(e.data);
          
        }.bind(this);
    
        this.connection.onclose = function (e) {
          console.log('Connection lost');
          //THIS FUNCTION IS CALLED ON REFRESH IN FIREFOX BUT NOT CHROME
        };
    }

    sendObject(obj){
      this.connection.send(JSON.stringify(obj));
    }
    
    updateScreen(){
        this.setState({
            loggedOn: true
        })
    }

    render(){

        return(
            <div id="Application">
                <div id="header">
                    <div id="title">CongoOnline <CongoIcon id="icon"/></div>
                    <div id="menu">
                        <a className="nav-link" href="/">Home</a>
                        <a className="nav-link right" href="/register">Register</a>
                        <a className="nav-link right" href="/login">Log In</a>
                    </div>
                </div>
                <div id="page_container">
                    <Switch location={window.location}>
                        <Route
                            exact
                            path="/"
                            render={(props) => <Home/>}
                        />
                        <Route
                            path="/register"
                            render={(props) => <Form {...props} title="Register an account" action={registerUser} updateScreen={this.updateScreen} sendToServer={this.sendObject}/>}
                        />
                        <Route
                            path="/login"
                            render={(props) => <Form {...props} title="Log in" action={attemptLogin} updateScreen={this.updateScreen} sendToServer={this.sendObject}/>}
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

