import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import fetchRootLevel from '../../lib/api/FetchRootLevelService'
import { urls } from '../../lib/api'

const LoadTargetComponentsAsyncAction = (functionId) => dispatch => {

   const projectCode = functionId.project

   fetchRootLevel(
      urls.components.components(projectCode),
      components => {
         dispatch({
            type: Types.LOAD,
            scope: Scopes.FUNCTIONS.TARGETS.COMPONENTS,
            source: functionId,
            components
         })
      },
      error => {
         dispatch({
            type: Types.ERROR,
            scope: Scopes.FUNCTIONS.BASE,
            projectCode: functionId.project,
            error
         })
      }
   )
}

export default LoadTargetComponentsAsyncAction
