import update from './UpdateProject.api';
import create from './CreateProject.api';
import { handleExchange } from './service-helpers';

/**
 * This function runs through the project list and decides for each project in
 * the list:  If the project only has the changed flag set, it will be PUT.
 * If in addition, the project is flagged as pristine, it will be POSTed.
 *
 * An error in any one call will be flagged in the associated project.
 *
 * A new list of all projects with their error status will be dispatched
 * through the callback.
 *
 * @param  {Array}  [projectsFromState=[]] the project list
 * @param  {[type]} dispatch               the callback for dispatching
 *                                         an updated copy of the project list
 * @return {[type]}                        void
 */
const SaveProjectsService = (projectsFromState=[], dispatch) => {

   handleExchange(projectsFromState, dispatch, projects => {
      const promises = [];
      projects.forEach(project => {
         if(project.changed) {
            if(project.pristine) {
               promises.push(create(project));
            } else {
               promises.push(update(project));
            }
         }
      });
      return promises;
   })
};

export default SaveProjectsService;
