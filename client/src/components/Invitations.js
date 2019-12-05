import React from 'react';
import Dashboard from "./Dashboard";
import './styles/Invitations.scss'
import {ButtonGroup, Input, Button, Table, Card, CardBody, CardTitle} from 'reactstrap';
import { FaEnvelopeOpenText as Invite, FaCheck as Accept, FaTimes as Reject} from 'react-icons/fa'

class Invitations extends React.Component {

    constructor(props) {
        super(props);

        this.submitSearchString = this.submitSearchString.bind(this);
        this.sendGameInvite = this.sendGameInvite.bind(this);
        this.updateSearchString = this.updateSearchString.bind(this);
        this.renderSearchInputs = this.renderSearchInputs.bind(this);
        this.listenForEnter = this.listenForEnter.bind(this);
        this.renderInviteDisplayButton = this.renderInviteDisplayButton.bind(this);
        this.handleRejectInvitationButtonClick = this.handleRejectInvitationButtonClick.bind(this);
        this.state = {
          searchString: '',
        };


    }
    // componentDidMount(){
    //     console.log(this.props);
    //     this.props.getInvitationsReceived();
    // }
    //
    // getInvitationsReceived() {
    //     console.log("something");
    //     // this.setState({showRefreshInvs: true})
    //     let getUserInvsLists = {
    //         communicationType: "getUserInvsLists",
    //         userName: this.props.userName
    //     };
    //     this.props.sendObject(getUserInvsLists);
    // }


    render () {
        console.log(this.props);

        return (
          <div id="invitations">
              <div id="viewsearch">
                <div id="invites">
                    <div id="subtitle">Current Invitations</div>
                    {this.renderInviteDisplayButton()}
                    {this.renderSentToInvitationsTable()}
                    {this.renderReceivedFromInvitationsTable()}
                </div>
                <div id="search">
                    <div id="subtitle">Invite a friend to play!</div>
                    {this.renderSearchInputs()}
                    {this.renderInvitePlayer()}
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

    listenForEnter(event) {
        if (event.keyCode === 13)
            this.submitSearchString(event);
    }

    renderSearchInputs() {
        return (
          <div>
              <div>
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

    renderInviteDisplayButton() {
      if (this.props.showRefreshInvs) {
        return (
          <Button onClick={this.props.getInvitationsReceived}>Refresh Invitations</Button>
        );
      }
      else {
        return (
          <Button onClick={this.props.getInvitationsReceived}>View Invitations</Button>
        );
      }
    }

  renderSentToInvitationsTable() {
      let toNames = this.props.invitationLists.sentToNames;
      if (toNames.length > 0 && toNames[0] !== "EMPTY") {
        return(
          <Card className="card_condensed">
            <CardBody>
              <CardTitle>Sent Invitations</CardTitle>
            <Table size="sm" className="table_condensed">
              <thead>
              <tr>
                <th>Sent To</th>
                <th>Time Sent</th>
              </tr>
              </thead>
              <tbody>{this.renderSentToTableRows()}</tbody>
            </Table>
            </CardBody>
          </Card>
        );
      }
      else if (this.props.showRefreshInvs) {
        return (
          <Card className="card_condensed">
            <CardBody>
              <CardTitle>No sent invitations to show</CardTitle>
            </CardBody>
          </Card>
        );
      }
  }

  renderSentToTableRows() {
      let rows = [];
      let namesList = this.props.invitationLists.sentToNames;
      let timesList = this.props.invitationLists.sentToTimes;
      for (let i = 0; i < namesList.length; i++) {
        rows.push(
          <tr>
            <td>{namesList[i]}</td>
            <td>{this.convertToDate(timesList[i])}</td>
          </tr>)
      }
      return rows;
  }

  renderReceivedFromInvitationsTable() {
    let fromNames = this.props.invitationLists.receivedFromNames;
    if (fromNames.length > 0 && fromNames[0] !== "EMPTY") {
      return(
        <Card className="card_condensed">
          <CardBody>
            <CardTitle>Received Invitations</CardTitle>
            <Table size="sm" className="table_condensed">
              <thead>
              <tr>
                <th>Received From</th>
                <th>Time Received</th>
              </tr>
              </thead>
              <tbody>{this.renderReceivedFromTableRows()}</tbody>
            </Table>
          </CardBody>
        </Card>
      );
    }
    else if (this.props.showRefreshInvs) {
      return (
        <Card className="card_condensed">
          <CardBody>
            <CardTitle>No received invitations to show</CardTitle>
          </CardBody>
        </Card>
      );
    }
  }

  renderReceivedFromTableRows() {
    let rows = [];
    let namesList = this.props.invitationLists.receivedFromNames;
    let timesList = this.props.invitationLists.receivedFromTimes;
    for (let i = 0; i < namesList.length; i++) {
      rows.push(
        <tr>
          <td>{namesList[i]}</td>
          <td>{this.convertToDate(timesList[i])}</td>
          <td>
            <ButtonGroup>
              <Accept id="accept"/>
              <Reject onClick={() => this.handleRejectInvitationButtonClick(namesList[i])}/>
            </ButtonGroup>
          </td>
        </tr>)
    }
    return rows;
  }

  convertToDate(timeMilli) {
      let date = new Date(parseInt(timeMilli,10));
      return date.toString().substring(0,23);
  }

  handleRejectInvitationButtonClick(invitationFrom) {
    let rejectInvite = {
      communicationType: "rejectInvite",
      userName: this.props.userName,
      invitationFrom: invitationFrom
    };
    this.props.sendObject(rejectInvite);
  }

}

export default Invitations;
