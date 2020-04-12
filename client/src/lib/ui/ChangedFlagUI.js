import React from 'react';
import { Badge } from 'react-bootstrap'
import PropTypes from 'prop-types'

const ChangedFlag = ({
   changed=false
}) => {
   if(changed) {
      return (
         <Badge variant="info">C</Badge>
      )
   } else {
      return (
         <Badge variant="secondary">O</Badge>
      )
   }
}

ChangedFlag.propTypes = {
   changed: PropTypes.bool.isRequired
}

export default ChangedFlag
