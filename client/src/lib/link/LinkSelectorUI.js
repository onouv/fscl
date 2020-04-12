import React from 'react'
import { Container, Alert } from 'react-bootstrap'

import ModalHeader from '../../lib/modals/ModalHeaderUI'
import FoldableEntitySelectLineUI from '../../lib/ui/FoldableEntitySelectLineUI'

const LinkSelectorUI = ({
   category,
   entities,
   onSelect,
   selectEnabled,
   onMarkChange,
   onFoldingControlEvent }) => {

   const modalCaption = `Select ${category} for Linking`
   const alertCaption = `No ${category} selectable for Linking...`

   if(entities.length > 0) {
      return(
         <Container fluid>
            <ModalHeader
               title={modalCaption}
               onConfirm={onSelect}
               confirmEnabled={selectEnabled}
               onConfirmCaption="Select"
            />
            { entities.map((entity, i) =>
               <FoldableEntitySelectLineUI
                  key={i}
                  index={i}
                  entityDisplayed={entity}
                  onMarkChange={onMarkChange}
                  onFoldingControlEvent={onFoldingControlEvent}
               />
               )
            }
         </Container>
      )
   } else {
      return(
         <Container fluid>
            <ModalHeader
               title={modalCaption}
               onConfirm={onSelect}
               confirmEnabled={selectEnabled}
               onConfirmCaption="Select"
            />
            <Alert variant='primary'>{alertCaption}</Alert>
         </Container>
      )
   }
}

export default LinkSelectorUI
