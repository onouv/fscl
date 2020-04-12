import { cloneDeep } from 'lodash';
import { clientSessionId } from '../../lib/api/service-helpers'
import formats from '../../lib/domain/CodeFormats'

export const handleExchange = (projectsFromState, dispatch, handleFetch) => {

   const promises = handleFetch(projectsFromState);

   let projectsForState = cloneDeep(projectsFromState);

   Promise.all(promises)
      .then(projectsSaved => {

         projectsSaved.forEach(p=> {
            console.log(`handled code=${p.code}`);
         })

         // all good, dispatch them as they are...
         dispatch(projectsForState);
      })
      .catch(failedProject => {
         console.error(`\
            failed handling project ${failedProject.code},\
            message:${failedProject.error}`
         );

         // one project had failure, so dispatch the whole bunch to state only
         // after inserting the received error message into the concerned project
         const failedIdx = projectsForState.findIndex(proj => {
            return (failedProject.code === proj.code)
         });

         projectsForState[failedIdx].error = failedProject.error;
         dispatch(projectsForState);
      }
   );
}


/**
 * creates an init block for a fetch call to save projects. It sets the
 * HTTP method, provides the given project as a JSON payload and sets the
 * HTTP headers.
 *
 * @param  {[type]} project project object to be saved
 * @param  {[type]} method  HTTP method term ('POST' | 'PUT')
 * @return {[type]}         initialized init object for the fetch call
 */
export function initSave(project, method) {
   const payload = {
      code: project.code,
      name: project.name,
      description: project.description,
      clientId: clientSessionId,
      functionFormat: formats.functionFormat,
      systemFormat: formats.systemFormat,
      componentFormat: formats.componentFormat,
      locationFormat: formats.locationFormat
   };

   return {
      method: method,
      body: JSON.stringify(payload),
      headers: {
         'Content-Type': 'application/json'
      }
   };
}
