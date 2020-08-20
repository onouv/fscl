import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch } from 'react-redux'
import { Container, Alert } from 'react-bootstrap'

// import helpers...
import fscl from '../../lib/lib'
import ViewNames from '../../lib/ui/ViewNames'

// ui components...
import LinkHeader from '../../lib/link/LinkHeaderUI'
import ComponentLinkHeader from '../ComponentLinkHeader/ComponentLinkHeader'
import ItemHeader from '../../lib/link/ItemHeaderUI'
import EntityDisplayLine from '../../lib/ui/EntityDisplayLineUI'
import EntitySectionUI from '../../lib/ui/EntitySectionUI'
import EntitySelectLineUI from '../../lib/ui/EntitySelectLineUI'
import ErrorDisplay from '../../lib/ui/ErrorDisplay'


// import redux actions...
import LoadLinkedComponentsAsyncAction from
   '../redux/LoadLinkedComponentsAsyncAction'

const LinkedComponentsListUI = ({
   functionId,
   functionContent,
   components,
   onMarkChange,
   onFoldingControlEvent,
   error=null }) => {

      const project = functionId.project
      const funcEntity = {
         self: functionId,
         content: functionContent
      }

      const FunctionSourceLine = () =>
         <EntityDisplayLine
            caption="Function "
            entity={funcEntity}
            ContentComponent={EntitySectionUI} />

      const headers = () =>
         <div>
            <LinkHeader
               viewName={ViewNames.COMPONENTS}
               project={project}
            />
            <FunctionSourceLine/>
            <ComponentLinkHeader
               caption="Linked Components "
               sourceEntityId={functionId}
            />
            <ItemHeader/>
         </div>

      const dispatchFromStore = useDispatch()

      React.useEffect(() => {
         LoadLinkedComponentsAsyncAction(functionId)
            (dispatchFromStore)
      },
         // eslint-disable-next-line
        [] // provide empty array to avoid dispatching after component update
      )

      if(error === null) {

         if(components.length > 0) {
            return(
               <Container fluid>
                  { headers() }
                  { components.map((component, i) =>
                        <EntitySelectLineUI
                           key={i}
                           index={i}
                           entityDisplayed={component}
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
                  <Alert variant='primary'>No Linked Components...</Alert>
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

LinkedComponentsListUI.propTypes = {
   functionId: fscl.propTypes.code,
   functionContent: fscl.propTypes.content,
   components: PropTypes.arrayOf(PropTypes.shape),
   onFoldingControlEvent: PropTypes.func.isRequired,
   onMarkChange: PropTypes.func.isRequired
}


export default LinkedComponentsListUI
