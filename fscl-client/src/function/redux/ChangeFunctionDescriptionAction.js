import { Types, Scopes } from '../../lib/redux/EntityActionTerms'


const ChangeFunctionDescriptionAction = (code, change) => (
   {
      type: Types.CHANGE,
      scope: Scopes.FUNCTIONS.BASE,
      code,
      entityChange: {
         code: code,
         name: null,
         description: change
      }
   })


export default ChangeFunctionDescriptionAction
