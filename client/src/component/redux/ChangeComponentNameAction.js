import ChangeEntityNameAction from '../../lib/redux/ChangeEntityNameAction'
import { Scopes } from '../../lib/redux/EntityActionTerms'

const ChangeComponentNameAction = (code, change) => ChangeEntityNameAction(
   code,
   change,
   Scopes.COMPONENTS.BASE)


export default ChangeComponentNameAction
