import foldService from '../../lib/api/EntityFoldService'
import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import { urls } from '../../lib/api'

/**
 * The "C" link button has been clicked. User whishes to either unfold the
 * childs of component or to fold up the component again, to make the children
 * disappear.
 *
 * Engage EntityFoldService to figure out the details. This may involve
 * aynchronous server interaction, hence return of a dispatch function
 *
 * @param {[type]} entity an object as devlivered by Entity.value
 */
const CLinkButtonClickedAsyncAction = (entity) =>  dispatch => {

   const url = urls.components.components(entity.self.project)

   foldService(
      url,
      entity,
      (entity) => {
         dispatch({
            type: Types.FOLD,
            scope: Scopes.COMPONENTS.BASE,
            entity: entity
         })
      },
      (entity, children) => {
         dispatch({
            type: Types.UNFOLD,
            scope: Scopes.COMPONENTS.BASE,
            entity: entity,
            children: children
         })
      }
   )
}


export default CLinkButtonClickedAsyncAction
