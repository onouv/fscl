"use strict;"
import PropTypes from 'prop-types';
import Error from '../Error'

const FetchRootLevelService = (url, dispatchResult, dispatchError) => {

   fetch(url)
      .then(res => res.json())
      .then(res => {
         if (res.error) {
            console.error(res.error);
            dispatchError(new Error("Error when fetching root level: ", res.error))
         }
         else {
            dispatchResult(res.entities);
         }
      })
      .catch(err => {
         console.error(err);
         dispatchError(new Error("Error when fetching root level: ", err.message))
      })
}


FetchRootLevelService.propTypes = {
   url: PropTypes.string.isRequired,
   projectCode: PropTypes.string.isRequired,
   dispatchResult: PropTypes.func.isRequired,
   dispatchError: PropTypes.func.isRequired
}

export default FetchRootLevelService;
