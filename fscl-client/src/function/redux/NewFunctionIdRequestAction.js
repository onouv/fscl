import { Types, Scopes } from '../../lib/redux/EntityActionTerms'

import functionService from '../api/function-services.js'

/**
 * Retrieve a new child entity code from server for each functions marked as
 * changed. If none are marked,  a single new root-level entity code
 * will be requested from server.
 *
 * The results of either request will be dispatched to EntityActionTerms.NEW
 *
 * An error concerning a new root entity will be dispatched to
 * EntityActionTerms.ERROR
 *
 * @param {[type]} projectCode   the project in which new entity codes are to
 *                               be generated
 * @param {[type]} functions     the current list of function entities in the
 *                               app state
 */
const NewFunctionIdRequestAsyncAction =
   (projectCode, functions) => dispatch  => {

   functionService.requestNewIds(
      projectCode,
      functions,
      (oldFunctions, newIds) => {
         dispatch({
            type: Types.NEW,
            scope: Scopes.FUNCTIONS.BASE,
            entities: oldFunctions, // reducer shall insert into these old ones new entities
            newIds: newIds          // for these newIds
         })
      },
      (error) => {
         dispatch({
            type: Types.ERROR,
            scope: Scopes.FUNCTIONS.BASE,
            projectCode,
            error
         })
      }
   )
}

export default NewFunctionIdRequestAsyncAction
