import ComponentSelectLineUI from './ComponentSelectLineUI'
import FoldComponentAsyncAction from './FoldComponentAsyncAction'
import { connect } from 'react-redux';

//const mapStateToProps = (state, props) => { }
const mapDispatchToProps = (dispatch) => ({
   onFoldingControlEvent(component) {
      dispatch(FoldComponentAsyncAction(component))
   }
})
//*

export default connect(null, mapDispatchToProps)(ComponentSelectLineUI)
