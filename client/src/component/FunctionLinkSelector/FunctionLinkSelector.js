import { connect } from 'react-redux';

import FunctionLinkSelectorUI from './FunctionLinkSelectorUI'
import TargetFunctionSelectedAction from '../redux/TargetFunctionSelectedAction'
import ToggleTargetFlagAction from '../redux/ToggleTargetFlagAction'
import FoldFunctionAsyncAction from '../redux/FoldFunctionAsyncAction'

const mapStateToProps = (state, ownProps) => {
   const props = {
      functions: state.components.links.targets,
      selectEnabled: state.components.controls.saveable
   }
   return props
}

const IS_TARGET = true

const mapDispatchToProps = (dispatch) =>
   dispatch => ({
      onSelect(functions) {
         dispatch(TargetFunctionSelectedAction(functions))
      },
      onMarkChange(code) {
         dispatch(ToggleTargetFlagAction(code))
      },
      onFoldingControlEvent(functions) {
         dispatch(FoldFunctionAsyncAction(functions, IS_TARGET))
      }
   })

export default connect(mapStateToProps, mapDispatchToProps)
   (FunctionLinkSelectorUI)
