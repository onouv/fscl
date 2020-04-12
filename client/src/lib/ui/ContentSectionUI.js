import React from 'react'
import { Row, Col, Form } from 'react-bootstrap'
import PropTypes from 'prop-types'

import CodeField from './CodeFieldUI'
import fscl from '../lib.js'

export const ContentSection = ({
   code={},
   name="",
   onNameChange=f=>f,
   description="",
   onDescriptionChange=f=>f
}) => {

   const handleInputChange = handler => event => {
      const value = event.target.value;

      const id = event.target.id;
      const projectEntitySeparatorIndex = id.indexOf('*')
      const project = id.slice(0, projectEntitySeparatorIndex)
      const entity = id.slice(projectEntitySeparatorIndex + 1, id.lastIndexOf('*'))
      const code = { project: project, entity: entity }

      handler(code, value);
   };

   function buildId(code, uid) {
      return `${code.project}*${code.entity}*${uid}`
   }

   return (
      <Col xs md lg={10}>
      <Row>
         <Col xs md lg={2}>
            <CodeField
               code={code}
               projectHidden={true}
               />
         </Col>
         <Col xs md lg={4}>
            <Form.Control
               id={buildId(code, "name")}
               type="text"
               value={name}
               onChange={handleInputChange(onNameChange)}
            />
         </Col>
         <Col xs md lg={6}>
            <Form.Control
               id={buildId(code, "desc")}
               type="text"
               value={description}
               onChange={handleInputChange(onDescriptionChange)}
            />
         </Col>
      </Row>
   </Col>
   )
}

ContentSection.propTypes = {
   code: fscl.propTypes.code.isRequired,
   onCodeChange: PropTypes.func,
   name: PropTypes.string,
   onNameChange: PropTypes.func,
   description: PropTypes.string,
   onDescriptionChange: PropTypes.func
}

export default ContentSection
