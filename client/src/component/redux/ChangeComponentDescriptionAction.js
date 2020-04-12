import ChangeEntityDescriptionAction from '../../lib/redux/ChangeEntityDescriptionAction'
import { Scopes } from '../../lib/redux/EntityActionTerms'

const ChangeComponentDescriptionAction = (code, change) => ChangeEntityDescriptionAction(
   code,
   change,
   Scopes.COMPONENTS.BASE)


export default ChangeComponentDescriptionAction
