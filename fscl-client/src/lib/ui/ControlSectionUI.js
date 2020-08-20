import React from 'react'
import { Row, Col, Form } from 'react-bootstrap'
import PropTypes from 'prop-types'

import fscl from '../lib'
import ChangedFlag from './ChangedFlagUI'
import MarkedFlag from './MarkedFlagUI'
import ErrorFlag from './ErrorFlagUI'

const ControlSection = ({
   code,
   changed=false,
   marked=false,
   error=null,
   onFlagChange=f=>f }) => {

   const handleFlagChange = handler => event => {
      const id = event.target.id;
      const projectEntitySeparatorIndex = id.indexOf('*')
      const project = id.slice(0, projectEntitySeparatorIndex)
      const entity = id.slice(projectEntitySeparatorIndex + 1, id.lastIndexOf('*'))
      const code = { project: project, entity: entity }
      handler(code);
   }

   function buildId(code, uid) {
      return `${code.project}*${code.entity}*${uid}`
   }

   return(
      <Col xs md lg={1}>
         <Form>
            <Form.Row>
               <Col>
                  <Row>
                     <Col>
                        <ChangedFlag changed={changed}/>
                     </Col>
                     <Col>
                        <MarkedFlag
                           code={code}
                           checked={marked}
                           onChange={handleFlagChange(onFlagChange)}
                           buildId={buildId}/>
                     </Col>
                     <Col>
                        <ErrorFlag error={error}/>
                     </Col>
                  </Row>
               </Col>
            </Form.Row>
         </Form>
      </Col>
   )
}


ControlSection.propTypes = {
   code: fscl.propTypes.code,
   changed: PropTypes.bool,
   error: fscl.propTypes.error,
   onFlagChange: PropTypes.func.isRequired
}

export default ControlSection;
