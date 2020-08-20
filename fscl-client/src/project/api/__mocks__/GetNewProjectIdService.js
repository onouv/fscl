/**
 * Mocked service for good case
 */

const code = "Test-Code";
const GetNewProjectIdService = (dispatchResult) => {
   console.log("MOCK: GetNewProjectIdService dispatching further");
   dispatchResult(code);
};

export default GetNewProjectIdService;
