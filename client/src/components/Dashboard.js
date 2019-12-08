import React from 'react';
import Invitations from "./Invitations";
import "./styles/Dashboard.scss"
import MyGames from "./MyGames";
import Confirm from "./Confirm";
import Home from "./Game";

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
        this.props.sendObject(this.state);
        this.props.logOut();
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
                             searchString={this.props.searchString}
                             statusMyGames={this.props.statusMyGames}
                             getGames={this.props.getGames}
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
                    />
                </div>

            </div>
        );
    }
}

export default Dashboard;
