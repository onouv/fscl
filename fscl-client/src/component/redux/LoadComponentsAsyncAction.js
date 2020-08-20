import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import fetchRootLevel from '../../lib/api/FetchRootLevelService'
import { urls } from '../../lib/api'

const LoadComponentsAsyncAction = (projectCode) => dispatch => {

   const url = urls.components.components(projectCode)
   fetchRootLevel(
      url,
      components => {
         dispatch({
            type: Types.LOAD,
            scope: Scopes.COMPONENTS.BASE,
            projectCode: projectCode,
            components
         })
      },
      error => {
         dispatch({
            type: Types.ERROR,
            scope: Scopes.COMPONENTS.BASE,
            projectCode: projectCode,
            error
         })
      }
   )
}


export default LoadComponentsAsyncAction
