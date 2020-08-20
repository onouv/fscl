import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch } from 'react-redux'
import { Container, Alert } from 'react-bootstrap'

// import helpers...
import fscl from '../../lib/lib'
import ViewNames from '../../lib/ui/ViewNames'

// ui components...
import ItemHeader from '../../lib/link/ItemHeaderUI'
import LinkHeader from '../../lib/link/LinkHeaderUI'
import FunctionLinkHeader from '../FunctionLinkHeader/FunctionLinkHeader'
import EntityDisplayLine from '../../lib/ui/EntityDisplayLineUI'
import EntitySectionUI from '../../lib/ui/EntitySectionUI'
import EntitySelectLineUI from '../../lib/ui/EntitySelectLineUI'
import ErrorDisplay from '../../lib/ui/ErrorDisplay'


// redux actions...
import LoadLinkedFunctionsAsyncAction from
   '../redux/LoadLinkedFunctionsAsyncAction'



const LinkedFunctionsListUI =({
   componentId,
   componentContent,
   functions,
   onMarkChange,
   onFoldingControlEvent,
   error=null  }) => {

   const project = componentId.project
   const sourceEntity = {
      self: componentId,
      content: componentContent
   }

   const ComponentSourceLine = () =>
      <EntityDisplayLine
         caption="Component "
         entity={sourceEntity}
         ContentComponent={EntitySectionUI} />

   const headers = () =>
      <div>
         <LinkHeader
            viewName={ViewNames.FUNCTIONS}
            project={project}
         />
         <ComponentSourceLine/>
         <FunctionLinkHeader
            caption="Linked Functions "
            sourceEntityId={componentId}
         />
         <ItemHeader/>
      </div>

   const dispatchFromStore = useDispatch()

   React.useEffect(() => {
      LoadLinkedFunctionsAsyncAction(componentId)
         (dispatchFromStore)
   },
      // eslint-disable-next-line
     [] // provide empty array to avoid dispatching after component update
   )

   if(error === null) {

      if(functions.length > 0) {
         return(
            <Container fluid>
               { headers() }
               { functions.map((func, i) =>
                     <EntitySelectLineUI
                        key={i}
                        index={i}
                        entityDisplayed={func}
                        ContentComponent={EntitySectionUI}
                        onMarkChange={onMarkChange}
                     />
                  )
               }
            </Container>
         )
      } else {
         return(
            <Container fluid>
               { headers() }
               <Alert variant='primary'>No Linked Functions...</Alert>
            </Container>
         )
      }
   } else {
      // ... there was an error, so we show it
      return (
         <div>
            {headers()}
            <ErrorDisplay error={error}/>
         </div>
      )
   }
}

LinkedFunctionsListUI.propTypes = {
   componentId: fscl.propTypes.code,
   componentContent: fscl.propTypes.content,
   functions: PropTypes.arrayOf(PropTypes.shape),
   onFoldingControlEvent: PropTypes.func.isRequired,
   onMarkChange: PropTypes.func.isRequired
}

export default LinkedFunctionsListUI
