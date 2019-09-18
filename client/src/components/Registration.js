import React from 'react';
import { Col, Button, Form, FormGroup, Label, Input } from 'reactstrap';

class Registration extends React.Component {
    constructor(props){
        super(props);
        
        this.state = {
            email: '',
            username: '',
            password: ''
        }
        
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.updateEmail = this.updateEmail.bind(this);
        this.updateUsername = this.updateUsername.bind(this);
        this.updatePassword = this.updatePassword.bind(this);
    }
    
    handleFormSubmit(){
        //The state is set, now we want to send to server for checking
    }
    
    updateEmail(event){
        this.setState({ email: event.target.value });
    }
    
    updateUsername(event){
        this.setState({ username: event.target.value });
    }
    
    updatePassword(event){
        this.setState({ password: event.target.value });
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
                <button onClick={this.handleFormSubmit}> Submit</button>
            </Form>
        );
    }
}

export default Registration;
