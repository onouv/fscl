import React from 'react';
import { OverlayTrigger, Popover, Button } from 'react-bootstrap';
import fscl from '../lib'

const ErrorFlag = ({error}) => {

   const message = () => error.message;

   if (error) {

      const popError = (
         <Popover id="popover-error">
            <Popover.Content>
               {`${error.name}: ${error.message}`}
            </Popover.Content>
         </Popover>
      );

      return (
         <OverlayTrigger trigger="click" placement="top" overlay={popError}>
            <Button size="sm" variant="danger" onClick={message}>
               Error
            </Button>
         </OverlayTrigger>
      )
   } else {
      return null;
   }
}

ErrorFlag.propTypes = {
   error: fscl.propTypes.error
};

export default ErrorFlag;
