import React from 'react'
import { useDispatch } from 'react-redux'
import { Container, Alert } from 'react-bootstrap'
import PropTypes from 'prop-types'
import fscl from '../../lib/lib'

import LoadFunctionsAsyncAction from '../redux/LoadFunctionsAsyncAction'
import FSCLHeader  from '../../lib/ui/FSCLHeaderUI'
import ErrorDisplay from '../../lib/ui/ErrorDisplay'
import ProjectLine from '../../lib/ui/project/ProjectLineUI'
import ViewNames from '../../lib/ui/ViewNames'
import ItemHeader from '../../lib/ui/ItemHeader'
import FunctionLine from '../FunctionLine/FunctionLine'

export const FunctionList = ({
   functions=[],
   projectCode="",
   saveable,
   deletable,
   linkable,
   error=null,
   onNew=f=>f,
   onSave=f=>f,
   onDelete=f=>f,
   onLink=f=>f }) => {

   const dispatchFromStore = useDispatch()

   const headers= () =>
      <div>
         <FSCLHeader
            viewName={ViewNames.FUNCTIONS}
            saveable={saveable}
            deletable={deletable}
            project={projectCode}
            onNew={() => onNew(functions)}
            onSave={() => onSave(functions)}
            onDelete={() => onDelete(functions)}
            onLink={() => onLink(projectCode)}
         />
         <ProjectLine project={projectCode}/>
      </div>

   React.useEffect(() => {
         LoadFunctionsAsyncAction(projectCode)(dispatchFromStore)
      },
      // eslint-disable-next-line
      [] // provide empty array to avoid dispatching after component update
   );

   if(error == null) {

      // ...no error

      if(functions.length === 0) {

         // ...  no entities returned from server

         return (
            <div>
               {headers()}
               <Container fluid>
                  <Alert id="no-functions-found-alert" variant='primary'>No functions found</Alert>
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
                     functions.map((f, i) =>
                        //<FunctionLine key={i} {...f}
                        <FunctionLine key={i} index={i} />)
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

FunctionList.propTypes = {
   functions: PropTypes.arrayOf(PropTypes.shape),
   projectCode: PropTypes.string.isRequired,
   saveable: PropTypes.bool.isRequired,
   deletable: PropTypes.bool.isRequired,
   error: fscl.propTypes.error,
   onNew: PropTypes.func.isRequired,
   onSave: PropTypes.func.isRequired,
   onDelete: PropTypes.func.isRequired,
}

export default FunctionList
