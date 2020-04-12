import PropTypes from 'prop-types';
import fscl from '../lib'
import Error from '../Error'
import EntityCode from '../domain/EntityCode'


const EntityFoldService = (baseUrl, entity, dispatchFold, dispatchUnfold, format ) => {

   if(entity.unfolded) {

      // we  fold back
      dispatchFold(entity)

   } else {

      // we unfold ...

      // fetch children data from server...
      const proms = []
      entity.children.forEach(childCode => {

         const url = baseUrl + `/${childCode.entity}`

         proms.push(new Promise((resolve, reject) => {

            const errName = `error unfolding entity ${EntityCode.fromFlat(entity.self)}`

            fetch(url)                          //
               .then(result => result.json())   // no network/ low-level error ...
               .then(result => {                //

                  if(fscl.variable.exists(result.error)) {

                     //
                     // application error (e.g. 404, 500, etc.) ...
                     //

                     const errMessage = `when loading child ${EntityCode.fromFlat(childCode)}, server returned: ${result.error}`

                     const error = new Error(errName, errMessage)
                     reject(error)
                  } else {
                     //
                     // we are all okay communication-wise...
                     //

                     // we expect exactly one array element...
                     if(result.entities.length > 0) {

                        resolve(result.entities[0])

                     } else {

                        const ec = new EntityCode({self: entity.self, format: format})
                        const errMessage = `received empty response for ${ec.toString()}`
                        const error = new Error(errName, errMessage)

                        reject(error)
                     }
                  }
               })
               .catch(err => {

                  //
                  // network/ low-level error ...
                  //

                  const error = new Error(errName, err.message)
                  reject(error)
               })
         }))
      })

      Promise.all(proms)
         .then(unfoldedChilds => {
            dispatchUnfold(entity, unfoldedChilds)
         })
         .catch(error => {
            console.error(error);
            entity.error = error
            dispatchFold(entity)
         })
   }
}




EntityFoldService.propTypes = {
   url: PropTypes.func.isRequired,
   entity: fscl.propTypes.entity,
   dispatchResult: PropTypes.func,
   dispatchError: PropTypes.func,
   format: fscl.propTypes.format
}

export default EntityFoldService
