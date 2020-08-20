import React from 'react'
import { Navbar, Row, Col, ButtonGroup, Button } from 'react-bootstrap'
import { connect } from 'react-redux';
import { Types } from '../redux/EntityActionTerms'

const ModalHeader = ({
   title,
   onConfirmCaption="OK",
   onConfirm,
   confirmEnabled=false,
   onCancelCaption="Cancel",
   onCancel
}) =>
   <div>
   <Row>
      <Col>
         <Navbar.Brand>{title}</Navbar.Brand>
      </Col>
      <Col>
         <ButtonGroup>
            <Button
               onClick={onConfirm}
               variant="secondary"
               disabled={!confirmEnabled}>
               {onConfirmCaption}
            </Button>
            <Button
               onClick={onCancel}
               variant="secondary">
               {onCancelCaption}
            </Button>
         </ButtonGroup>

      </Col>
   </Row>
   <hr/>
   </div>

const mapDispatchToProps = (dispatch) => ({
   onCancel() {
      dispatch({
         type: Types.MODAL.HIDE
      })
   }
})

export default connect(null, mapDispatchToProps)(ModalHeader)
