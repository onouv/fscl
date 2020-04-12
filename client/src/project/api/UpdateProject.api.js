import { urls } from '../../lib/api';
import { initSave } from './service-helpers';

function UpdateProject(project) {
   const url = urls.projects.update(project.code);
   return new Promise((resolve, reject) => {
      fetch(url, initSave(project, 'PUT'))
         .then(res => res.json())
         .then(res => {
            if (res.errMsg) {
               project.error=res.errMsg;
               reject(project);
            }
            else {
               resolve(project);
            }
         })
         .catch(error => {
            project.error=error.message;
            reject(project);
         });
   })
}


export default UpdateProject;
