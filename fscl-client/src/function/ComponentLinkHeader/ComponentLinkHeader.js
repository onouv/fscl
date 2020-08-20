import { connect } from 'react-redux';

import ActionControlHeaderUI from '../../lib/ui/ActionControlHeaderUI'
import LoadTargetComponentsAsyncAction from '../redux/LoadTargetComponentsAsyncAction'
import LinkComponentsToFunctionAsyncAction from '../redux/LinkComponentsToFunctionAsyncAction'
import UnlinkComponentsFromFunctionAsyncAction from '../redux/UnlinkComponentsFromFunctionAsyncAction'

const mapStateToProps = (state, ownProps) => {

   return ({
      saveable: state.functions.controls.saveable,
      deletable: state.functions.controls.deletable,
      sourceEntityId: ownProps.sourceFunctionId,
      entities: state.functions.links.components
   })
}

const mapDispatchToProps = (dispatch, ownProps) => ({

   onSave(sourceId, linkedComponents) {
      dispatch(LinkComponentsToFunctionAsyncAction(
         sourceId,
         linkedComponents)
      )
   },
   onDelete(sourceId, linkedComponents) {
      dispatch(UnlinkComponentsFromFunctionAsyncAction(
         sourceId,
         linkedComponents))
   },
   onNew(sourceFunctionId) {
      dispatch(LoadTargetComponentsAsyncAction(sourceFunctionId))
   }
})

export default connect(
   mapStateToProps,
   mapDispatchToProps)(ActionControlHeaderUI)
