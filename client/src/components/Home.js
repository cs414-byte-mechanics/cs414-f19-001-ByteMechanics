import React from 'react';
import Dashboard from "./Dashboard";

class Home extends React.Component {

    render () {
        return (
            <div id="home">
                {this.props.isLoggedIn()
                    ? <Dashboard
                        userName={this.props.userName}
                        logOut={this.props.logOut}
                        isLoggedIn={this.props.isLoggedIn}
                        gamesResults={this.props.gamesResults}
                        sendObject={this.props.sendObject}
                        searchResult={this.props.searchResult}
                        showInvitePlayer={this.props.showInvitePlayer}
                        invitationSentStatus={this.props.invitationSentStatus}
                        showInvitationSentStatus={this.props.showInvitationSentStatus}
                        invitationLists={this.props.invitationLists}
                        getInvitationsReceived={this.props.getInvitationsReceived}
                        showRefreshInvs={this.props.showRefreshInvs}
                    />
                    : "Welcome to CongoOnline! Register an account and invite your friends to play Congo!"
                }
            </div>
        );
    }
}

export default Home;
