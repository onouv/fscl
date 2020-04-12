import { connect } from 'react-redux';
import { ProjectList } from '../ui/ProjectList';
import { newProjectAsyncAction,
         saveProjectsAsyncAction,
         deleteProjectAsyncAction,
         changeProjectCodeAction,
         changeProjectNameAction,
         changeProjectDescriptionAction,
         toggleFlagProjectAction } from './project-actions';

const mapStateToProps = (state) => {
   return ({
      projects: state.projects.projects,
      saveable: state.projects.controls.saveable,
      deletable: state.projects.controls.deletable,
      error: state.projects.error
   })
}

/**
 * Container for ProjectList component
 * Maps the props and methods of ProjectList to state given by Provider
 *
 */
export const ProjectContainer = connect(
   mapStateToProps,
   dispatch => (
      {
         //User requests to create a new project
         onNew(projects) {            
            dispatch(newProjectAsyncAction());
         },

         // The projects changed or flagged by user shall be saved to server
         onSave(projects) {
            dispatch(saveProjectsAsyncAction(projects));
         },

         // The projects flagged by user shall be deleted from server
         onDelete(projects) {
            dispatch(deleteProjectAsyncAction(projects));
         },

         // A project has been flagged by the user
         onFlagChange(code) {
            dispatch(toggleFlagProjectAction(code));
         },

         // User has changed the value of the code field in the project
         // identified by code
         onCodeChange(code, change) {
            dispatch(changeProjectCodeAction(code, change));
         },

         // User has changed the value of the name field in the project
         // identified by code
         onNameChange(code, change) {
            dispatch(changeProjectNameAction(code, change));
         },

         // User has changed the value of the description field in the project
         // identified by code
         onDescriptionChange(code, change) {
            dispatch(changeProjectDescriptionAction(code, change));
         },
      }
   )
)(ProjectList);
