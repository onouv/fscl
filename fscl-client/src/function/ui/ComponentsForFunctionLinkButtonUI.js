import LinkButton from '../../lib/ui/LinkButtonUI'
import links from '../../lib/links'
import fscl from '../../lib/lib'
import PropTypes from 'prop-types';
import React from 'react';
import { useHistory } from "react-router-dom";

const ComponentsForFunctionLinkButton = ({
   entity,
   enabled,
   entityHomeCode  }) => {

   const history = useHistory();

   const route = (entity) => {
      history.push(links.functions.components.url(entity.self));
   }
   
   const isEntityHome = (entityHomeCode === "F") ? true: false

   return (
      <LinkButton
         mark="C"
         entity={entity}
         enabled={enabled}
         isEntityHome={isEntityHome}
         onClick={route}
      />
   )

}

ComponentsForFunctionLinkButton.propTypes = {
   entity: fscl.propTypes.entity,
   enabled: PropTypes.bool.isRequired,
   fLinkHandler: PropTypes.func,
   entityHomeCode: PropTypes.string
}

export default ComponentsForFunctionLinkButton;
