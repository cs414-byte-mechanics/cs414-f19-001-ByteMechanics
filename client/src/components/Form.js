import React from 'react';
import { Button, Label, Input } from 'reactstrap';
import './styles/Form.scss';
//Used for Log in and Registration
class Form extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            submit: this.props.action
        }
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.updateTextField = this.updateTextField.bind(this);
        this.listenForEnter = this.listenForEnter.bind(this);
    }
    handleFormSubmit(){
        let patterns = {
            userName: /[A-Za-z0-9]+/,
            userEmail: /[A-Za-z0-9_\-.]+@[A-Za-z0-9_\-.]+\.[A-Za-z0-9_\-.]+/,
            userPassword: /[A-Za-z0-9]+/
        }
        for (var i = 0; i < Object.keys(this.state.submit).length; i++) {
            var key = Object.keys(this.state.submit)[i]
            if (typeof this.state.submit[key] == "string" && !this.state.submit[key].match(patterns[key])) {
                alert("Please ensure that you have entered a valid email.")
                return;
            }
        }
        this.props.sendToServer(this.state.submit);
    }
    updateTextField(key, value) {
        let state = this.state;
        state.submit[key] = value;
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
                    {Object.keys(this.state.submit).filter((key) => key.match(/user/i)).map((field)=>{
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
