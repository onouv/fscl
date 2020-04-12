import { Types, Scopes } from '../../lib/redux/EntityActionTerms'

import manager from '../../lib/redux/EntityManager'
//import linkMgr from '../link/LinkedEntityManager'
import entityList from '../../lib/domain/EntityList'
//import fscl from '../lib'

const EntityLinksReducer = (state, action, codeFormat) => {

   const source = action.source ? action.source: state.source
   /*const componentsFromState =
      fscl.variable.exists(state.components) ? state.components : []*/


   switch(action.scope){

      case Scopes.FUNCTIONS.TARGETS.COMPONENTS:

         let targets = []

         switch(action.type) {
            case Types.LOAD:
               const existing = state.components
               const loaded = entityList(action.components)
               const candidates = loaded.pickAllExcept(existing)
               targets = manager.init.rawEntities(
                  candidates,
                  codeFormat)
               break

            case Types.TOGGLE_FLAG:
               targets = manager.clone.withToggledMarkerFlag(
                  state.targets,
                  action.code)
               break

            case Types.SELECT:
               // insert copy of any marked entities from functions.links.targets to
               // functions.links.components, ensuring they are marked there, as well
               const inserts = manager.clone.withCopiedEntitiesWhenMarked(
                  state.targets,
                  state.components)

               return {
                  source: source,
                  components: inserts
               }

            case Types.FOLD:
               targets = manager.clone.withoutChildrenOfParent(
                  state.targets,
                  action.entity)
               break

            case Types.UNFOLD:
               targets = manager.clone.withNewChildrenOfParent(
                  state.targets,
                  action.entity,
                  action.children,
                  codeFormat)
               break

            default:
               return {
                  source: source,
                  ...state
               }
         }

         return {
            ...state,
            source: source,
            targets: targets
         }

      case Scopes.FUNCTIONS.LINKS.COMPONENTS:

         let components = []

         switch(action.type) {

            case Types.LOAD:
               components = manager.init.rawEntities(
                  action.components,
                  codeFormat)
               break

            case Types.SAVE:
               components = manager.clone.withSavedEntities(
                  state.components,
                  action.componentsSaved,
                  codeFormat)
               break

            case Types.TOGGLE_FLAG:
               components = manager.clone.withToggledMarkerFlag(
                  state.components,
                  action.code)
               break

            case Types.DELETE:
               components = manager.clone.withoutDeletedEntities(
                  state.components,
                  action.componentsDeleted,  // at this point, there are no
                  action.componentsDeleted)  // errors, so we are mocking
               break                         // the 2nd parameter to reuse the
                                             // EntityManager
            case Types.FOLD:
               components = manager.clone.withoutChildrenOfParent(
                  state.components,
                  action.entity)
               break

            case Types.UNFOLD:
               components = manager.clone.withNewChildrenOfParent(
                  state.components,
                  action.entity,
                  action.children,
                  codeFormat)
               break

            default:
               return {
                  source: source,
                  ...state
               }
         }

         return {
            ...state,
            source: source,
            components: components
         }

      default:
         return {
            source: source,
            ...state
         }
   }
}

export default EntityLinksReducer
