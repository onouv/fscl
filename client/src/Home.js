import React from 'react';
import { Navbar, Container, Figure, Jumbotron, Row, Col } from 'react-bootstrap';

import logo from './lib/ui/fscl-logo.png';
import BaseLinks from './lib/ui/BaseLinksUI';
import ViewNames from './lib/ui/ViewNames'


const HomeHeader = () =>
   <div>
      <Navbar collapseOnSelect sticky="top" expand="md">
         <Row>
            <Col>
               <Navbar.Brand href="/">FSCL</Navbar.Brand>
            </Col>
            <Col>
               <Navbar.Collapse id="responsive-navbar-nav">
                  <BaseLinks viewName={ViewNames.HOME}/>
               </Navbar.Collapse>
            </Col>
         </Row>
      </Navbar>
   </div>

export const Home = () => (
   <div>
      <HomeHeader/>
      <Container fluid>
         <Jumbotron fluid>
            <Row>
               <Col xs={2}>
                  <Figure>
                     <Figure.Image
                        width={120}
                        height={150}
                        alt="fscl-logo"
                        src={logo}
                     />
                  </Figure>
               </Col>
               <Col>
                  <h1>F S C L</h1>
                  <p>A System to Manage Systems</p>
               </Col>
            </Row>
         </Jumbotron>
      </Container>
   </div>
);
