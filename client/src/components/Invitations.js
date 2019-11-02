import React from 'react';
import Dashboard from "./Dashboard";
import {Form, FormGroup, Label, Input, Button} from 'reactstrap';

class Invitations extends React.Component {
    constructor(props){
        super(props);
    }

    render () {
        return (
          <div>
            {this.renderText()}
            <p/><p/><p/>
            {this.renderSearchBox()}
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
            <Form>
                <FormGroup>
                    <Label>Search for a User to Invite</Label>
                    <Input type="searchBox" name="searchBox" id="userSearchBox" placeholder="Enter the name of the user..." />
                    {this.renderSearchButton()}
                </FormGroup>
            </Form>
          </div>
        );
    }

    renderSearchButton() {
        return (
          <Button color="info">Search</Button>
        );
    }

}

export default Invitations;
