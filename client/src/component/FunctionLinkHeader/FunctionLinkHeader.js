import { connect } from 'react-redux';

import ActionControlHeaderUI from '../../lib/ui/ActionControlHeaderUI'
import LoadTargetFunctionsAsyncAction from '../redux/LoadTargetFunctionsAsyncAction'
import LinkFunctionsToComponentsAsyncAction from '../redux/LinkFunctionsToComponentAsyncAction'
import UnlinkFunctionsFromComponentAsyncAction from '../redux/UnlinkFunctionsFromComponentAsyncAction'

const mapStateToProps = (state, ownProps) => {

   return ({
      saveable: state.components.controls.saveable,
      deletable: state.components.controls.deletable,
      sourceEntityId: ownProps.sourceEntityId,
      entities: state.components.links.functions
   })
}

const mapDispatchToProps = (dispatch, ownProps) => ({

   onSave(sourceId, linkedFunctions) {
      dispatch(LinkFunctionsToComponentsAsyncAction(
         sourceId,
         linkedFunctions)
      )
   },
   onDelete(sourceId, linkedFunctions) {
      dispatch(UnlinkFunctionsFromComponentAsyncAction(
         sourceId,
         linkedFunctions))
   },
   onNew(sourceId) {
      dispatch(LoadTargetFunctionsAsyncAction(sourceId))
   }
})

export default connect( mapStateToProps, mapDispatchToProps)
   (ActionControlHeaderUI)
