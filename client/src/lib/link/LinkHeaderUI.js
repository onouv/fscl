import React from 'react';
import PropTypes from 'prop-types';
import { Container, Navbar, Row, Col  } from 'react-bootstrap'

import BaseLinks from '../ui/BaseLinksUI'
import FSCLLinks from '../ui/FSCLLinksUI'

const LinkHeader = ({
   viewName,
   project
}) => {


   return(
      <Container fluid>
         <Navbar collapseOnSelect sticky="top" expand="md">
            <Row>
               <Col>
                  <Navbar.Brand href="/">FSCL</Navbar.Brand>
               </Col>
               <Col>
                  <Navbar.Collapse id="responsive-navbar-nav">
                     <BaseLinks viewName={viewName}/>
                     <FSCLLinks viewName={viewName} project={project}/>
                  </Navbar.Collapse>
               </Col>
               <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            </Row>
         </Navbar>
         <hr/>
      </Container>
   )
}

LinkHeader.propTypes = {
   viewName: PropTypes.string.isRequired,
   project: PropTypes.string.isRequired
}

export default LinkHeader
