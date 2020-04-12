import React from 'react';
import PropTypes from 'prop-types';

import { useHistory } from "react-router-dom";
import LinkButton from '../LinkButtonUI.js'
import links from '../../links.js'
import fscl from '../../lib'

const ComponentLinkButton = ({
   entity,
   enabled,
   entityHomeCode,
   CLinkHandler }) => {

   const history = useHistory();
   const route = (entity) => {
      history.push(links.components.url(entity));
   }
   const handler = (CLinkHandler != null) ? CLinkHandler : route
   const isEntityHome = (entityHomeCode === "C") ? true: false

   return (
      <LinkButton
         mark="C"
         entity={entity}
         enabled={enabled}
         isEntityHome={isEntityHome}
         onClick={handler}
      />
   )
}

ComponentLinkButton.propTypes = {
   entity: fscl.propTypes.entity,
   enabled: PropTypes.bool,
   entityHomeCode: PropTypes.string,
   CLinkHandler: PropTypes.func
}

export default ComponentLinkButton;
