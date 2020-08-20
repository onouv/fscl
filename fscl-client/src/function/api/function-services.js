"use strict;"
import { urls } from '../../lib/api.js'
import service from '../../lib/api/NewEntityIdService'

const requestNewFunctionIds = (
   projectCode,
   parents,
   dispatchResult=f=>f,
   dispatchRootFail=f=>f) => {

   service.requestNewIds(
      urls.functions.new(projectCode),
      parents,
      dispatchResult,
      dispatchRootFail)
}

export default {
   requestNewIds: requestNewFunctionIds
}
