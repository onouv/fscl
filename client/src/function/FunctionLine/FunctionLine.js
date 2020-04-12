import { connect } from 'react-redux';

import FunctionLineUI from './FunctionLineUI'
import ChangeFunctionNameAction from '../redux/ChangeFunctionNameAction'
import ChangeFunctionDescriptionAction from '../redux/ChangeFunctionDescriptionAction'
import ToggleFlagAction from '../redux/ToggleFunctionFlagAction'
import FLinkButtonClickedAsyncAction from '../redux/FLinkButtonClickedAsyncAction'

const mapStateToProps = (state, props) => {

   const index = props.index

   if(index === undefined || index === null) {
      console.error(`FunctionLine called for invalid index prop.`)
      return ({})

   } else {

      const entityDisplayed = state.functions.entities[index]
      const project = state.functions.projectCode

      return ({
         index,
         entityDisplayed,
         project
      })
   }
}

const mapDispatchToProps = (dispatch, props) =>
   dispatch => (
      {
         onFlagChange(entityCode) {
            dispatch(ToggleFlagAction(entityCode))
         },
         onNameChange(self, change) {
            dispatch(ChangeFunctionNameAction(self, change))
         },
         onDescriptionChange(self, change) {
            dispatch(ChangeFunctionDescriptionAction(self, change))
         },
         onFLinked(entity) {
            dispatch(FLinkButtonClickedAsyncAction(entity))
         }
      }
   )

export default connect(mapStateToProps, mapDispatchToProps)(FunctionLineUI)
