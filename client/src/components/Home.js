import React from 'react';
import Dashboard from "./Dashboard";
import ViewGames from "./ViewGames";

class Home extends React.Component {
    constructor(props){
        super(props);
    }

    render () {
        return (
            <div id="home">
                {this.props.isLoggedIn()
                    ? <Dashboard userName={this.props.userName}/>
                    : <ViewGames/>
                }
            </div>
        );
    }
}

export default Home;
