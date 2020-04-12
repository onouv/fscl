import React from 'react';
import { Nav } from 'react-bootstrap';
import PropTypes from 'prop-types';

import ViewNames from './ViewNames.js'

const BaseLinks = ({viewName}) =>
   <Nav variant="pills" activeKey={viewName}>
      <Nav.Link eventKey={ViewNames.HOME} href="/">Home</Nav.Link>
      <Nav.Link eventKey={ViewNames.PROJECTS} href="/projects">Projects</Nav.Link>
   </Nav>

BaseLinks.propTypes = {
   viewName: PropTypes.string.isRequired
}

export default BaseLinks;
