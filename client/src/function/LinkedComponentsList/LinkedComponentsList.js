import { connect } from 'react-redux';
import LinkedComponentsListUI from './LinkedComponentsListUI'
import entityList from '../../lib/domain/EntityList'
import ToggleLinkedComponentFlagAction from '../redux/ToggleLinkedComponentFlagAction'
import FoldComponentAsyncAction from '../redux/FoldComponentAsyncAction'

const mapStateToProps = (state, ownProps) => {

   // create id of the function in question
   const id = {
      project: ownProps.match.params.project,
      entity: ownProps.match.params.entity
   }

   // look up that functions content
   const functions = entityList(state.functions.entities)
   const sourceFunction = functions.findEntity(id)
   let content
   if(sourceFunction && sourceFunction != null) {
      content = sourceFunction.content
   } else {
      content = {
         name: "",
         description: ""
      }
   }

   return ({
      functionId: id,
      functionContent: content,
      components: state.functions.links.components,
      error: state.functions.error
   })
}

const IS_NOT_TARGET = false
const mapDispatchToProps = (dispatch) => ({
   onMarkChange(code) {
      dispatch(ToggleLinkedComponentFlagAction(code))
   },
   onFoldingControlEvent(component) {
      dispatch(FoldComponentAsyncAction(component, IS_NOT_TARGET))
   }
})


export default connect(mapStateToProps, mapDispatchToProps)
   (LinkedComponentsListUI)
