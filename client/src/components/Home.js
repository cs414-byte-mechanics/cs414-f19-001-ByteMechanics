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

                <Dashboard userName={this.props.userName}/>
            </div>
        );
    }
}

export default Home;
