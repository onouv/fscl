import React from 'react';
import { Row, Col, Form } from 'react-bootstrap';
import PropTypes from 'prop-types';

import fscl from '../lib'

const EntitySectionUI = ({entity}) =>
   <div>
         <Row>
            <Col lg={2}>
               <Form.Control
                  disabled
                  type="text"
                  value={`${entity.self.project} | ${entity.self.entity}`}
               />
            </Col>
            <Col lg={4}>
               <Form.Control
                  disabled
                  type="text"
                  value={`${entity.content.name}`}
               />
            </Col>
            <Col lg={6}>
               <Form.Control
                  disabled
                  type="text"
                  value={`${entity.content.description}`}
               />
            </Col>
         </Row>
   </div>

EntitySectionUI.propTypes = {
   entity: PropTypes.shape({
      self: fscl.propTypes.code,
      content: fscl.propTypes.content
   })
}

export default EntitySectionUI
