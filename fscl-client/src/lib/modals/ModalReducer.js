import { Types, Scopes } from '../../lib/redux/EntityActionTerms'

const initialState = {
  modalType: null,
  modalProps: {}
}

function ModalReducer(state = initialState, action) {

   switch(action.type) {

      case Types.MODAL.HIDE:
         return initialState

      case Types.LOAD:
         switch(action.scope) {
            case Scopes.FUNCTIONS.TARGETS.COMPONENTS:
               return {
                 modalType: "ComponentLinkSelector",
                 modalProps: action.modalProps
               }

            case Scopes.COMPONENTS.TARGETS.FUNCTIONS:
               return {
                 modalType: "FunctionLinkSelector",
                 modalProps: action.modalProps
               }

            default:
               return state
         }

      case Types.SELECT:
         return initialState

      default:
         return state
   }

}

export default ModalReducer
