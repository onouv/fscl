import React from 'react';
import { Container, Navbar, Row, Col } from 'react-bootstrap';

import PropTypes from 'prop-types';

const ProjectLine = ({project="", name=""}) =>
   <Container fluid>
      <Navbar>
         <Row>
            <Col>
               <Navbar.Brand id="project-code">Project: {project}</Navbar.Brand>
            </Col>
            <Col>
               <Navbar.Brand id="project-name"> {name}</Navbar.Brand>
            </Col>
         </Row>
      </Navbar>
      <hr/>
   </Container>


ProjectLine.propTypes = {
   project: PropTypes.string.isRequired,
   name: PropTypes.string
}


export default ProjectLine;
