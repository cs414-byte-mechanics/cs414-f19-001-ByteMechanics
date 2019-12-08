import React from 'react';
import './styles/MyGames.scss'
import {Input, Button, UncontrolledButtonDropdown, DropdownMenu, DropdownItem, DropdownToggle } from 'reactstrap';
import { FaTrashAlt as Trash, FaPlay as Play} from 'react-icons/fa'
import Confirm from './Confirm.js'

class MyGames extends React.Component {

    windowID = 1;

    constructor(props) {
        super(props);
        this.getGames = this.getGames.bind(this);
        this.updateSearchString = this.updateSearchString.bind(this);
        this.renderSearchInputs = this.renderSearchInputs.bind(this);
        this.listenForEnter = this.listenForEnter.bind(this);
        this.abandonGame = this.abandonGame.bind(this);
        this.updateStatus = this.updateStatus.bind(this);

        this.state = {
            searchString: '',
            status: 'In Progress',
            matchID: 1
        };
    }

    getGames(){
        let searchObject = {
            communicationType: "searchGames",
            userName: this.props.userName,
            playerTwoName: this.state.searchString,
            status: this.state.status,
        };
        this.props.sendObject(searchObject);
    }

    componentDidMount(){
        this.getGames();
    }

    listenForEnter(event) {
        if (event.keyCode === 13)
            this.getGames();
    }

    updateStatus(event) {
        this.setState({status: event.target.value}, () => this.getGames())
    }
    renderSearchInputs() {
        return (
            <div id="search_input">
                <Input type="search" placeholder="Filter on opponent..." onChange={this.updateSearchString} onKeyDown={this.listenForEnter}/>
                <UncontrolledButtonDropdown>
                    <DropdownToggle caret> {this.state.status === '' ? "Games Status" : this.state.status} </DropdownToggle>
                    <DropdownMenu>
                        <DropdownItem value="In Progress" onClick={this.updateStatus}>In Progress</DropdownItem>
                        <DropdownItem value="Finished" onClick={this.updateStatus}>Finished</DropdownItem>
                        <DropdownItem value="Abandoned" onClick={this.updateStatus}>Abandoned</DropdownItem>
                        <DropdownItem value="All" onClick={this.updateStatus}>All</DropdownItem>
                    </DropdownMenu>
                </UncontrolledButtonDropdown>
                <Button onClick={this.getGames}>Search</Button>
            </div>
        );
    }

    updateSearchString(event) {
      this.setState({
        searchString: event.target.value
      });
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
        this.getGames();
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
