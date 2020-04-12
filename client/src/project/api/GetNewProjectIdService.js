import { urls, timeouts } from '../../lib/api';
import { clientSessionId } from '../../lib/api//service-helpers';

/**
 * Service for retrieving a freshly generated project code from the server.
 *
 * @param {[type]} dispatchResult callback for good case with string parameter
 *                                for the retrieved code
 * @param {[type]} dispatchError  callback for error case with string parameter
 *                                for the error message
 */
const GetNewProjectIdService = (dispatchResult, dispatchError) => {

   const url = urls.projects.newId();
   const data = {
      timeoutSeconds: timeouts.projects.cachedCodes.timeoutSeconds,
	   clientId: clientSessionId.toString()
   };
   const init = {
      method: 'POST',
      body: JSON.stringify(data),
      headers: {
         'Content-Type': 'application/json'
      }
   };

   fetch(url, init)
      .then(res => res.json())
      .then(res => {
         if (res.errMsg) {
            throw res.errMsg;
         }
         else {
            dispatchResult(res.code);
         }
      })
      .catch(error => {
         dispatchError(error);
      });
};

export default GetNewProjectIdService;
