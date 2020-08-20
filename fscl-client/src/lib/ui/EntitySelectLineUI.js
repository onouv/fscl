import React from 'react';
import { Row, Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import fscl from '../lib'
import Marker from './MarkerUI.js'
import ErrorFlag from './ErrorFlagUI'

const EntitySelectLineUI = ({
   index,
   entityDisplayed,
   ContentComponent,
   onMarkChange
   }) => {

      return (
         <Row>
            <Col lg={1}>
               <Row>
                  <Col lg={6}>
                     <Marker
                        entity={entityDisplayed}
                        onMarkChange={onMarkChange}
                     />
                  </Col>
                  <Col lg={6}>
                     <ErrorFlag error={entityDisplayed.error}/>
                  </Col>
               </Row>
            </Col>
            <Col lg={11}>
               <ContentComponent entity={entityDisplayed}/>
            </Col>
         </Row>
      )
}


EntitySelectLineUI.propTypes = {
   index: PropTypes.number.isRequired,
   entityDisplayed: fscl.propTypes.entity,
   ContentComponent: PropTypes.func.isRequired,   
   onMarkChange: PropTypes.func.isRequired
}

export default EntitySelectLineUI
