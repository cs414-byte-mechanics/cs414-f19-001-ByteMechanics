import React from 'react';
import Dashboard from "./Dashboard";

class Home extends React.Component {
    constructor(props){
        super(props);
    }

    render () {
        return (
            <div id="home">
                {this.props.isLoggedIn()
                    ? <Dashboard userName={this.props.userName} logOut={this.props.logOut} sendToServer={this.props.sendToServer}/>
                    : "Welcome to CongoOnline! Register an account and invite your friends to play Congo!"
                }
            </div>
        );
    }
}

export default Home;
