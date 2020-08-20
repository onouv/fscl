import { urls } from '../../lib/api';

const GetProjectListService = (dispatchResult, dispatchError) => {

   const url = urls.projects.projects();

   fetch(url)
      .then(res => res.json())
      .then(res => {
         if (res.errMsg) {
            throw res.error(res.errMsg);
         }
         else {
            dispatchResult(res.projects);
         }
      })
      .catch(error => {
         dispatchError(error);
      });
};

export default GetProjectListService;
