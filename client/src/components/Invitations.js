import React from 'react';
import Dashboard from "./Dashboard";
import './styles/Invitations.scss'
import {Form, FormGroup, Label, Input, Button, InputGroup, InputGroupButtonDropdown, Row, Col} from 'reactstrap';
import { FaEnvelopeOpenText as Invite } from 'react-icons/fa'

class Invitations extends React.Component {

    constructor(props) {
        super(props);

        this.submitSearchString = this.submitSearchString.bind(this);
        this.sendGameInvite = this.sendGameInvite.bind(this);
        this.updateSearchString = this.updateSearchString.bind(this);
        this.renderSearchInputs = this.renderSearchInputs.bind(this);
        this.listenForEnter = this.listenForEnter.bind(this);

        this.state = {
          searchString: ''
        };
    }

    render () {
        return (
          <div id="invitations">
            {this.renderText()}
            <div id="viewsearch">
                <div id="invites">
                    <div id="subtitle">Current Invitations</div>
                </div>
                <div id="search">
                    {this.renderSearchInputs()}
                    {this.renderInvitePlayer()}
                </div>
            </div>
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

    listenForEnter(event) {
        if (event.keyCode === 13)
            this.submitSearchString(event);
    }

    renderSearchInputs() {
        return (
          <div>
              <div>
                <div id="subtitle">Search for a User to Invite</div>
                <div id="search_input">
                    <Input type="search" placeholder="Search for a user..." onChange={this.updateSearchString} onKeyDown={this.listenForEnter}/>
                    <Button onClick={this.submitSearchString}>Search</Button>
                </div>
              </div>

          </div>
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
          userName: this.state.searchString,
          playerName: this.props.userName
        };
        this.props.sendObject(searchObject);
      }
    }

    renderInvitePlayer() {
        let list = []
        this.props.searchResult.forEach(user => {
            list.push(
                <div className="result" key={user}>
                  <p>{user}</p>
                  <Invite className="invite_button" onClick={()=>{this.sendGameInvite(user)}}/>
                </div>
            )
        });
        return(
            <div id="searchResults">
                {list.length > 0 ? list : <p>No users found</p>}
            </div>
        );
    }

    sendGameInvite(userName) {
      let inviteObject = {
        communicationType: "sendInvitation",
        invitationFrom: this.props.userName,
        invitationTo: userName
      };
      this.props.sendObject(inviteObject);
      this.setState({showInvitePlayer: false});
    }
}

export default Invitations;
