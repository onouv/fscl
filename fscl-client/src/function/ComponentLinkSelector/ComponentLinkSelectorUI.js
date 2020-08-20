import React from 'react';
import ReactModal from 'react-modal'

import LinkSelectorUI from '../../lib/link/LinkSelectorUI'

const ComponentLinkSelectorUI = ({
   components,
   onSelect,
   selectEnabled=false,
   onMarkChange,
   onFoldingControlEvent }) => {

   return (
      <ReactModal isOpen={true}>
         <LinkSelectorUI
            category="Components"
            entities={components}
            onSelect={() => onSelect(components)}
            selectEnabled={selectEnabled}
            onMarkChange={onMarkChange}
            onFoldingControlEvent={onFoldingControlEvent}
         />
      </ReactModal>
   )
}

export default ComponentLinkSelectorUI
