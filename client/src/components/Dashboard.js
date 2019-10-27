import React from 'react';

class Dashboard extends React.Component {
    constructor(props){
        super(props);
    }

    render () {
        return (
            <div id="dash">
                Hi {this.props.userName}! Access your games and options here.
            </div>
        );
    }
}

export default Dashboard;
