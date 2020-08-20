import React from 'react';

import EntitySelectLineUI from '../../lib/ui/EntitySelectLineUI'
import EntitySectionUI from '../../lib/ui/EntitySectionUI'

const ComponentSelectLineUI = ({
   index,
   component,
   onMarkChange,
   onFoldingControlEvent }) =>
   <EntitySelectLineUI
      key={index}
      index={index}
      entityDisplayed={component}
      ContentComponent={EntitySectionUI}
      onMarkChange={onMarkChange}
      onFoldingControlEvent={() => onFoldingControlEvent(component)}
   />


export default ComponentSelectLineUI
