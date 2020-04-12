import React from 'react';
import { Navbar, Row, Col } from 'react-bootstrap';
import PropTypes from 'prop-types';

import HeaderButtonGroup from './HeaderButtonGroupUI';
import BaseLinks from './BaseLinksUI';
import FSCLLinks from './FSCLLinksUI';

export const FSCLHeader = ({
   viewName,
   saveable=false,
   deletable=false,
   project,
   onNew=f=>f,
   onSave=f=>f,
   onDelete=f=>f
}) =>
   <div>
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
            <Col>
               <HeaderButtonGroup
                  saveable={saveable}
                  deletable={deletable}
                  project={project}
                  onNew={onNew}
                  onSave={onSave}
                  onDelete={onDelete} />
            </Col>
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
         </Row>
      </Navbar>
      <hr/>
   </div>


FSCLHeader.propTypes = {
   viewName: PropTypes.string.isRequired,
   saveable: PropTypes.bool.isRequired,
   deletable: PropTypes.bool.isRequired,
   project: PropTypes.string.isRequired,
   onNew: PropTypes.func.isRequired,
   onSave: PropTypes.func.isRequired,
   onDelete: PropTypes.func.isRequired
};

export default FSCLHeader;
