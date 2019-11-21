import React from 'react';

class ViewGames extends React.Component {
    //This class needs to:
        //Get all the games from the server - hard code for now and do later (maybe this is sent back when they've sucessfully logged in?)
        //Store information in state for now
        //Display the information on the screen
        //Make display prettier
        //Don't hard code
        //Move state?
    
    constructor(props){
        super(props)
        this.state={
            games: [
                {
                    date: "yesterday",
                    opponent: "amrictor",
                    board: "null"
                },
                {
                    date: "two days ago",
                    opponent: "fari",
                    board: "null"
                },
                {
                    date: "Week ago",
                    opponent: "zach",
                    board: "null"
                }
            ]
        }
    }
    
    render(){
        return(
            <h2>Hello</h2>
        )
    }
}

export default ViewGames
