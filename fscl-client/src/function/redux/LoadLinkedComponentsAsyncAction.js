import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import fetchRootLevel from '../../lib/api/FetchRootLevelService'
import loadLinkedComponents from '../../lib/api/LoadLinkEntitiesService'
import { urls } from '../../lib/api'

const LoadLinkedComponentsAsyncAction = (functionId) => dispatch => {

   fetchRootLevel(
      urls.functions.components(functionId),
      componentIds => {

         loadLinkedComponents(
            urls.components.component,
            componentIds,
            components => {
               dispatch({
                  type: Types.LOAD,
                  scope: Scopes.FUNCTIONS.LINKS.COMPONENTS,
                  source: functionId,
                  components
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
         scope: Scopes.FUNCTIONS.BASE,
         projectCode: functionId.project,
         error
      })
   }
}




export default LoadLinkedComponentsAsyncAction
