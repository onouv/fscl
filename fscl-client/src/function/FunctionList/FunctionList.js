import { connect } from 'react-redux';
import FunctionList from './FunctionListUI';

import NewFunctionIdRequestAsyncAction from '../redux/NewFunctionIdRequestAction'
import SaveFunctionsAsyncAction from '../redux/SaveFunctionAsyncAction'
import DeleteFunctionsAsyncAction from '../redux/DeleteFunctionsAsyncAction'

const mapStateToProps = (state, ownProps) => {

   return ({
      functions: state.functions.entities,
      projectCode: ownProps.match.params.project,
      saveable: state.functions.controls.saveable,
      deletable: state.functions.controls.deletable,
      error: state.functions.error
   })
}

const mapDispatchToProps = (dispatch, ownProps) => ({

   onNew(functions) {
      dispatch(NewFunctionIdRequestAsyncAction(
         ownProps.match.params.project,
         functions))
   },
   onSave(functions) {
      dispatch(SaveFunctionsAsyncAction(functions))
   },
   onDelete(functions) {
      dispatch(DeleteFunctionsAsyncAction(functions))
   }
})

const FunctionListContainer = connect(
   mapStateToProps,
   mapDispatchToProps)(FunctionList);


export default FunctionListContainer
