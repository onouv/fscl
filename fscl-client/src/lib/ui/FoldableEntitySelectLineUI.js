import React from 'react';
import { Row, Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import fscl from '../lib'

import Marker from './MarkerUI.js'
import EntitySectionUI from '../../lib/ui/EntitySectionUI'
import LinkButton from './LinkButtonUI'
import ErrorFlag from './ErrorFlagUI'


const FoldableEntitySelectLineUI = ({
   index,
   entityDisplayed,
   onFoldingControlEvent,
   onMarkChange }) => {

      const unfolded = entityDisplayed.unfolded ? entityDisplayed.unfolded : false
      const mark =  unfolded ? "<" : ">"

      return (
         <Row>
            <Col lg={1}>
               <Row>
                  <Col lg={4}>
                     <LinkButton
                        mark={mark}
                        entity={entityDisplayed}
                        enabled={entityDisplayed.foldingEnabled}
                        isEntityHome={true}
                        onClick={() => onFoldingControlEvent(entityDisplayed)}
                     />
                  </Col>
                  <Col lg={4}>
                     <Marker
                        entity={entityDisplayed}
                        onMarkChange={onMarkChange}
                     />
                  </Col>
                  <Col lg={4}>
                     <ErrorFlag error={entityDisplayed.error}/>
                  </Col>
               </Row>
            </Col>
            <Col lg={11}>
               <EntitySectionUI entity={entityDisplayed}/>
            </Col>
         </Row>
      )
}

FoldableEntitySelectLineUI.propTypes = {
   index: PropTypes.number.isRequired,
   entityDisplayed: fscl.propTypes.entity,
   ContentComponent: PropTypes.func.isRequired,
   onFoldingControlEvent: PropTypes.func.isRequired,
   onMarkChange: PropTypes.func.isRequired
}


export default FoldableEntitySelectLineUI
