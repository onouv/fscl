import { Types, Scopes } from '../../lib/redux/EntityActionTerms'

const TargetFunctionSelectedAction = (functions=[]) => {

   const action = {
      type: Types.SELECT,
      scope: Scopes.COMPONENTS.TARGETS.FUNCTIONS,
      selected: functions
   }

   return action

}

export default TargetFunctionSelectedAction
