import { Scopes } from '../../lib/redux/EntityActionTerms'
import ToggleFlagAction from '../../lib/redux/ToggleFlagAction'

const ToggleTargetFlagAction = (code) => ToggleFlagAction(
   code, Scopes.COMPONENTS.TARGETS.FUNCTIONS)


export default ToggleTargetFlagAction
