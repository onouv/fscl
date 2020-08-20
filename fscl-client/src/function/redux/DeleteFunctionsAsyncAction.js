import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import deleteFunction from '../../lib/api/DeleteEntityService'
import { urls } from '../../lib/api'

const DeleteFunctionsAsyncAction = (functions) => dispatch =>
   deleteFunction(
      functions,
      urls.functions.base,
      (deletions, functionsToDelete) => {
         dispatch({
            type: Types.DELETE,
            scope: Scopes.FUNCTIONS.BASE,
            functionsReqToDelete: functionsToDelete,
            actualDeletions: deletions
         })
      }
   )

export default DeleteFunctionsAsyncAction
