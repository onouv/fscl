import foldService from '../../lib/api/EntityFoldService'
import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import { urls } from '../../lib/api'

const FoldComponentAsyncAction = (component, isTarget) =>  dispatch => {

   const url = urls.components.components(component.self.project)
   const scope = isTarget
      ? Scopes.FUNCTIONS.TARGETS.COMPONENTS
      : Scopes.FUNCTIONS.LINKS.COMPONENTS

   foldService(
      url,
      component,
      (entity) => {
         dispatch({
            type: Types.FOLD,
            scope: scope,
            entity: entity
         })
      },
      (entity, children) => {
         dispatch({
            type: Types.UNFOLD,
            scope: scope,
            entity: entity,
            children: children
         })
      }
   )
}


export default FoldComponentAsyncAction
