import React from 'react';
import { Button } from 'react-bootstrap';
import fscl from '../lib'
import PropTypes from 'prop-types';


const LinkButton =({
   mark,
   entity,
   enabled,
   isEntityHome = false,
   onClick}) => {
      const variant = isEntityHome ? "primary" : "secondary"
      if(enabled) {
         return (
            <Button
               variant={variant}
               size="sm"
               onClick={() => {onClick(entity)}}>
               {mark}
            </Button>
         )
      } else {
         return (
            <Button
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
   entity: fscl.propTypes.entity,
   enabled: PropTypes.bool.isRequired,
   onClick: PropTypes.func.isRequired,
   isEntityHome: PropTypes.bool
}

export default LinkButton;
