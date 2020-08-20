import { Types } from './EntityActionTerms'

const ChangeEntityNameAction = (code, change, scope) => (
   {
      type: Types.CHANGE,
      scope: scope,
      code,
      entityChange: {
         code: code,
         name: change,
         description: null
      }
   })


export default ChangeEntityNameAction
