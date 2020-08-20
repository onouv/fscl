
const project = {
   code: "002",
   name: "dummy 2",
   description: "dummy 2",
   changed: true,
   pristine: false
};

const DeleteProject = (project) =>  new Promise((resolve) => {
      console.log("MOCK DeleteProject resolves");
      resolve(project);
   });

export default DeleteProject;
