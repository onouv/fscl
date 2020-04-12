import { connect } from 'react-redux';

import LinkedFunctionsListUI from './LinkedFunctionsListUI'
import ToggleLinkedFunctionFlagAction from '../redux/ToggleLinkedFunctionFlagAction'
import FoldFunctionAsyncAction from '../redux/FoldFunctionAsyncAction'
import entityList from '../../lib/domain/EntityList'

const mapStateToProps = (state, ownProps) => {

   // create id of the component in question
   const id = {
      project: ownProps.match.params.project,
      entity: ownProps.match.params.entity
   }

   // look up that components content
   const components = entityList(state.components.entities)
   const source = components.findEntity(id)
   let content
   if(source && source != null) {
      content = source.content
   } else {
      content = {
         name: "",
         description: ""
      }
   }

   return ({
      componentId : id,
      componentContent: content,
      functions: state.components.links.functions,
      error: state.components.error
   })
}

const IS_NOT_TARGET = false
const mapDispatchToProps = (dispatch, props) => ({
   onMarkChange(code) {
      dispatch(ToggleLinkedFunctionFlagAction(code))
   },
   onFoldingControlEvent(component) {
      dispatch(FoldFunctionAsyncAction(component, IS_NOT_TARGET))
   }
})

export default connect(mapStateToProps, mapDispatchToProps)
   (LinkedFunctionsListUI)
