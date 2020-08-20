import React from 'react';
import PropTypes from 'prop-types'
import fscl from '../lib'

const MarkedFlag = ({
   code,
   checked=false,
   onChange=f=>f,
   buildId=f=>f }) => {

   const idStr = buildId(code, "marked")

   return (
      <input
         type="checkbox"
         id={idStr}
         onChange={onChange}
         checked={checked}
      />
   );
}

MarkedFlag.propTypes = {
   code: fscl.propTypes.code,
   checked: PropTypes.bool.isRequired
};

export default MarkedFlag;
