import { urls } from '../../lib/api'
import unlink from '../../lib/api/LinkEntitiesService'
import { Types, Scopes } from '../../lib/redux/EntityActionTerms'

const UnlinkFunctionsFromComponentAsyncAction = (
   component,
   functions) => dispatch => {

      unlink(
         urls.components.unlink.functions(component),
         component,
         functions,
         (functionsUnlinked) => {
            dispatch({
               type: Types.DELETE,
               scope: Scopes.COMPONENTS.LINKS.FUNCTIONS,
               functionsDeleted: functionsUnlinked
            })
         },
         error => {
            dispatch({
               type: Types.ERROR,
               scope: Scopes.COMPONENTS.BASE,
               error
            })
         }
      )
   }

export default UnlinkFunctionsFromComponentAsyncAction
