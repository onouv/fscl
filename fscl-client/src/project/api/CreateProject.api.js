import { urls } from '../../lib/api';
import { initSave } from './service-helpers';

function CreateProject(project) {
   const url = urls.projects.new();

   return new Promise((resolve, reject) => {
      fetch(url, initSave(project, 'POST'))
         .then(res => res.json())
         .then(res => {
            if (res.errMsg) {
               project.error=res.errMsg;
               reject(project);
            }
            else {
               //project.pristine = false; ... will be taken care of in reducer
               resolve(project);
            }
         })
         .catch(error => {
            project.error=error.message;
            reject(project);
         });
   })
}

export default CreateProject;
