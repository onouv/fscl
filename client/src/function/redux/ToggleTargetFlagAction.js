import { Scopes } from '../../lib/redux/EntityActionTerms'
import ToggleFlagAction from '../../lib/redux/ToggleFlagAction'

const ToggleTargetFlagAction = (code) => ToggleFlagAction(
   code, Scopes.FUNCTIONS.TARGETS.COMPONENTS)


export default ToggleTargetFlagAction
