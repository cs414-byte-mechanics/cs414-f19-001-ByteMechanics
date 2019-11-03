import React from 'react';
import Dashboard from "./Dashboard";
import {Form, FormGroup, Label, Input, Button, InputGroup, InputGroupButtonDropdown, Row, Col} from 'reactstrap';

class Invitations extends React.Component {

    constructor(props){
        super(props);

      this.submitSearchString = this.submitSearchString.bind(this);

        this.state = {
          searchString: ''
        };
    }

    render () {
        return (
          <div>
            {this.renderText()}
            <p/>
            <Row>{this.renderSearchBox()}</Row>
            <p/>
            <Row>{this.renderInvitePlayer()}</Row>
          </div>
        );
    }

    renderText() {
        return (
          <div id="invitations">
              View your game invitations and invite other players here!
          </div>
        );
    }

    renderSearchBox() {
        return (
          <div>
          <Col>
            <Label>Search for a User to Invite</Label>
            <Input type="searchBox" name="searchBox" id="userSearchBox" placeholder="Enter the name of the user..." onChange={event => this.updateSearchString(event)}/>
          </Col>
          <Col>
            {this.renderSearchButton()}
          </Col>
          </div>
        );
    }

    renderSearchButton() {
        return (
          <Button color="info" onClick={this.submitSearchString}>Search</Button>
        );
    }

    updateSearchString(event) {
      this.setState({
        searchString: event.target.value
      });
    }

    submitSearchString() {
      let searchObject = {
        communicationType: "searchUser",
        userName: this.state.searchString
      };
      this.props.sendObject(searchObject);
    }

    renderInvitePlayer() {

      console.log("in renderInvitePlayer");

      if (this.props.showInvitePlayer) {
        if (this.props.searchResult.userFound && this.props.showInvitePlayer) {
          return (
            <Col>
              <div>
                <p>{this.props.searchResult.userName} has been found!</p>
                <Button color= "info">Invite Player</Button>
              </div>
            </Col>
        );
        } else {
          return (
            <Col>
              <div><p>{this.props.searchResult.userName} wasn't found!</p></div>
            </Col>
          );
        }
      }
      else {
        return
      }
    }

}

export default Invitations;
