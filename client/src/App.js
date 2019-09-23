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
        let registration =
             <Card>
                    <CardHeader>Welcome to Congo!</CardHeader>
                    <CardBody>
                        <CardText> </CardText>
                        <Registration updateScreen={this.updateScreen}/>
                    </CardBody>
            </Card>
            
        let game = ''
        if(this.state.displayGame){
            game = 
                <Card>
                    <CardBody>
                        <Game/>
                    </CardBody>
                </Card>
            registration = ''
        }
                
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
