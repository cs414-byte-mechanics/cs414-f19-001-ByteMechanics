import React from 'react';
import { Button } from 'reactstrap';
import Invitations from "./Invitations";
import "./styles/Dashboard.scss"
import MyGames from "./MyGames";
import Confirm from "./Confirm";

class Dashboard extends React.Component {
    constructor(props){
        super(props);
        console.log(props);

        this.state = {
            "communicationType": "unregisterUser",
            "userName": this.props.userName,
            searchString: '',
            status: 'In Progress'
        }
        this.unregisterUser = this.unregisterUser.bind(this);
        this.getGames = this.getGames.bind(this);
    }

    unregisterUser() {
        this.props.sendObject(this.state);
        this.props.logOut();
    }

    getGames(){
        let searchObject = {
            communicationType: "searchGames",
            userName: this.props.userName,
            playerTwoName: this.state.searchString,
            status: this.state.status,
        };
        this.props.sendObject(searchObject);
    }

    render () {
        return (
            <div id="dash">
                <div className="header">
                    <p >Hi <b>{this.props.userName}</b>! Access your games and options here.</p>
                    <Confirm className="delete" onClick={this.unregisterUser} reason="Delete Account" button=<a className="delete">Delete Account</a>/>
                </div>
                <div id="features">
                    <MyGames isLoggedIn={this.props.isLoggedIn}
                             userName={this.props.userName}
                             gamesResults={this.props.gamesResults}
                             sendObject={this.props.sendObject}
                             getGames={this.getGames}
                    />
                    <Invitations isLoggedIn={this.props.isLoggedIn}
                                 userName={this.props.userName}
                                 sendObject={this.props.sendObject}
                                 searchResult={this.props.searchResult}
                                 showInvitePlayer={this.props.showInvitePlayer}
                                 invitationSentStatus={this.props.invitationSentStatus}
                                 showInvitationSentStatus={this.props.showInvitationSentStatus}
                                 invitationLists={this.props.invitationLists}
                                 getInvitationsReceived={this.getInvitationsReceived}
                                 showRefreshInvs={this.props.showRefreshInvs}
                                 getGames={this.getGames}
                    />
                </div>

            </div>
        );
    }
}

export default Dashboard;
