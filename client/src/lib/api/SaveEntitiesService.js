import { handleEntityExchange } from './service-helpers'
import { clientSessionId } from './service-helpers'
import { initHttp } from './service-helpers'
import { cloneDeep } from 'lodash'
import fscl from '../lib'
import EntityCode from '../domain/EntityCode'

const UPDATE = 'PUT'
const CREATE = 'POST'

const SaveEntitiesService = (
   entities=[],
   url,
   dispatch ) => {

      handleEntityExchange(entities, dispatch, entitiesToFetch => {

         const promises = []
         entitiesToFetch.forEach(e => {
            if(e.changed) {
               if(e.pristine) {
                  promises.push(fetchEntity(
                     CREATE,
                     url(e.self),
                     createPayload(e),
                     e))
               } else {
                  promises.push(fetchEntity(
                     UPDATE,
                     url(e.self),
                     updatePayload(e),
                     e))
               }
            }
         })
         return promises
      })
   }

function fetchEntity(httpMethod, url, payload, entity) {

      const message = initHttp(httpMethod, payload)
      const errName = `failed saving entity ${EntityCode.fromFlat(entity.self)}`

      return new Promise((resolve, reject) => {
         fetch(url, message)
            .then(response => response.json())
            .then(result => {
               if(fscl.variable.exists(result.error)) {

                  // application error (e.g. 404, 500, etc.) ...

                  const errMessage = `server returned ${result.error}`
                  const error = new Error(errName, errMessage)

                  const failedEntity = cloneDeep(entity)
                  failedEntity.error = error

                  reject(failedEntity)
               } else {

                  // we're good ...
                  
                  const succeededEntity = cloneDeep(entity)
                  resolve(succeededEntity)
               }
            })
            .catch(err => {

               entity.error = err
               reject(entity)
            })
      })

}


/**
 * cherry-pick the correct elements of entity to create the required
 * data payload for the http request
 */
const createPayload = entity => ({
      parent: cloneDeep(entity.parent),
      name: entity.content.name,
      description:  entity.content.description,
      clientId: clientSessionId
   })


const updatePayload = entity => ({
      self: cloneDeep(entity.self),
      parent: cloneDeep(entity.parent),
      name: entity.content.name,
      description:  entity.content.description,
   })

export default SaveEntitiesService
