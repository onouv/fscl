import { Types } from './EntityActionTerms'

const ChangeEntityDescriptionAction = (code, change, scope) => (
   {
      type: Types.CHANGE,
      scope: scope,
      code,
      entityChange: {
         code: code,
         name: null,
         description: change
      }
   })

export default ChangeEntityDescriptionAction
