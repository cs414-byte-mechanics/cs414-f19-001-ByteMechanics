import React, { Component } from 'react';
import { Container, Col, Row } from 'reactstrap';
import GameBoard from './GameBoard.js'
import Registration from './Registration.js'


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
        let registration =
            <Registration updateScreen={this.updateScreen}/>
            
        let game = ''
        if(this.state.loggedOn){
            game = 
                <GameBoard game={this.state.games[0]} send={this.sendObject}/>  
                registration = ''
        }
                
        return(
            <Container id="Application">
                <Row>
                    <Col className= 'text-center'>
                        {registration}
                        <br></br>
                        {game}
                    </Col>
                </Row>
            </Container>
        );
    }
}

export default Game;

