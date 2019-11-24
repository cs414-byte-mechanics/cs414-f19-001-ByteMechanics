import React from 'react';
import ViewGames from "./ViewGames";

class Dashboard extends React.Component {
    constructor(props){
        super(props);
    }

    render () {
        return (
            <div id="dash">
                Hi {this.props.userName}! Access your games or invite a friend to play!
                <ViewGames/>
            </div>
        );
    }
}

export default Dashboard;
