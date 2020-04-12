import { Scopes } from '../../lib/redux/EntityActionTerms'
import ToggleFlagAction from '../../lib/redux/ToggleFlagAction'

const ToggleLinkedComponentFlagAction = (code) => ToggleFlagAction(
   code, Scopes.FUNCTIONS.LINKS.COMPONENTS)


export default ToggleLinkedComponentFlagAction
