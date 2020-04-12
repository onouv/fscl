import { urls } from '../../lib/api';

const DeleteProject = (project) => {
   const url = urls.projects.delete(project.code);
   const init = { method: 'DELETE' };
   return new Promise((resolve, reject) => {
      fetch(url, init)
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

export default DeleteProject;
