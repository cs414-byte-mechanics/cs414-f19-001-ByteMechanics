import React, { Component } from 'react';


class Game extends Component {
    constructor(props){
	super(props);
        this.connection = null;
    }

    componentDidMount() {
        this.connection = new WebSocket('ws://localhost:4444');
        this.connection.onopen = function () {
          console.log('Connected!');
        };

        this.connection.onerror = function (error) {
          console.log('WebSocket Error ' + error);
          alert("Cannot reach server!")
        };
    
        this.connection.onmessage = function (e) {
          console.log('Server: ' + e.data);
          //let message = JSON.parse(e.data);
          
        }.bind(this);
    
        this.connection.onclose = function (e) {
          console.log('Connection lost');
          //THIS FUNCTION IS CALLED ON REFRESH IN FIREFOX BUT NOT CHROME
        };
    }

    render(){
        return (
            <div className="App">
                <h3>Coming soon!</h3>
                <img src="https://upload.wikimedia.org/wikipedia/commons/7/7b/Congo_gameboard_and_init_config.PNG" alt="Congo (chess variant)"/>
            </div>
        );
    }
}

export default Game;

