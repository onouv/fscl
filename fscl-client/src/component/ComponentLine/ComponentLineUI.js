import React from 'react'
import PropTypes from 'prop-types'
import { Row } from 'react-bootstrap';

import ComponentViewLinkSection from '../ui/ComponentViewLinkSection'
import ControlSection from '../../lib/ui/ControlSectionUI'
import ContentSection from '../../lib/ui/ContentSectionUI'
import fscl from '../../lib/lib'


const ComponentLineUI = ({
   index,
   entityDisplayed,
   project,
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

   return (
      <Row>
         <ComponentViewLinkSection
            entity={entityDisplayed}
            active={!errorPending}
            hierarchyActive={entityDisplayed.foldingEnabled}
            onCLinkClicked={() => onCLinked(entityDisplayed)}
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



ComponentLineUI.propTypes = {
   index: PropTypes.number.isRequired,
   entityDisplayed: fscl.propTypes.entity,
   onFlagChange: PropTypes.func.isRequired,
   onNameChange: PropTypes.func.isRequired,
   onDescriptionChange: PropTypes.func.isRequired,
   onFLinked: PropTypes.func,
   onSLinked: PropTypes.func,
   onCLinked: PropTypes.func
}

export default ComponentLineUI
