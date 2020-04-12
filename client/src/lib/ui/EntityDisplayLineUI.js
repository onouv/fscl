import React from 'react';
import { Container, Navbar, Row, Col } from 'react-bootstrap';


const EntityDisplayLine = ({caption, entity, ContentComponent}) =>
   <Container fluid>
      
      <Row>
         <Col lg={1}>
            <Navbar.Brand>{caption}</Navbar.Brand>
         </Col>
         <Col lg={11}>
            <ContentComponent entity={entity}/>
         </Col>
         </Row>

      <hr/>
   </Container>

export default EntityDisplayLine
