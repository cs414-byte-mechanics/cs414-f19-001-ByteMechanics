import React from 'react';
import './styles/MyGames.scss'
import {Input, Button, UncontrolledButtonDropdown, DropdownMenu, DropdownItem, DropdownToggle } from 'reactstrap';
import { FaTrashAlt as Trash, FaPlay as Play} from 'react-icons/fa'
import Confirm from './Confirm.js'

class MyGames extends React.Component {

    windowID = 1;

    constructor(props) {
        super(props);
        this.renderSearchInputs = this.renderSearchInputs.bind(this);
        this.listenForEnter = this.listenForEnter.bind(this);
        this.abandonGame = this.abandonGame.bind(this);

        this.state = {
            matchID: 1
        };
    }

    componentDidMount(){
        this.props.getGames();
    }

    listenForEnter(event) {
        if (event.keyCode === 13)
            this.props.getGames();
    }

    renderSearchInputs() {
        return (
            <div id="search_input">
                <Input type="search" placeholder="Filter on opponent..." onChange={this.props.updateSearchString} onKeyDown={this.listenForEnter}/>
                <UncontrolledButtonDropdown>
                    <DropdownToggle caret> {this.props.statusMyGames === '' ? "Games Status" : this.props.statusMyGames} </DropdownToggle>
                    <DropdownMenu>
                        <DropdownItem value="In Progress" onClick={this.props.updateStatus}>In Progress</DropdownItem>
                        <DropdownItem value="Finished" onClick={this.props.updateStatus}>Finished</DropdownItem>
                        <DropdownItem value="Abandoned" onClick={this.props.updateStatus}>Abandoned</DropdownItem>
                        <DropdownItem value="All" onClick={this.props.updateStatus}>All</DropdownItem>
                    </DropdownMenu>
                </UncontrolledButtonDropdown>
                <Button onClick={this.props.getGames}>Search</Button>
            </div>
        );
    }


    playGame(id){

        window.open(`/game/${id}`, `popUpWindow${this.windowID}`, "resizable=yes,top=200,left=600,width=700,height=700");
        this.windowID++;
    }
    
    abandonGame(matchID){
        let abandonObject = {
            communicationType: "quitMatch",
            matchID: matchID,
            playerQuitting: this.props.userName
        }
        this.props.sendObject(abandonObject)
        alert("Game sucessfully abandoned")
        this.props.getGames();
    }


    renderTableData(games){
        if(this.props.gamesResults.length===0) return (<p>No matches found</p>);
        return this.props.gamesResults.map((data) => {
        let data_array = data.split(',');
        return (
            <div className="result" key={data_array[0]}>
                <p><b><a href={"/user/"+data_array[1]}>{data_array[1]}</a></b></p>
                <p><i>{data_array[2]}</i></p>
                <p>Last updated {data_array[3]}</p>
                <div className="game_buttons">
                    <Confirm title="Abandon" className="game_buttons" onClick={e => this.abandonGame(data_array[0])} button=<Trash className="game_buttons"/> reason="Abandon Game"/>
                    <Play title="Play" className="game_buttons" onClick={e => this.playGame(data_array[0])}/>
                </div>
            </div>);
        })
    }

    render () {
        return (
            <div id="mygames">
                <div id="wrapper">
                    <div id="games">
                        <div id="subtitle">My Games</div>
                        {this.renderSearchInputs()}
                        <div id="game_data">
                            {this.renderTableData(this.props.gamesResults)}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
export default MyGames;
