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

      case Scopes.COMPONENTS.TARGETS.FUNCTIONS:

         let targets = []

         switch(action.type) {
            case Types.LOAD:
               const existing = state.functions
               const loaded = entityList(action.functions)
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
                  state.functions)

               return {
                  source: source,
                  functions: inserts
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

      case Scopes.COMPONENTS.LINKS.FUNCTIONS:

         let functions = []

         switch(action.type) {

            case Types.LOAD:
               functions = manager.init.rawEntities(
                  action.functions,
                  codeFormat)
               break

            case Types.SAVE:
               functions = manager.clone.withSavedEntities(
                  state.functions,
                  action.functionsSaved,
                  codeFormat)
               break

            case Types.TOGGLE_FLAG:
               functions = manager.clone.withToggledMarkerFlag(
                  state.functions,
                  action.code)
               break

            case Types.DELETE:
               functions = manager.clone.withoutDeletedEntities(
                  state.functions,
                  action.functionsDeleted,  // at this point, there are no
                  action.functionsDeleted)  // errors, so we are mocking
               break                        // the 2nd parameter to reuse the
                                            // EntityManager
            case Types.FOLD:
               functions = manager.clone.withoutChildrenOfParent(
                  state.functions,
                  action.entity)
               break

            case Types.UNFOLD:
               functions = manager.clone.withNewChildrenOfParent(
                  state.functions,
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
            functions: functions
         }

      default:
         return {
            source: source,
            ...state
         }
   }
}

export default EntityLinksReducer
