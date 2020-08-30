import { initHttp } from './service-helpers'
import { urls } from '../api'
import EntityCode from '../domain/EntityCode'
import fscl from '../lib'
import Error from '../Error'


function DeleteEntityService(entitiesToDelete, baseUrl, dispatch) {


   const deletions = []
   const deleteRequests = []
   entitiesToDelete.forEach(e => {
      if(e.marked) {

         deleteRequests.push(e)

         if(e.pristine)  {
            // server has never seen this one, so don't bother it,,,
            deletions.push(new Promise((resolve, reject) => {
               resolve({
                  self: e.self,
                  error: null
               })
            }))
         } else {
            // do the real thing with server...
            deletions.push(requestDelete(e))
         }
      }
   })

   Promise.all(deletions)
      .then(deleteResponses => {

         // all good, dispatch them as they are...         
         dispatch(deleteResponses, deleteRequests)
      })
      .catch(failedResponse => {

         const responses = [ failedResponse ]
         dispatch(responses, deleteRequests)
      })

   function requestDelete(entity) {

      const url = `${baseUrl}${urls.entities.partial(entity.self)}`

      return new Promise((resolve, reject) => {

         const errName = `failed deleting entity ${EntityCode.fromFlat(entity.self)}`

         fetch(url,  initHttp('DELETE'))
            .then(response => response.json())
            .then(result => {

               function illStructuredDataError() {
                  const errMessage = 'server returned ill-structured data'
                  return new Error(errName, errMessage)
               }

               if(fscl.variable.exists(result.error)) {

                  // application error (e.g. 404, 500, etc.) ...

                  const errMessage = `server returned ${result.error}`
                  const error = new Error(errName, errMessage)

                  const returner = {
                     self: entity.self,
                     error: error
                  }
                  reject(returner)

               } else {

                  // communication-wise we're good ...

                  try {

                     // we expect a list of all deleted entities in the array,
                     // while the requested entity should be part of that.
                     // Note that we trust server has only deleted requested children, 
                     // requested parents and their unrequested children.
                     if((fscl.variable.exists(result.entities)) &&
                        (result.entities.length >= 0)) {

                        const idx = result.entities.findIndex(id => (
                            (id.project === entity.self.project) &&
                            (id.entity === entity.self.entity))
                        )
                        if( idx > -1) {
                           // we're also good application-wise...
                           const returner = {
                              self: entity.self,
                              deletedIds: result.entities,
                              error: null
                           }
                           resolve(returner)

                        } else {
                           throw illStructuredDataError()
                        }
                     } else {
                        throw illStructuredDataError()
                     }

                  } catch(applicationError) {

                     const returner = {
                        self: entity.self,
                        error: applicationError
                     }
                     reject(returner)
                  }
               }
            }).catch(networkError => {

               // network/ low-level error...

               const returner = {
                  self: entity.self,
                  error: networkError
               }
               reject(returner)
            })
      })
   }
}

export default DeleteEntityService
