import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import componentService from '../api/ComponentService.js'

/**
 * Retrieve a new child entity code from server for each component marked as
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
 * @param {[type]} components     the current list of entities in the
 *                               app state
 */
const NewComponentIdRequestAsyncAction =
   (projectCode, components) => dispatch  => {

   componentService.requestNewIds(
      projectCode,
      components,
      (oldComponents, newIds) => {
         dispatch({
            type: Types.NEW,
            scope: Scopes.COMPONENTS.BASE,
            entities: oldComponents, // reducer shall insert into these old ones
            newIds: newIds           // new entities for these newIds
         })
      },
      (error) => {
         dispatch({
            type: Types.ERROR,
            scope: Scopes.COMPONENTS.BASE,
            projectCode,
            error
         })
      }
   )
}

export default NewComponentIdRequestAsyncAction
