import React from 'react';
import { useDispatch } from 'react-redux'
import { Container, Alert } from 'react-bootstrap';
import PropTypes from 'prop-types';

import ItemHeader from '../../lib/ui/ItemHeader'

import { ProjectsHeader } from './ProjectsHeader';
import { ProjectLine }  from './ProjectLine';
import { loadProjectsAsyncAction } from '../redux/project-actions';



export const ProjectList = (
   {
      projects=[],
      saveable=false,
      deletable=false,
      error=null,
      onNew=f=>f,
      onSave=f=>f,
      onDelete=f=>f,
      onFlagChange=f=>f,
      onCodeChange=f=>f,
      onNameChange=f=>f,
      onDescriptionChange=f=>f
   }) => {

   const dispatchFromStore = useDispatch();

   React.useEffect(() => {
         loadProjectsAsyncAction()(dispatchFromStore);
      },
      // eslint-disable-next-line
      [] // provide empty array to avoid dispatching after component update
   );


   return (
      <div>
         <ProjectsHeader
            saveable={saveable}
            deletable={deletable}
            onNew={onNew}
            onSave={() => onSave(projects)}
            onDelete={() => onDelete(projects)}
         />
         <ItemHeader/>
         <hr/>
         <Container fluid>
            {
               (error)
                  ? <Alert variant='danger'>'Error:'{error}</Alert>
                  : (projects.length === 0)
                     ?  <Alert variant='primary'>Nothing loaded</Alert>
                     :  projects.map((project, idx) =>
                           <ProjectLine
                              key={idx}
                              {...project}
                              onFlagChange={onFlagChange}
                              onCodeChange={onCodeChange}
                              onNameChange={onNameChange}
                              onDescriptionChange={onDescriptionChange}
                           />
                        )
            }
         </Container>
      </div>
   );
};

ProjectList.propTypes = {
   projects: PropTypes.array,
   saveable: PropTypes.bool,
   deletable: PropTypes.bool,

   // note these are the only parts supported by all browsers:
   error: PropTypes.shape({
      message: PropTypes.string,
      name: PropTypes.string
   })
};
