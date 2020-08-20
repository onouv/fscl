import { connect } from 'react-redux';

import ComponentListUI from './ComponentListUI'
import NewComponentIdRequestAsyncAction from '../redux/NewComponentIdRequestAsyncAction'
import SaveComponentsAsyncAction from '../redux/SaveComponentsAsyncAction'
import DeleteComponentsAsyncAction from '../redux/DeleteComponentsAsyncAction'


const mapStateToProps = (state, props) => {
   return ({
      projectCode: props.match.params.project,
      saveable: state.components.controls.saveable,
      deletable: state.components.controls.deletable,
      linkable: state.components.controls.linkable,
      error: state.components.error,
      components: state.components.entities
   })
}

const mapDispatchToProps = (dispatch, props) => {

   return ({
      onNew(components) {
         dispatch(NewComponentIdRequestAsyncAction(
            props.match.params.project,
            components))
      },
      onSave(components) {
         dispatch(SaveComponentsAsyncAction(components))
      },
      onDelete(components) {
         dispatch(DeleteComponentsAsyncAction(components))
      }
   })
}


export default connect(mapStateToProps, mapDispatchToProps)(ComponentListUI)
