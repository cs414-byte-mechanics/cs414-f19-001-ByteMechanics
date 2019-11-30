import React from 'react';
import { Button } from 'reactstrap';
import Invitations from "./Invitations";
import "./styles/Dashboard.scss"

class Dashboard extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            "communicationType": "unregisterUser",
            "userName": this.props.userName
        }
        this.unregisterUser = this.unregisterUser.bind(this);
    }

    unregisterUser() {
        this.props.sendToServer(this.state);
        this.props.logOut();
    }

    render () {
        return (
            <div id="dash">
                Hi {this.props.userName}! Access your games and options here.
                <br/>
                <a id="delete" title="Are you sure? Clicking this will delete your account forever!" onClick={this.unregisterUser} >Delete account</a>

            </div>
        );
    }
}

export default Dashboard;
