import deleteProject from './DeleteProject.api';
import { handleExchange } from './service-helpers';

/**
 * This function runs through the project list and decides for each project in
 * the list:  If the project has the changed flag set and is NOT marked
 * pristine, it will be DELETED.
 *
 * An error in any one call will be flagged in the associated project.
 *
 * A new list of all remaining projects with their error status will be
 * dispatched through the callback.
 *
 * @param  {Array}      [projectsFromState=[]] the project list
 * @param  {[function]} dispatch               callback for dispatching an
 *                                             updated copy of the project list
 * @return {[type]}                            void
 */
export const deleteProjectService = (projectsFromState=[], dispatch) => {
   handleExchange(projectsFromState, dispatch, projects => {
      const promises = [];
      projects.forEach(project => {
         if(project.marked) {
            if(!project.pristine) {
               promises.push(deleteProject(project));
            }
         }
      })
      return promises;
   });
};

export default deleteProjectService;
