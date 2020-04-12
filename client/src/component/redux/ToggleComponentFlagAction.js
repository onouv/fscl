import ToggleFlagAction from '../../lib/redux/ToggleFlagAction'
import { Scopes } from '../../lib/redux/EntityActionTerms'


const ToggleComponentFlagAction = (code) => ToggleFlagAction(
   code, Scopes.COMPONENTS.BASE)

export default ToggleComponentFlagAction
