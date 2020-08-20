import React from 'react';
import { Form } from 'react-bootstrap'
import fscl from '../lib'
import PropTypes from 'prop-types';

const CodeField = ({
   code={},
   projectHidden=true

   }) => {

   const fieldValue = projectHidden ? code.entity: `${code.entity}(${code.project})`

   return(
      <Form.Control
         id={`${code.entity}#code`}
         type="text"
         value={fieldValue}         
         disabled
      />
   )
}

CodeField.propTypes = {
   code: fscl.propTypes.code.isRequired,
   projectHidden: PropTypes.bool.isRequired
}


export default CodeField;
