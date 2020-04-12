import ToggleFlagAction from '../../lib/redux/ToggleFlagAction'
import { Scopes } from '../../lib/redux/EntityActionTerms'

const ToggleFunctionFlagAction = (code) => ToggleFlagAction(
   code, Scopes.FUNCTIONS.BASE)

export default ToggleFunctionFlagAction
