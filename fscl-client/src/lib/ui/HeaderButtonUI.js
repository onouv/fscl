import React from 'react';
import { Button } from 'react-bootstrap';
import PropTypes from 'prop-types';


/*******************************************************************************
 * HeaderButton Component
 *
 */
const HeaderButton = ({
      enabled,
      caption,
      project="none",
      onClick=f=>f
   }) => {
      return (
         enabled
         ?  <Button size="sm" variant="secondary" onClick={onClick}>
               {caption}
            </Button>
         :  <Button size="sm" variant="secondary" disabled>
               {caption}
            </Button>
      );
}

HeaderButton.propTypes = {
   enabled: PropTypes.bool.isRequired,
   caption: PropTypes.string.isRequired,
   project: PropTypes.string,
   onClick: PropTypes.func
}

export default HeaderButton
