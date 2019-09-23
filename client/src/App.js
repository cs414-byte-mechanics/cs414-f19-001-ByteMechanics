import React from 'react';
import {Card, CardHeader, CardText, CardBody, Container, Row, Col} from 'reactstrap'
import logo from './logo.svg';
import './App.css';
import Game from './components/Game';
import Registration from './components/Registration';

class App extends React.Component {
    constructor(props){
        super(props);
        
        this.state = {
            displayGame: false
        }
        
        this.updateScreen = this.updateScreen.bind(this);
    }
    
    updateScreen(){
        this.setState({
            displayGame: true
        })
    }
    
    render(){
        
        let game = ''
        if(this.displayGame){
            game = 
                <Card>
                    <CardHeader> Start or Resume a Game </CardHeader>
                        <CardBody>
                            <Game/>
                        </CardBody>
                </Card>
        }
        
        let registration =
             <Card>
                    <CardHeader>Welcome to Congo!</CardHeader>
                    <CardBody>
                        <CardText> </CardText>
                        <Registration updateScreen={this.updateScreen}/>
                    </CardBody>
            </Card>
                
        return(
            <Container id="Application">
                <Row>
                    <Col className= 'text-center'>
                        {registration}
                        <br></br>
                        {game}
                    </Col>
                </Row>
            </Container>
        );
    }
    
}

export default App;
