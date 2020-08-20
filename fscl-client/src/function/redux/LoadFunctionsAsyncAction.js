import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import fetchRootLevel from '../../lib/api/FetchRootLevelService'
import { urls } from '../../lib/api'


const LoadFunctionsAsyncAction = (projectCode) => dispatch => {
   fetchRootLevel(
      urls.functions.functions(projectCode),
      functions => {
         dispatch({
            type: Types.LOAD,
            scope: Scopes.FUNCTIONS.BASE,
            projectCode: projectCode,
            functions
         })
      },
      error => {
         dispatch({
            type: Types.ERROR,
            scope: Scopes.FUNCTIONS.BASE,
            projectCode: projectCode,
            error
         })
      }
   )
}

export default LoadFunctionsAsyncAction
