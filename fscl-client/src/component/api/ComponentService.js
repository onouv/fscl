import { urls } from '../../lib/api.js'
import service from '../../lib/api/NewEntityIdService'

const newComponentIdRequest = (
   projectCode,
   parents,
   dispatchResult=f=>f,
   dispatchRootFail=f=>f) => {

   service.requestNewIds(
      urls.components.new(projectCode),
      parents,
      dispatchResult,
      dispatchRootFail)
}


export default {
   requestNewIds: newComponentIdRequest
}
