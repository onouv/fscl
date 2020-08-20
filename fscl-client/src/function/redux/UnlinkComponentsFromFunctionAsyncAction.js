import { urls } from '../../lib/api'
import unlink from '../../lib/api/LinkEntitiesService'
import { Types, Scopes } from '../../lib/redux/EntityActionTerms'

const UnlinkComponentsFromFunctionAsyncAction = (
   source,
   components) => dispatch => {

      unlink(
         urls.functions.unlink.components(source),
         source,
         components,
         (componentsUnlinked) => {
            dispatch({
               type: Types.DELETE,
               scope: Scopes.FUNCTIONS.LINKS.COMPONENTS,
               componentsDeleted: componentsUnlinked
            })
         },
         error => {
            dispatch({
               type: Types.ERROR,
               scope: Scopes.FUNCTIONS.BASE,
               error
            })
         }
      )
   }

export default UnlinkComponentsFromFunctionAsyncAction
