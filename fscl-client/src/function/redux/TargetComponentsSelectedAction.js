import { Types, Scopes } from '../../lib/redux/EntityActionTerms'

const TargetComponentsSelectedAction = (components=[]) => {

   const action = {
      type: Types.SELECT,
      scope: Scopes.FUNCTIONS.TARGETS.COMPONENTS,
      selected: components
   }

   return action

}

export default TargetComponentsSelectedAction
