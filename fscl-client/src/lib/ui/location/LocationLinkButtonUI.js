import React from 'react';
import PropTypes from 'prop-types';
import { useHistory } from "react-router-dom";

import LinkButton from '../LinkButtonUI.js'
import links from '../../links.js'
import fscl from '../../lib'


const LocationLinkButton = ({
   entity,
   enabled,
   entityHomeCode,
   fLinkHandler }) => {

   const history = useHistory();
   const route = (entity) => {
      history.push(links.locations(entity));
   }
   const handler = (fLinkHandler != null) ? fLinkHandler : route
   const isEntityHome = (entityHomeCode === "L") ? true: false

   return (
      <LinkButton
         mark="L"
         entity={entity}
         enabled={enabled}
         isEntityHome={isEntityHome}
         route={handler}
         onClick={handler} />
   )
}

LocationLinkButton.propTypes = {
   entity: fscl.propTypes.entity,
   enabled: PropTypes.bool,
   entityHomeCode: PropTypes.string,
   fLinkHandler: PropTypes.func
}

export default LocationLinkButton;
