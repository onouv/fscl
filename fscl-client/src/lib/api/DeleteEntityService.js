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

                     // we expect exacly one element in the array...
                     if((fscl.variable.exists(result.entities)) &&
                        (result.entities.length === 1)) {

                        const id = result.entities[0]

                        if((id.project === entity.self.project) &&
                          (id.entity === entity.self.entity )) {

                              // we're also good application-wise...
                              const returner = {
                                 self: id,
                                 error: null
                              }
                              resolve(returner)

                        } else {

                           const errMessage = 'server returned inconsistent code for deleted entity'
                           const error = new Error(errName, errMessage)
                           throw error
                        }

                     } else {
                        const errMessage = 'server returned ill-structured data'
                        const error = new Error(errName, errMessage)
                        throw error
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
