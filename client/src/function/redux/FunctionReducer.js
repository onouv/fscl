import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import reduceControls from '../../lib/redux/EntityControlsReducer'
import EntityLinksReducer from './EntityLinksReducer'
import manager from '../../lib/redux/EntityManager'
import format from '../../lib/domain/CodeFormats'

const reduceLinks = (state, action) =>
   EntityLinksReducer(state, action, format.componentFormat)

const  FunctionReducer = (state={}, action) => {

   let links = []
   let controls

   switch(action.scope){
      case Scopes.FUNCTIONS.BASE:
         return reduceBaseScopes(state, action)

      case Scopes.FUNCTIONS.LINKS.COMPONENTS:
         switch(action.type) {
            case Types.LOAD:
            case Types.SAVE:
            case Types.TOGGLE_FLAG:
            case Types.DELETE:
            case Types.FOLD:
            case Types.UNFOLD:
               links = reduceLinks(state.links, action)
               controls = reduceControls(state.links.components, action)
               return {
                  ...state,
                  links: links,
                  controls: controls,
                  error: null
               }

            default:
               return state
         }

      case Scopes.FUNCTIONS.TARGETS.COMPONENTS:
         switch(action.type) {
            case Types.LOAD:
            case Types.TOGGLE_FLAG:
            case Types.FOLD:
            case Types.UNFOLD:
               links = reduceLinks(state.links, action)
               controls = reduceControls(state.links.targets, action)
               return {
                  ...state,
                  links: links,
                  controls: controls,
                  error: null
               }

            case Types.SELECT:
               links = reduceLinks(state.links, action)
               controls = reduceControls(state.links.components, action)
               return {
                  ...state,
                  links: links,
                  controls: controls,
                  error: null
               }

            default:
               return state
         }

      default:
         return state
   }
}


function reduceBaseScopes(state, action) {

   let entities = []

   switch(action.type) {

      case Types.TOGGLE_FLAG:
         entities = manager.clone.withToggledMarkerFlag(
            state.entities,
            action.code)
         return {
            project: state.project,
            entities: entities,
            links: reduceLinks(state, action),
            controls: reduceControls(entities, action),
            error: null
         }

      case Types.LOAD:
         entities = manager.init.rawEntities(
            action.functions,
            format.functionFormat)
         return {
            project: action.projectCode,
            entities: entities,
            links: state.links,
            controls: reduceControls(entities, action),
            error: null
         }

      case Types.NEW:
         entities = manager.clone.withNewEntitiesFromCodes(
            action.entities,
            action.newIds,
            format.functionFormat)
         return {
            project: action.projectCode,
            entities: entities,
            links: state.links,
            controls: reduceControls(entities, action),
            error: null
         }

      case Types.SAVE:
         entities = manager.clone.withSavedEntities(
            state.entities,
            action.functions,
            format.functionFormat)
         return {
            project: state.project,
            entities: entities,
            links: state.links,
            controls: reduceControls(entities, action),
            error: null
         }

      case Types.CHANGE:

         entities = manager.clone.withChangedEntity(
            state.entities,
            action.entityChange)
         return {
            project: state.project,
            entities: entities,
            links: state.links,
            controls: reduceControls(entities, action),
            error: null
         }

      case Types.DELETE:
         entities = manager.clone.withoutDeletedEntities(
            state.entities,
            action.functionsReqToDelete,
            action.actualDeletions)
         return {
            project: state.project,
            entities: entities,
            links: state.links,
            controls: reduceControls(entities, action),
            error: null
         }

      case Types.UNFOLD:
         entities = manager.clone.withNewChildrenOfParent(
            state.entities,
            action.entity,
            action.children)
         return {
            project: state.project,
            entities: entities,
            links: state.links,
            controls: reduceControls(entities, action),
            error: null
         }

      case Types.FOLD:
         entities = manager.clone.withoutChildrenOfParent(
            state.entities,
            action.entity)
         return {
            project: state.project,
            entities: entities,
            links: state.links,
            controls: reduceControls(entities, action),
            error: null
         }

      case Types.ERROR:
         return {
            project: action.projectCode,
            entities: [],
            links: state.links,
            controls: reduceControls([], action),
            error: action.error
         }

      default:
         return state
   }
}


export default FunctionReducer;
