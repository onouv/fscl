import { Scopes } from '../../lib/redux/EntityActionTerms'
import ToggleFlagAction from '../../lib/redux/ToggleFlagAction'

const ToggleLinkedFunctionFlagAction = (code) => ToggleFlagAction(
   code, Scopes.COMPONENTS.LINKS.FUNCTIONS)


export default ToggleLinkedFunctionFlagAction
