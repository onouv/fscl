"use strict;"
import { clone, cloneDeep } from 'lodash';
import { ProjectActionTerms } from './project-actions';
import fscl from '../../lib/lib.js';
import controls from './ControlsReducer';

/**
 *    reducertree
 *       --> projects --> controls
 *       (--> functions)
 */

const ProjectsReducer = (state={}, action) => {

   let projects = [];

   switch (action.type) {
      case ProjectActionTerms.INIT:
         // initialize empty state for initial display before/ while loading
         // anything
         return {
            controls: {
               saveable: false,
               deletable: false
            }
         };

      case ProjectActionTerms.NEW_PROJECT:
         // attach a new project with the code returned from server and
         // otherwise empty data fields to state, or an error-tagged project
         return clonedStateWithNewProject(state, action);

      case ProjectActionTerms.SAVE_PROJECTS:
         projects = newProjects(action.projects);
         return {
            projects: projects,
            controls: controls(projects, action)
         }

      case ProjectActionTerms.DOWNLOAD_PROJECTS:
         return {
            projects: initProjects(action),
            controls: controls([], action)
         };

      case ProjectActionTerms.DELETE_PROJECT:
         projects = newWithoutDeletedProjects(action.projects);
         return {
            projects: projects,
            controls: controls(projects, action)
         }

      case ProjectActionTerms.CHANGE_PROJECT:
         projects = clonedWithChangedProject(state, action.code, action.project)
         return {
            projects: projects,
            controls: controls(projects, action)
         };

      case ProjectActionTerms.TOGGLE_FLAG_PROJECT:
         projects = clonedWithToggledProject(state.projects, action.code)
         return {
            projects: projects,
            controls: controls(projects, action)
         };

      case ProjectActionTerms.RAISE_ERROR:
         return {
            error: action.error,
            controls: controls([], action)
         };

      default:
         return state;
   }
};

function initProjects(action) {
   let projects = [];

   action.projects.forEach(project => {
      project.pristine = false;
      project.error = null;
      project.changed = false;
      project.marked = false
      projects.push(project);
   });

   return projects;
}

function newProjects(projects) {

   let newProject = {}
   const newProjects = []

   projects.forEach((project)=> {
      newProject = {...project};
      if(project.error) {
         //hasError = true;
         newProject.changed = true
         newProject.marked = true
         newProject.pristine = true;
      } else {
         newProject.changed = false
         newProject.marked = false
         newProject.pristine = false;
      }
      newProjects.push(newProject);
   });

   return newProjects;
}

function newWithoutDeletedProjects(projects) {

   const newProjects = []

   projects.forEach(p => {
      if(p.pristine || p.marked) {
         // we drop it for good
         console.log(`DELETE: dropping pristine project ${p.code}`);
      }
      else {
         if(!p.error) {
            // we keep it for good
            newProjects.push(clone(p));
         } else {
            // mark for action
            p.marked = true
            // we otherwise keep it as is (error flagged)
            newProjects.push(clone(p));
            //hasError = true;

            const warning = `DELETE: project ${p.code} returns error ${p.error}, \
               expect no delete on server.`
            console.warn(warning);
         }
      }
   });

   return newProjects;
}

function clonedStateWithNewProject(state, action) {
   let newProject = {};
   let newState = {
      controls: {}
   };
   if(action.error) {
      newProject = {
         code: "",
         name: "",
         description: "",
         error: action.error,
         changed: true,
         marked: true,
         pristine: true
      };
      newState.controls.saveable = false;
   } else {
      newProject = {
         code: action.code,
         name: "",
         description: "",
         error: null,
         changed: true,
         marked: true,
         pristine: true
      }
      newState.controls.saveable = true;
   }
   newState.controls.deletable = true;

   // keep old projects and add the new project
   newState.projects = cloneDeep(state.projects);
   newState.projects.push(newProject);

   return newState;
}

/**
 * find the specified project in the state provided, update it,
 * mark as changed and error-free and return a new state copy.
 *
 * @param  {Object} [state={}]   [old state]
 * @param  {String} [code=""]    [id of project to update]
 * @param  {Object} [project={}] [update data for project]
 * @return {Object}              [new state]
 */
function clonedWithChangedProject(state={}, code="", project={}) {

   const projects = [];
   state.projects.forEach(old => {
      let projClone = {};
      if(old.code === code) {
         projClone.changed = true
         projClone.marked = true
         projClone.error = null;
         projClone.pristine = old.pristine;
         projClone.code =project.code;
         projClone.name = project.name
            ? project.name
            : old.name;
         projClone.description = project.description
            ? project.description
            : old.description;
      }
      else {
         projClone = clone(old);
      }
      projects.push(projClone);
   })

   return projects;
}

function clonedWithToggledProject(projectsFromState, code) {

   const clones = cloneDeep(projectsFromState);
   const idx = clones.findIndex(p=>(p.code === code));
   if(idx > -1) {
      let flag = fscl.variable.toggle(clones[idx].marked);
      clones[idx].marked = flag;
   }
   else {
      console.warn(`reducer expected to find project ${code} in app state.`);
   }

   return clones;
}


export default ProjectsReducer;
