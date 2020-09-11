import React from 'react';
import { Button } from 'react-bootstrap';
import fscl from '../../lib/lib'
import PropTypes from 'prop-types';

const LinkButton =({
   mark,
   project,
   enabled,
   isEntityHome = false,
   onClick}) => {
      const variant = isEntityHome ? "primary" : "secondary"
      const buttonId = `${mark}${project}`
      if(enabled) {
         return (
            <Button
               id={buttonId}  // to enable Selenium testing
               variant={variant}
               size="sm"
               onClick={() => {onClick(project)}}>
               {mark}
            </Button>
         )
      } else {
         return (
            <Button
               id={buttonId} // to enable Selenium testing
               variant={variant}
               size="sm"
               disabled>
               {mark}
            </Button>
         )
      }
}

LinkButton.propTypes = {
   mark: PropTypes.string.isRequired,
   project: fscl.propTypes.string,
   enabled: PropTypes.bool.isRequired,
   onClick: PropTypes.func.isRequired,
   isEntityHome: PropTypes.bool
}

export default LinkButton;
