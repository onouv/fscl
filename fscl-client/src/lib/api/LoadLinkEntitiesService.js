const LoadLinkEntitiesService = (
   url,
   entityIds,
   dispatchResult,
   dispatchError) => {

   const promises = []

   entityIds.forEach(id => {

      const fetchUrl = url(id)

      promises.push(new Promise((resolve, reject) => {

         fetch(fetchUrl)
            .then(result => result.json())
            .then(result => {
               if(result.error) {

                  //
                  // application error (e.g. 404, 500, etc.) ...
                  //

                  reject(err(result.error))

               } else {

                  //
                  // we are all okay communication-wise...
                  //

                  resolve(result.entities[0])

               }
            })
            .catch(error => {

               //
               // network/ low-level error ...
               //

               reject(err(error))
             })
      }))

      function err(error) {
         const msg = `Error when loading {${id.project}| ${id.entity}}: `
         return new Error( msg, error)
      }
   })

   Promise.all(promises)
      .then(entities => {
         dispatchResult(entities)
      })
      .catch(error => {
         console.error(error)
         dispatchError(error)
      })
}




export default LoadLinkEntitiesService
