import React from 'react';
import { Navbar, Row, Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import HeaderButtonGroup from '../../lib/ui/HeaderButtonGroupUI';
import BaseLinks from '../../lib/ui/BaseLinksUI';
import ViewNames from '../../lib/ui/ViewNames'

export const ProjectsHeader = ({
   saveable=false,
   deletable=false,
   onNew=f=>f,
   onSave=f=>f,
   onDelete=f=>f}
) =>
   <div>
      <Navbar collapseOnSelect sticky="top" expand="md">
         <Row>
            <Col xs md lg={2}>
               <Navbar.Brand href="/">FSCL</Navbar.Brand>
            </Col>
            <Col xs md lg={8}>
               <Navbar.Collapse id="responsive-navbar-nav">
                  <BaseLinks viewName={ViewNames.PROJECTS}/>
               </Navbar.Collapse>
            </Col>
            <Col xs md lg={2}>
               <HeaderButtonGroup                  
                  saveable={saveable}
                  deletable={deletable}
                  onNew={onNew}
                  onSave={onSave}
                  onDelete={onDelete}/>
            </Col>
         </Row>
      </Navbar>
      <hr/>
   </div>

ProjectsHeader.propTypes = {
   saveable: PropTypes.bool.isRequired,
   deletable: PropTypes.bool.isRequired,
   onNew: PropTypes.func.isRequired,
   onSave: PropTypes.func.isRequired,
   onDelete: PropTypes.func.isRequired,
};
