import fscl from '../../lib/lib'
import EntityCode from '../domain/EntityCode'

const LinkEntitiesService = (
   url,
   sourceId,
   entities,
   dispatchOK,
   dispatchError) => {

   const requesters = entities.filter(e => e.marked === true)
   const ids = requesters.map(e => (e.self))
   const message = createRequest(ids)

   fetch(url, message)
      .then(result => result.json())
      .then(result => {

         // communication to server succeeded...

         if(fscl.variable.exists(result.error)) {

            //
            // application error (e.g. 404, 500, etc.) ...
            //

            const errMessage = `server returned: ${result.error}`
            const error = new Error(errMessage)
                        
            throw(error)

         } else {

            //
            // we are all okay communication-wise...
            //

            if((result.project === sourceId.project) &&
               (result.entity === sourceId.entity )) {

                  dispatchOK(requesters)

               } else {

               const error = new Error(
                  `received ${EntityCode.fromFlat(...result)}\
                   while expecting ${EntityCode.fromFlat(sourceId)}`)

               throw(error)
            }
         }
      })
      .catch(err => {

         //
         // network/ low-level error or catching one of the above...
         //

         const errName = `error during linking action ${EntityCode.fromFlat(sourceId)}`
         const error = new Error(errName, err.message)
         dispatchError(error)

      })
   }



function createRequest(entities) {

   const request = {
      method: "POST",
      headers: {
         'Content-Type': 'application/json'
      },
      body: JSON.stringify({ entities: entities })
   }

   return request
}

export default LinkEntitiesService
