import React from 'react';
import './styles/MyGames.scss'
import {Input, Button, UncontrolledButtonDropdown, DropdownMenu, DropdownItem, DropdownToggle } from 'reactstrap';
import { FaEnvelopeOpenText as Invite} from 'react-icons/fa'

class Profile extends React.Component {

    constructor(props) {
        super(props);
        this.getGames = this.getGames.bind(this);
        this.updateSearchString = this.updateSearchString.bind(this);
        this.renderSearchInputs = this.renderSearchInputs.bind(this);
        this.listenForEnter = this.listenForEnter.bind(this);
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
            userName: this.props.match.params.username,
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

    sendGameInvite(userName) {
        let inviteObject = {
            communicationType: "sendInvitation",
            invitationFrom: this.props.userName,
            invitationTo: userName
        };
        this.props.sendObject(inviteObject);
        this.setState({showInvitePlayer: false});
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
            </div>);
        })
    }

    render () {
        return (
            <div id="profiles">
                <div id="wrapper">
                    <div id="games">
                        <div id="subtitle"><b>{this.props.match.params.username}</b>'s Game History
                            <Invite className="invite_button" title="Invite to play!" onClick={()=>{this.sendGameInvite(this.props.match.params.username)}}/>
                       </div>
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
export default Profile;
