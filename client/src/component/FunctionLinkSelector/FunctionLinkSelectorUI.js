import React from 'react';
import ReactModal from 'react-modal'

import LinkSelectorUI from '../../lib/link/LinkSelectorUI'

const FunctionLinkSelectorUI = ({
   functions,
   onSelect,
   selectEnabled=false,
   onMarkChange,
   onFoldingControlEvent }) => {

   return (
      <ReactModal isOpen={true}>
         <LinkSelectorUI
            category="Functions"
            entities={functions}
            onSelect={() => onSelect(functions)}
            selectEnabled={selectEnabled}
            onMarkChange={onMarkChange}
            onFoldingControlEvent={onFoldingControlEvent}
         />
      </ReactModal>
   )
}

export default FunctionLinkSelectorUI
