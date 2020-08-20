import { connect } from 'react-redux';

import ComponentLinkSelectorUI from './ComponentLinkSelectorUI'
import TargetComponentsSelectedAction from '../redux/TargetComponentsSelectedAction'
import ToggleTargetFlagAction from '../redux/ToggleTargetFlagAction'
import FoldComponentAsyncAction from '../redux/FoldComponentAsyncAction'

const mapStateToProps = (state, ownProps) => {
   const props = {
      components: state.functions.links.targets,
      selectEnabled: state.functions.controls.saveable
   }
   return props
}

const IS_TARGET = true

const mapDispatchToProps = (dispatch) =>
   dispatch => ({
      onSelect(components) {
         dispatch(TargetComponentsSelectedAction(components))
      },
      onMarkChange(code) {
         dispatch(ToggleTargetFlagAction(code))
      },
      onFoldingControlEvent(component) {
         dispatch(FoldComponentAsyncAction(component, IS_TARGET))
      }
   })

export default connect(mapStateToProps, mapDispatchToProps)
   (ComponentLinkSelectorUI)
