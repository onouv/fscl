import React from 'react';
import { Row, Col, Form } from 'react-bootstrap';
import PropTypes from 'prop-types'
import fscl from '../../lib/lib'

import LinkSection from './LinkSectionUI';
import MarkedFlag from '../../lib/ui/MarkedFlagUI';
import ChangedFlag from '../../lib/ui/ChangedFlagUI'
import ErrorFlag from '../../lib/ui/ErrorFlagUI';


/**
 * A UI component to display a single Project item in a line
 *
 */
export const ProjectLine = ({
   code="",
   name="",
   description="",
   changed=false,
   marked=false,
   error=false,
   onFlagChange=f=>f,
   onCodeChange=f=>f,
   onNameChange=f=>f,
   onDescriptionChange=f=>f}) => {

      const handleFlagChange = handler => event => {
         const id = event.target.id;
         const code = id.slice(0, id.lastIndexOf('#'));
         handler(code);
      }

      const handleInputChange = handler => event => {
         const value = event.target.value;
         const id = event.target.id;
         const code = id.slice(0, id.lastIndexOf('#'));
         handler(code, value);
      };

      function buildId(code, uid) {
         return `${code.project}#${uid}`
      }

      const codeDummy = {
         project: code,
         entity: ""
      }

   return (
      <Row>
         <Col xs md lg={1}>
            <LinkSection
               projectCode={code}
               active={!error}
            />
         </Col>
         <Col>
            <Form>
               <Form.Row>
                  <Col xs={1}>
                     <Row>
                        <Col>
                           <ChangedFlag changed={changed}/>
                        </Col>
                        <Col>
                           <MarkedFlag
                              code={codeDummy}
                              checked={marked}
                              onChange={handleFlagChange(onFlagChange)}
                              buildId={buildId}
                           />
                        </Col>
                        <Col>
                           <ErrorFlag error={error}/>
                        </Col>
                     </Row>
                  </Col>
                  <Col xs={2}>
                     <Form.Control
                        id={`${code}#code`}
                        type="text"
                        value={code}
                        onChange={handleInputChange(onCodeChange)}
                     />
                  </Col>
                  <Col xs={3}>
                     <Form.Control
                        id={`${code}#name`}
                        type="text"
                        value={name}
                        onChange={handleInputChange(onNameChange)}
                     />
                  </Col>
                  <Col xs={5}>
                     <Form.Control
                        id={`${code}#desc`}
                        type="text"
                        value={description}
                        onChange={handleInputChange(onDescriptionChange)}
                     />
                  </Col>
               </Form.Row>
            </Form>
         </Col>
      </Row>
   );
};

ProjectLine.propTypes = {
   code: PropTypes.string,
   name: PropTypes.string,
   description: PropTypes.string,
   error: fscl.propTypes.error
}
