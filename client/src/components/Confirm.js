import React from 'react';
import {Button, Modal, ModalHeader, ModalBody, ModalFooter} from 'reactstrap';

class Confirm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            open : false
        };
        
        this.toggleModal = this.toggleModal.bind(this)
    }
    
    toggleModal(){
        this.setState({open : !this.state.open})
    }

    render() {
        return (
            <div className={this.props.className} title={this.props.title}>
            <div onClick={this.toggleModal}>{this.props.button}</div>
            <Modal isOpen={this.state.open} toggle={this.toggleModal}>
                <ModalHeader toggle={this.toggleModal}>Confirm {this.props.reason}</ModalHeader>
                <ModalBody>Are you sure you want to {this.props.reason.toLowerCase()}?</ModalBody>
                <ModalFooter>
                <Button onClick={()=>{this.props.onClick(); this.toggleModal();}}>Yes</Button>{' '}
                <Button onClick={this.toggleModal}>No</Button>
                </ModalFooter>
            </Modal>
            </div>
        );
    }
}

export default Confirm
