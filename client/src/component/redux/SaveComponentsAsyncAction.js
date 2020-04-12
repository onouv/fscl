import { Types, Scopes } from '../../lib/redux/EntityActionTerms'
import { urls } from '../../lib/api.js'
import PropTypes from 'prop-types';

import saveEntities from '../../lib/api/SaveEntitiesService'


const SaveComponentsAsyncAction = (components) => dispatch => {
   saveEntities(
      components,
      urls.components.component,
      (savedComponents) => {

         dispatch({
            type: Types.SAVE,
            scope: Scopes.COMPONENTS.BASE,
            components: savedComponents
         })
      }
   )
}


SaveComponentsAsyncAction.propTypes = {
   components: PropTypes.array
}

export default SaveComponentsAsyncAction
