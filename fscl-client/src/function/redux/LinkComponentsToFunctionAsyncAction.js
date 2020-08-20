import { urls } from '../../lib/api'
import link from '../../lib/api/LinkEntitiesService'
import { Types, Scopes } from '../../lib/redux/EntityActionTerms'



const LinkComponentsToFunctionAsyncAction = (
   source,
   components) => dispatch => {

   link(
      urls.functions.link.components(source),
      source,
      components,
      (componentsSaved) => {
         dispatch({
            type: Types.SAVE,
            scope: Scopes.FUNCTIONS.LINKS.COMPONENTS,
            componentsSaved: componentsSaved
         })
      },
      (error) => {
         // show error in ComponentList...
         dispatch({
            type: Types.ERROR,
            scope: Scopes.FUNCTIONS.BASE,
            error
         })
      }
   )
}

export default LinkComponentsToFunctionAsyncAction
