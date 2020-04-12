import { Types, Scopes } from '../../lib/redux/EntityActionTerms'


import deleteComponent from '../../lib/api/DeleteEntityService'
import { urls } from '../../lib/api'

import PropTypes from 'prop-types';

const DeleteComponentsAsyncAction = (components) => dispatch => {

   const url = urls.components.base

   deleteComponent(
      components,
      url,
      (deletions, deletedComponents) => {
         dispatch({
            type: Types.DELETE,
            scope: Scopes.COMPONENTS.BASE,
            deletedComponents: deletedComponents,
            actualDeletions: deletions
         })
      }
   )
}

DeleteComponentsAsyncAction.propTypes = {
   components: PropTypes.array
}

export default DeleteComponentsAsyncAction
