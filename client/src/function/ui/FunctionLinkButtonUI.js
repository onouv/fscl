import LinkButton from '../../lib/ui/LinkButtonUI'
import links from '../../lib/links'
import fscl from '../../lib/lib'
import PropTypes from 'prop-types';
import React from 'react';
import { useHistory } from "react-router-dom";

const FunctionLinkButton = ({
   entity,
   enabled,
   entityHomeCode,
   fLinkHandler=null }) => {

   const history = useHistory();

   const route = (entity) => {
      history.push(links.functions.url(entity.self));
   }

   const handler = (fLinkHandler != null) ? fLinkHandler : route

   const isEntityHome = (entityHomeCode === "F") ? true: false

   return (
      <LinkButton
         mark="F"
         entity={entity}
         enabled={enabled}
         isEntityHome={isEntityHome}
         onClick={handler}
      />
   )

}

FunctionLinkButton.propTypes = {
   entity: fscl.propTypes.entity,
   enabled: PropTypes.bool.isRequired,
   fLinkHandler: PropTypes.func,
   entityHomeCode: PropTypes.string
}

export default FunctionLinkButton;
