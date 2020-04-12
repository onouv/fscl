/**
 * Mocked service
 */

const projects = [
   {
    code: "001",
    name: "dummy 1",
    description: "dummy 1",
    changed: false,
    error: null
   },
   {
    code: "002",
    name: "dummy 2",
    description: "dummy 2",
    changed: false,
    error: null
   },
   {
    code: "003",
    name: "dummy 3",
    description: "dummy 3",
    changed: false,
    error: null
   }
];

const GetProjectListService = (dispatchResult, dispatchError) => {
   console.log("MOCK: GetProjectListService dispatching further")
   dispatchResult(projects);
};

export default GetProjectListService;
