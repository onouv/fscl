import React from 'react';
import { Nav } from 'react-bootstrap';
import PropTypes from 'prop-types';
import ViewNames from './ViewNames';
import links from '../links'

const FSCLLinks = ({
   viewName,
   project }) =>

      <Nav variant="pills" activeKey={viewName}>
         <Nav.Link eventKey={ViewNames.FUNCTIONS} href={links.functions.url(project)}>Functions</Nav.Link>
         <Nav.Link
            eventKey={ViewNames.SYSTEMS}
            href="/systems"
            disabled={true}>
            Systems
         </Nav.Link>
         <Nav.Link eventKey={ViewNames.COMPONENTS} href={links.components.url(project)}>Components</Nav.Link>
         <Nav.Link
            eventKey={ViewNames.LOCATIONS}
            href="/locations"
            disabled={true}>
            Locations
         </Nav.Link>
      </Nav>

FSCLLinks.propTypes = {
      viewName: PropTypes.string.isRequired
   }


export default FSCLLinks;
