import { initHttp, clientSessionId } from './service-helpers'
import { timeouts } from '../api'
import fscl from '../lib'
import { cloneDeep } from 'lodash'
import Error from '../Error'


/**
 * Takes a url and a list of entities. For all entities marked as changed,
 * retrieves a list of new entity ids from the url amd dispatches them
 * together with a deep copy of the entity list to dispatchResult.
 *
 * Any errors while requesting ids for a child of
 * one of the entities in the list will be marked in the dispatched list.
 *
 * The first error while requesting id for a new root will be dispatched to
 * dispatchRootFail.
 *
 * @param  {[type]} url                     [description]
 * @param  {[type]} entities                [description]
 * @param  {f(entities, newIds)} [dispatchResult=f=>f]
 * @param  {[type]} [dispatchRootFail=f=>f] f(error)
 * @return {[type]}                         [description]
 */
const requestNewEntityIds = (
   url,
   entities,
   dispatchResult=f=>f,
   dispatchRootFail=f=>f ) => {

   const entitiesForState = cloneDeep(entities)
   const newIds = []

   const promises = []
   const marked = entities.filter(e => e.marked === true)
   let actualUrl
   if(marked.length > 0) {
      marked.forEach(parent => {
         actualUrl = `${url}/${parent.self.entity}`
         promises.push(fetchNewEntityId(actualUrl, parent))
      })
   } else {
      actualUrl = url
      promises.push(fetchNewEntityId(actualUrl))
   }

   Promise.all(promises)
      .then(results => {
         results.forEach(res => {
            newIds.push(res.newId)
         })

         dispatchResult(entitiesForState ,newIds)
      })
      .catch(result => {
         if(fscl.variable.exists(result.parent)) {
            const failedIdx = entitiesForState.findIndex(e => (
               (e.self.entity === result.parent.self.entity) &&
               (e.self.project === result.parent.self.project)
            ))
            entitiesForState[failedIdx].error = result.error
            dispatchResult(entitiesForState)
         } else {
            dispatchRootFail(result.error)
         }
      })
}

const fetchNewEntityId = (url, parent) => {

   const requestData = {
      timeoutSeconds: timeouts.projects.cachedCodes.timeoutSeconds,
	   clientId: clientSessionId.toString()
   };

   return new Promise((resolve, reject) => {
      fetch(url, initHttp('POST', requestData))
         .then(response => {

            // no network error or other low-level error...

            return response.json()
         })
         .then(result => {

            if(fscl.variable.exists(result.error)) {

               // application error (e.g. 404, 500, etc.) ...

               reject({
                  error: new Error(
                     "Problem loading new Id, server message is ",
                     result.error),
                  parent: parent,
                  newId: null
               })

            } else {

               // we're good ...

               resolve({
                  error: null,
                  parent: result.parent,
                  newId: {
                     project: result.project,
                     entity: result.entity
                  }
               })
            }
         })
         .catch(error => {

            // network error or other low-level error ...

            reject({
               error: error,
               newId: null,
               parent: parent
            })
         })
   })
}

export default {
   requestNewIds: requestNewEntityIds
}
