import React from 'react';
import {Card, CardBody, CardText, CardTitle, Button} from 'reactstrap'
 
class ViewGames extends React.Component {
    //This class needs to:
        //Get all the games from the server - hard code for now and do later (maybe this is sent back when they've sucessfully logged in?)
        //Make display prettier
        //Don't hard code
        //Move state?
    
    constructor(props){
        super(props)
        this.state={
            username: "ajeske",
            games: [
                {
                    matchID: "123",
                    date: "yesterday",
                    opponent: "amrictor",
                    whoseTurn: "amrictor",
                    board: "null"
                },
                {
                    matchID: "234",
                    date: "two days ago",
                    opponent: "fari",
                    whoseTurn: "ajeske",
                    board: "null"
                },
                {
                    matchID: "345",
                    date: "Week ago",
                    opponent: "zach",
                    whoseTurn: "ajeske",
                    board: "null"
                }
            ]
        }
        
        this.determineTurn = this.determineTurn.bind(this)
    }
    
    determineTurn(matchID){
        let game = this.state.games.find(object => object.matchID === matchID)
        if(game.whoseTurn === this.state.username){
            return "Your turn!"
        } else {
            return "Opponent's turn!"
        }
    }
    
    render(){
        let currentGames = 
            <div id="currentGames">
                { this.state.games.map((singleGame)=> 
                    <Card>
                        <CardTitle>Game vs. {singleGame.opponent}</CardTitle>
                        <CardText>{this.determineTurn(singleGame.matchID)}</CardText>
                        <Button>Resume</Button>
                    </Card>
                )}
            </div>

        return(
            <div id="homeScreen">
                {currentGames}
            </div>

        )
    }
}

export default ViewGames
