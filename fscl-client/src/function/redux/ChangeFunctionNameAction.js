import { Types, Scopes } from '../../lib/redux/EntityActionTerms'

const ChangeFunctionNameAction = (code, change) => ({
   type: Types.CHANGE,
   scope: Scopes.FUNCTIONS.BASE,
   code,
   entityChange: {
      code: code,
      name: change,
      description: null
   }
})

export default ChangeFunctionNameAction
