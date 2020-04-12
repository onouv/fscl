import uuid from 'uuid';
import { isEqual, cloneDeep } from 'lodash';
import fscl from '../lib.js'


// this generates once per "session", i.e. only valid until the
// current instance of the Projects page is left.
export const clientSessionId = uuid.v4();

export const initHttp = (method, payload) => {

   const http = {
      method: method,
      headers: {
      'Content-Type': 'application/json'
      }
   }

   if(fscl.variable.exists(payload)) {
      http.body = JSON.stringify(payload)
   }

   return http
}

/**
 * Handle the data exchange with server by callig the handleFetch function
 * provided, then awaiting all fetch calls to complete and handle errors
 *
 * @param  {array} entitiesFromState [description]
 * @param  {function} dispatch        a react dispatch function to call when all
 *                                    results are successfully fetched
 * @param  {function} handleFetch     a function expected to call fetch and
 *                                    return an array of promises. Each promise
 *                                    is expected to either
 *                                    a) resolve with an entity which has its
 *                                       error field set to null or
 *                                    b) reject with an entity which has its
 *                                       error field set to an Error object
 * @return {[type]}
 */
export const handleEntityExchange = (entitiesFromState, dispatch, handleFetch) => {

   const promises = handleFetch(entitiesFromState)

   let entitiesForState = cloneDeep(entitiesFromState)

   Promise.all(promises)
      .then(responsesHandled => {

         // all good, dispatch them as they are...
         dispatch(responsesHandled)
      })
      .catch(failedEntity => {

         // one entity had failure, so dispatch the whole bunch to state only
         // after inserting the received error message into the concerned one
         const failedIdx = entitiesForState.findIndex(e => {
            return isEqual(failedEntity.self, e.self)
         })

         entitiesForState[failedIdx].error = failedEntity.error

         // create an object of similar structure to what the server would
         // have generated in OK case
         const responsesFailed = [{
            entities: entitiesForState
         }]
         dispatch(responsesFailed)
      }
   );
}

export default {
   initHttp: initHttp,
   handleEntityExchange: handleEntityExchange
}
