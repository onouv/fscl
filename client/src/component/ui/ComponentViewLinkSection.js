import React from 'react';
import { ButtonGroup, Col } from 'react-bootstrap';

import fscl from '../../lib/lib'
import FunctionsForComponentLinkButton from '../ui/FunctionsForComponentLinkButtonUI'
import ComponentLinkButton from '../../lib/ui/component/ComponentLinkButtonUI'
import LocationLinkButton from '../../lib/ui/location/LocationLinkButtonUI'
import SystemLinkButton from '../../lib/ui/system/SystemLinkButtonUI'

import PropTypes from 'prop-types';

const ComponentViewLinkSection =
   ({
      entity,
      active,
      hierarchyActive,
      onCLinkClicked=f=>f
   }) => {

      const entityHomeCode="C"

      return (
         active
         ?  <Col xs md lg={1}>
               <ButtonGroup name="links">
                  <FunctionsForComponentLinkButton
                     entity={entity}
                     enabled={true}
                     entityHomeCode={entityHomeCode}/>
                  <SystemLinkButton
                     entity={entity}
                     enabled={false}
                     entityHomeCode={entityHomeCode}/>
                  <ComponentLinkButton
                     entity={entity}
                     enabled={hierarchyActive}
                     entityHomeCode={entityHomeCode}
                     CLinkHandler={onCLinkClicked}/>
                  <LocationLinkButton
                     entity={entity}
                     enabled={false}
                     entityHomeCode={entityHomeCode}/>
               </ButtonGroup>
            </Col>
         :  <Col xs md lg={1}>
               <ButtonGroup name="links">
                  <FunctionsForComponentLinkButton entity={entity} enabled={false}/>
                  <SystemLinkButton entity={entity} enabled={false}/>
                  <ComponentLinkButton entity={entity} enabled={false}/>
                  <LocationLinkButton entity={entity} enabled={false}/>
               </ButtonGroup>
            </Col>
      )
};

ComponentViewLinkSection.propTypes = {
   entity: fscl.propTypes.entity,
   active: PropTypes.bool,
   hierarchyActive: PropTypes.bool,
   onFLinkClicked: PropTypes.func
}

export default ComponentViewLinkSection;
