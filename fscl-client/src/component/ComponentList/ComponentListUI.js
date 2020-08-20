import React from 'react'
import { useDispatch } from 'react-redux'
import { Container, Alert } from 'react-bootstrap'
import PropTypes from 'prop-types'
import fscl from '../../lib/lib'

import LoadComponentsAsyncAction from '../redux/LoadComponentsAsyncAction'
import FSCLHeader  from '../../lib/ui/FSCLHeaderUI'
import ErrorDisplay from '../../lib/ui/ErrorDisplay'
import ProjectLine from '../../lib/ui/project/ProjectLineUI'
import ViewNames from '../../lib/ui/ViewNames'
import ItemHeader from '../../lib/ui/ItemHeader'
import ComponentLine from '../ComponentLine/ComponentLine'


const ComponentListUI = ({
   components=[],
   projectCode="",
   saveable,
   deletable,
   linkable,
   error=null,
   onNew=f=>f,
   onSave=f=>f,
   onDelete=f=>f }) => {

   const dispatchFromStore = useDispatch();

   React.useEffect(() => {
         LoadComponentsAsyncAction(projectCode)(dispatchFromStore)
      },
      // eslint-disable-next-line
      [] // provide empty array to avoid dispatching after component update
   );

   const headers= () =>
      <div>
         <FSCLHeader
            viewName={ViewNames.COMPONENTS}
            saveable={saveable}
            deletable={deletable}
            linkable={linkable}
            project={projectCode}
            onNew={() => onNew(components)}
            onSave={() => onSave(components)}
            onDelete={() => onDelete(components)}
         />
         <ProjectLine project={projectCode}/>
      </div>

   if(error == null) {

      // ...no error

      if(components.length === 0) {

         // ...  no entities returned from server

         return (
            <div>
               {headers()}
               <Container fluid>
                  <Alert variant='primary'>No components found</Alert>
               </Container>
            </div>
         )
      } else {

         // ...server returned a list of entities, so we show it

         return (
            <div>
               {headers()}
               <ItemHeader />
               <Container fluid> {
                     components.map((f, i) =>
                        //<FunctionLine key={i} {...f}
                        <ComponentLine key={i} index={i} />)
                  }
               </Container>
            </div>
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
};

ComponentListUI.propTypes = {
   components: PropTypes.arrayOf(PropTypes.shape),
   projectCode: PropTypes.string.isRequired,
   saveable: PropTypes.bool.isRequired,
   deletable: PropTypes.bool.isRequired,
   error: fscl.propTypes.error,
   onNew: PropTypes.func.isRequired,
   onSave: PropTypes.func.isRequired,
   onDelete: PropTypes.func.isRequired
}

export default ComponentListUI
