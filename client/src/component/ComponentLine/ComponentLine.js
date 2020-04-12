import { connect } from 'react-redux';

import ComponentLineUI from './ComponentLineUI'
import ToggleComponentFlagAction from '../redux/ToggleComponentFlagAction'
import ChangeComponentNameAction from '../redux/ChangeComponentNameAction'
import ChangeComponentDescriptionAction from '../redux/ChangeComponentDescriptionAction'
import CLinkButtonClickedAsyncAction from '../redux/CLinkButtonClickedAsyncAction'



const mapStateToProps = (state, props) => {

   const index = props.index

   if(index === undefined || index === null) {
      console.error(`ComponentLine called for invalid index prop.`)
      return ({})

   } else {

      const entityDisplayed = state.components.entities[index]
      const project = state.components.projectCode

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
            dispatch(ToggleComponentFlagAction(entityCode))
         },
         onNameChange(self, change) {
            dispatch(ChangeComponentNameAction(self, change))
         },
         onDescriptionChange(self, change) {
            dispatch(ChangeComponentDescriptionAction(self, change))
         },
         onCLinked(entity) {
            dispatch(CLinkButtonClickedAsyncAction(entity))
         }
      }
   )

export default connect(mapStateToProps, mapDispatchToProps)(ComponentLineUI)
