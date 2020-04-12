import { Types } from './EntityActionTerms'

export const ToggleFlagAction = (code, scope) => ({
   type: Types.TOGGLE_FLAG,
   scope,
   code
})


export default ToggleFlagAction
