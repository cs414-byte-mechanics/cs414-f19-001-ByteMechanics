import React from 'react';
import { GiLion as CongoIcon } from 'react-icons/gi'

class Header extends React.Component {
    constructor(props){
        super(props);
    }

    render () {
        let logInOut =
                this.props.isLoggedIn()
                    ? <a className="nav-link right" href="/" onClick={this.props.logOut}>Log Out (<u>{this.props.userName}</u>)</a>
                :   <>
                        <a className="nav-link right" href="/register">Register</a>
                        <a className="nav-link right" href="/login">Log In</a>
                    </>

        return (
            <div id="header">
            <div id="title">
                CongoOnline
                <CongoIcon id="icon"/>
            </div>
            <div id="menu">
                <a className="nav-link" href="/">Home</a>
                {logInOut}
            </div>
            </div>
        );
    }



}
export default Header;

