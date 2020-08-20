import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import fetchRootLevel from '../../lib/api/FetchRootLevelService'
import { urls } from '../../lib/api'

const LoadTargetFunctionsAsyncAction = (componentId) => dispatch => {

   const projectCode = componentId.project

   fetchRootLevel(
      urls.functions.functions(projectCode),
      functions => {
         dispatch({
            type: Types.LOAD,
            scope: Scopes.COMPONENTS.TARGETS.FUNCTIONS,
            source: componentId,
            functions
         })
      },
      error => {
         dispatch({
            type: Types.ERROR,
            scope: Scopes.COMPONENTS.BASE,
            projectCode: componentId.project,
            error
         })
      }
   )
}

export default LoadTargetFunctionsAsyncAction
