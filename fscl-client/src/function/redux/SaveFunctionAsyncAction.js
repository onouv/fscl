import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import { urls } from '../../lib/api.js'
import PropTypes from 'prop-types';

import saveEntities from '../../lib/api/SaveEntitiesService'


const SaveFunctionsAsyncAction = (functions) => dispatch => {
   saveEntities(
      functions,
      urls.functions.function,
      (functions) => {
         dispatch({
            type: Types.SAVE,
            scope: Scopes.FUNCTIONS.BASE,
            functions
         })
      }
   )
}


SaveFunctionsAsyncAction.propTypes = {
   functions: PropTypes.array
}


export default SaveFunctionsAsyncAction
