import { urls } from '../../lib/api'
import link from '../../lib/api/LinkEntitiesService'
import { Types, Scopes } from '../../lib/redux/EntityActionTerms'



const LinkFunctionsToComponentAsyncAction = (
   source,
   functions) => dispatch => {

   link(
      urls.components.link.functions(source),
      source,
      functions,
      (functionsSaved) => {
         dispatch({
            type: Types.SAVE,
            scope: Scopes.COMPONENTS.LINKS.FUNCTIONS,
            functionsSaved: functionsSaved
         })
      },
      (error) => {
         // show error in ComponentList...
         dispatch({
            type: Types.ERROR,
            scope: Scopes.COMPONENTS.BASE,
            error
         })
      }
   )
}

export default LinkFunctionsToComponentAsyncAction
