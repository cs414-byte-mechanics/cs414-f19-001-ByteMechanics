import React from 'react';
import Dashboard from "./Dashboard";

class Invitations extends React.Component {
    constructor(props){
        super(props);
    }

    render () {
        return (
            <div id="invitations">
                View your game invitations and invite other players here.
            </div>
        );
    }
}

export default Invitations;
