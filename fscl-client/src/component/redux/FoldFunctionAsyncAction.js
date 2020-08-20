import foldService from '../../lib/api/EntityFoldService'
import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import { urls } from '../../lib/api'

const FoldFunctionAsyncAction = (func, isTarget) =>  dispatch => {

   const url = urls.functions.functions(func.self.project)
   const scope = isTarget
      ? Scopes.COMPONENTS.TARGETS.FUNCTIONS
      : Scopes.COMPONENTS.LINKS.FUNCTIONS

   foldService(
      url,
      func,
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


export default FoldFunctionAsyncAction
