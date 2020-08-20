import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import fetchRootLevel from '../../lib/api/FetchRootLevelService'
import loadLinkedFunctions from '../../lib/api/LoadLinkEntitiesService'
import { urls } from '../../lib/api'

const LoadLinkedFunctionsAsyncAction = (componentId) => dispatch => {

   fetchRootLevel(
      urls.components.functions(componentId),
      functionIds => {

         loadLinkedFunctions(
            urls.functions.function,
            functionIds,
            functions => {
               dispatch({
                  type: Types.LOAD,
                  scope: Scopes.COMPONENTS.LINKS.FUNCTIONS,
                  source: componentId,
                  functions
               })
            },
            error => {
               handleError(error)
            }
         )
      },
      error => {
         handleError(error)
      }
   )

   function handleError(error) {
      dispatch({
         type: Types.ERROR,
         scope: Scopes.COMPONENTS.BASE,
         projectCode: componentId.project,
         error
      })
   }
}




export default LoadLinkedFunctionsAsyncAction
