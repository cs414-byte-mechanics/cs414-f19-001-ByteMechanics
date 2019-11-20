import React from 'react';
import Dashboard from "./Dashboard";
import './styles/Invitations.scss'
import {Form, FormGroup, Label, Input, Button, InputGroup, InputGroupButtonDropdown, Row, Col} from 'reactstrap';

class Invitations extends React.Component {

    constructor(props) {
        super(props);

        this.submitSearchString = this.submitSearchString.bind(this);
        this.sendGameInvite = this.sendGameInvite.bind(this);

        this.state = {
          searchString: ''
        };
    }

    render () {
        return (
          <div id="invitations">
            {this.renderText()}
            <p/>
            <Row>{this.renderSearchBox()}</Row>
            <p/>
            <Row>{this.renderInvitePlayer()}</Row>
            <p/>
            <Row>{this.renderInviteStatus()}</Row>
          </div>
        );
    }

    renderText() {
        return (
          <div id="title">
              View your game invitations and invite other players here!
          </div>
        );
    }



    renderSearchButton() {
        return (
          <Button onClick={this.submitSearchString}>Search</Button>
        );
    }

    renderSearchBox() {
        return (
          <div>
              <div>
                <Label>Search for a User to Invite</Label>
                <div id="search">
                    <Input type="searchBox" name="searchBox" id="userSearchBox" placeholder="Search for a user..." onChange={event => this.updateSearchString(event)}/>
                    {this.renderSearchButton()}
                </div>
              </div>

          </div>
        );
    }

    renderSearchButton() {
        return (
          <Button onClick={this.submitSearchString}>Search</Button>
        );
    }

    updateSearchString(event) {
      this.setState({
        searchString: event.target.value
      });
    }

    submitSearchString() {
      if (this.state.searchString !== "") {
        let searchObject = {
          communicationType: "searchUser",
          userName: this.state.searchString
        };
        this.props.sendObject(searchObject);
      }
    }

    renderInvitePlayer() {
        console.log(this.props.searchResult)
        let list = []
        this.props.searchResult.forEach(user => {
            list.push(
                <div className="result">
                  <p>{user}</p>
                  <Button onClick={this.sendGameInvite}>Invite Player</Button>
                </div>
            )

        });
        return(
            <div id="searchResults">
                {list}
            </div>
        );
    }

    renderInviteStatus() {
      console.log(this.props.invitationSentStatus.statusMessage);
      if (this.props.showInvitationSentStatus) {
        if (this.props.invitationSentStatus.invitationSent) {
          return(
            <Col>
              <div><p>An invitation was successfully sent to {this.props.searchResult.userName}!</p></div>
            </Col>
          );
        }
        else {
          if (this.props.invitationSentStatus.statusMessage === "java.lang.Exception: duplicate invitation") {
            return (
              <Col>
                <div><p>Was not able to send an invitation to {this.props.searchResult.userName}! An Invitation from {this.props.searchResult.userName} has already been sent or received!</p></div>
              </Col>
            );
          }
          else {
            return (
              <Col>
                <div><p>Was not able to send an invitation to {this.props.searchResult.userName}!</p></div>
              </Col>
            );
          }
        }
      }
    }

    sendGameInvite() {
      let inviteObject = {
        communicationType: "sendInvitation",
        invitationFrom: this.props.userName,
        invitationTo: this.props.searchResult.userName
      };
      this.props.sendObject(inviteObject);
      this.setState({showInvitePlayer: false});
    }
}

export default Invitations;
