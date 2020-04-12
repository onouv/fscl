"use strict;"
import getNewProjectId from '../api/GetNewProjectIdService';
import saveProjects from '../api/SaveProjectsService';
import getProjectList from '../api/GetProjectListService';
import deleteProject from '../api/DeleteProjectService';

export const ProjectActionTerms = {
   NEW_PROJECT: "PROJECT/NEW_PROJECT",
   SAVE_PROJECTS: "PROJECT/SAVE_PROJECTS",
   DOWNLOAD_PROJECTS: "PROJECT/DOWNLOAD_PROJECTS",
   DELETE_PROJECT: "PROJECT/DELETE_PROJECT",
   CHANGE_PROJECT: "PROJECT/CHANGE_PROJECT",
   TOGGLE_FLAG_PROJECT: "PROJECT/TOGGLE_FLAG_PROJECT",
   RAISE_ERROR: "PROJECT/RAISE_ERROR"
}

export const deleteProjectAsyncAction = projects => dispatch =>
   deleteProject(
      projects,
      (projects) => {
         dispatch({
            type: ProjectActionTerms.DELETE_PROJECT,
            projects
         });
      }
   )

export const saveProjectsAsyncAction = projects => dispatch =>
   saveProjects(
      projects,
      (projects) => {
         dispatch({
            type: ProjectActionTerms.SAVE_PROJECTS,
            projects
         })
      }
   );

export const newProjectAsyncAction = () => dispatch =>
   getNewProjectId(
      code => {
         dispatch(
            {
               type: ProjectActionTerms.NEW_PROJECT,
               code
            }
         );
      },
      error => {
         dispatch(
            {
               type: ProjectActionTerms.NEW_PROJECT,
               error
            }
         )
      }
   );

export const loadProjectsAsyncAction = () => dispatch =>
   getProjectList(
      projects => {
         dispatch(
            {
               type: ProjectActionTerms.DOWNLOAD_PROJECTS,
               projects
            }
         );
      },
      error => {
         dispatch(
            {
               type: ProjectActionTerms.RAISE_ERROR,
               error
            }
         )
      }
   );

export const changeProjectCodeAction = (code, change) => (
   {
      type: ProjectActionTerms.CHANGE_PROJECT,
      code,
      project: {
         code: change,
         name: null,
         description: null
      }
   }
);

export const changeProjectNameAction = (code, change) => (
   {
      type: ProjectActionTerms.CHANGE_PROJECT,
      code,
      project: {
         code: code,
         name: change,
         description: null
      }
   }
);

export const changeProjectDescriptionAction = (code, change) => (
   {
      type: ProjectActionTerms.CHANGE_PROJECT,
      code,
      project: {
         code: code,
         name: null,
         description: change
      }
   }
);

export const toggleFlagProjectAction = (code) => (
   {
      type: ProjectActionTerms.TOGGLE_FLAG_PROJECT,
      code
   }
);

export const raiseErrorAction = (error) => (
   {
      type: ProjectActionTerms.RAISE_ERROR,
      error
   }
);
