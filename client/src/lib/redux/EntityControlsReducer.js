import { Scopes } from '../../lib/redux/EntityActionTerms'

const EntityControlsReducer = (entities=[], action) => {

   let deletable = false
   let saveable = false

   const marked = entities.some(e => e.marked === true)
   const changed = entities.some(e => e.changed === true)

   switch(action.scope) {

      case Scopes.FUNCTIONS.LINKS.COMPONENTS:
      case Scopes.FUNCTIONS.TARGETS.COMPONENTS:
      case Scopes.COMPONENTS.LINKS.FUNCTIONS:
      case Scopes.COMPONENTS.TARGETS.FUNCTIONS:

         deletable = marked
         saveable = marked
         break

      case Scopes.FUNCTIONS.BASE:
      default:
         deletable = marked
         saveable = changed
   }

   return {
      saveable,
      deletable
   }
};


export default EntityControlsReducer;
