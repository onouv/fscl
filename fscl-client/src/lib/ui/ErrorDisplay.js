import React from 'react'
import { Container, Alert } from 'react-bootstrap'
import Error from '../Error'

import fscl from '../../lib/lib'


const ErrorDisplay = ({error}) => {

   if(error != null) {

      const err = new Error(error.name, error.message);

      return (
         <Container fluid>
            <Alert variant='danger'>{`${err.toString()}`}</Alert>
            <Alert variant='dark'>You would normally just try to reload the page...</Alert>
         </Container>
      )

   } else {
      return (
         <Container fluid>
            <Alert variant='danger'>Sorry, an unspecified Error occurred</Alert>
            <Alert variant='light'>This is rather embarassing. Please let us know about it.</Alert>
         </Container>
      )
   }
}

ErrorDisplay.propTypes = {
   error: fscl.propTypes.error.isRequired
}

export default ErrorDisplay
