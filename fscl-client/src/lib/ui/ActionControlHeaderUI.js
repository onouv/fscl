import React from 'react';
import PropTypes from 'prop-types';
import { Container, Navbar, Row, Col } from 'react-bootstrap'

import HeaderButtonGroup from './HeaderButtonGroupUI'
import fscl from '../lib'

const ActionControlHeaderUI = ({
   saveable,
   deletable,
   caption= "",
   sourceEntityId,
   entities,
   onSave,
   onDelete,
   onNew  }) => {

      return(
         <Container fluid>
            <Row>
               <Col lg={4}>
                  <Navbar.Brand>{caption}</Navbar.Brand>
               </Col>
               <Col>
                  <HeaderButtonGroup
                     saveable={saveable}
                     deletable={deletable}
                     onSave={() => onSave(sourceEntityId, entities)}
                     onDelete={() => onDelete(sourceEntityId, entities)}
                     onNew={() => onNew(sourceEntityId)}
                  />
               </Col>
               <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            </Row>
            <hr/>
         </Container>
      )
}
ActionControlHeaderUI.propTypes = {
   saveable: PropTypes.bool.isRequired,
   deletable: PropTypes.bool.isRequired,
   caption: PropTypes.string,
   sourceEntityId: fscl.propTypes.code,
   onSave: PropTypes.func.isRequired,
   onDelete: PropTypes.func.isRequired,
   onNew: PropTypes.func.isRequired
}


export default ActionControlHeaderUI
