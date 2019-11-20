import React from 'react';
import { Button, Label, Input } from 'reactstrap';
import Game from './Game';
import './styles/Form.scss';
//Used for Log in and Registration
class Form extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            registerUser: this.props.action
        }
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.updateTextField = this.updateTextField.bind(this);
        this.listenForEnter = this.listenForEnter.bind(this);
    }
    handleFormSubmit(){
        this.props.sendToServer(this.state.registerUser);
    }
    updateTextField(key, value) {
        let state = this.state;
        state.registerUser[key] = value;
        this.setState(state);
    }

    listenForEnter(event) {
        if (event.keyCode === 13)
            this.handleFormSubmit(event);
    }
    render () {
        return (

                <div id="form">
                    {this.props.isLoggedIn() ? "You're already logged in!" :
                    <>
                        <div className="page_title">{this.props.title}</div>
                        {Object.keys(this.state.registerUser).filter((key) => key.match(/user/i)).map((field)=>{
                            let type = field.match(/email/, "gi") ? "email" : field.match(/password/i) ? "password" : "text";
                            return (
                                <div className="form_input">
                                    <Label for={field}>{field.replace("user", "")}: </Label>
                                    <Input type={type} key={field} onChange={(event)=>{this.updateTextField(field, event.target.value)}} onKeyDown={this.listenForEnter}/>
                                </div>
                            );}
                        )}
                        <Button block size="sm" onClick={this.handleFormSubmit}>Submit</Button>
                    </>}
                </div>
        );
    }
}

export default Form;
