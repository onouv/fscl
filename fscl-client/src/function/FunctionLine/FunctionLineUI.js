import React from 'react'
import PropTypes from 'prop-types'
import { Row } from 'react-bootstrap';

import LinkSectionConstrainedLocation from '../ui/LinkSectionConstrainedLocationUI'
import ControlSection from '../../lib/ui/ControlSectionUI'
import ContentSection from '../../lib/ui/ContentSectionUI'
import fscl from '../../lib/lib'


const FunctionLineUI = ({
   index,
   entityDisplayed,
   onFlagChange=f=>f,
   onNameChange=f=>f,
   onDescriptionChange=f=>f,
   onFLinked=f=>f,
   onSLinked=f=>f,
   onCLinked=f=>f }) => {

   let errorPending = false
   if(entityDisplayed.error != null) {
      errorPending = true
   }

   const elementId = () => `function-line${entityDisplayed.self.entity}`

   return (
      <Row id={elementId()}>
         <LinkSectionConstrainedLocation
            entity={entityDisplayed}
            active={!errorPending}
            hierarchyActive={entityDisplayed.foldingEnabled}
            entityHomeCode="F"
            onFLinkClicked={() => onFLinked(entityDisplayed)}
         />
         <ControlSection
            code={entityDisplayed.self}
            changed={entityDisplayed.changed}
            marked={entityDisplayed.marked}
            error={entityDisplayed.error}
            onFlagChange={onFlagChange}
         />
         <ContentSection
            code={entityDisplayed.self}
            {...entityDisplayed.content}
            onNameChange={onNameChange}
            onDescriptionChange={onDescriptionChange}
         />
   </Row>
   )
}


FunctionLineUI.propTypes = {
   index: PropTypes.number.isRequired,
   entityDisplayed: fscl.propTypes.entity,
   onFlagChange: PropTypes.func.isRequired,
   onNameChange: PropTypes.func.isRequired,
   onDescriptionChange: PropTypes.func.isRequired,
   onFLinked: PropTypes.func,
   onSLinked: PropTypes.func,
   onCLinked: PropTypes.func
}

export default FunctionLineUI
