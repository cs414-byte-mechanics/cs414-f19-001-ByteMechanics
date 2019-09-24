import React from 'react';
import { Card, CardHeader, CardFooter, CardBody, CardTitle, CardText,
    Col, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import Game from './Game';

class Registration extends React.Component {
    constructor(props){
        super(props);
        
        this.state = {
            registerUser: {
                "communicationType": "registerUser",
                "communicationVersion": 1,
                "userName": "",
                "userPassword": "",
                "userEmail": ""
            }
        }
        
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.updateEmail = this.updateEmail.bind(this);
        this.updateUsername = this.updateUsername.bind(this);
        this.updatePassword = this.updatePassword.bind(this);
    }
    
    handleFormSubmit(){
        //The state is set, now we want to send to server for checking
        this.props.updateScreen();
        this.props.sendToServer(this.state.registerUser);
    }
    
    updateEmail(event){
        this.state.registerUser.userEmail = event.target.value;
    }
    
    updateUsername(event){
        this.state.registerUser.userName = event.target.value;
    }
    
    updatePassword(event){
        this.state.registerUser.userPassword = event.target.value;
    }
    
    render () {
        return (
            <Form>
                <FormGroup>
                    <Label for="Email">Email</Label>
                    <Col sm={10}>
                        <Input type="email" name="email" id="userEmail" onChange={this.updateEmail}/>
                    </Col>
                </FormGroup>
                <FormGroup>
                    <Label for="Username">Username</Label>
                    <Col sm={10}>
                        <Input type="username" name="username" id="username" onChange={this.updateUsername}/>
                    </Col>
                </FormGroup>
                <FormGroup>
                    <Label for="Password">Password</Label>
                    <Col sm={10}>
                        <Input type="password" name="password" id="userPassword" onChange={this.updatePassword}/>
                    </Col>
                </FormGroup>
                <Button onClick={this.handleFormSubmit}> Submit</Button>
            </Form>
        );
    }
}

export default Registration;
