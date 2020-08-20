import FunctionLinkButton from './FunctionLinkButtonUI'
import ComponentsForFunctionLinkButton from '../ui/ComponentsForFunctionLinkButtonUI'
import LocationLinkButton from '../../lib/ui/location/LocationLinkButtonUI'
import SystemLinkButton from '../../lib/ui/system/SystemLinkButtonUI'

import React from 'react'
import { ButtonGroup, Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import fscl from '../../lib/lib'

const LinkSectionConstrainedLocation =
   ({
      entity,
      active,
      hierarchyActive,
      entityHomeCode,
      onFLinkClicked=f=>f
   }) => {

   return (
      active
      ?  <Col xs md lg={1}>
            <ButtonGroup name="links">
               <FunctionLinkButton
                  entity={entity}
                  enabled={hierarchyActive}
                  fLinkHandler={onFLinkClicked}
                  entityHomeCode={entityHomeCode}/>
               <SystemLinkButton
                  entity={entity}
                  enabled={false}
                  entityHomeCode={entityHomeCode}/>
               <ComponentsForFunctionLinkButton
                  entity={entity}
                  enabled={true}
                  entityHomeCode={entityHomeCode}/>
               <LocationLinkButton
                  entity={entity}
                  enabled={false}
                  entityHomeCode={entityHomeCode}/>
            </ButtonGroup>
         </Col>
      :  <Col xs md lg={1}>
            <ButtonGroup name="links">
               <FunctionLinkButton
                  entity={entity}
                  enabled={false}
                  entityHomeCode={entityHomeCode}/>
               <SystemLinkButton
                  entity={entity}
                  enabled={false}
                  entityHomeCode={entityHomeCode}/>
               <ComponentsForFunctionLinkButton
                  entity={entity}
                  enabled={false}
                  entityHomeCode={entityHomeCode}/>
               <LocationLinkButton
                  entity={entity}
                  enabled={false}
                  entityHomeCode={entityHomeCode}/>
            </ButtonGroup>
         </Col>
   )
}

LinkSectionConstrainedLocation.propTypes = {
   entity: fscl.propTypes.entity,
   active: PropTypes.bool,
   hierarchyActive: PropTypes.bool,
   onFLinkClicked: PropTypes.func,
   entityHomeCode: PropTypes.string
}

export default LinkSectionConstrainedLocation
