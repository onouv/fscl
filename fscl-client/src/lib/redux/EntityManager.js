import { cloneDeep, isEqual } from 'lodash'
import fscl from '../lib.js'
import Entity from '../domain/Entity'
import Code from '../domain/Code'
import entityList from '../domain/EntityList'


//
// **********************************************************************
// A set of helper functions to manage Entity state for a reducer
//
// **********************************************************************
//

function setup(rawEntities, format) {
   const refined = rawEntities.map(e => {
      const clone = cloneDeep(e)
      const newEntity = new Entity({
         initialState: Entity.State.SAVED,
         format: format,
         unfolded: false,
         foldingEnabled: false,
         content: {name: clone.name, description: clone.description},
         ...clone })

         return newEntity.value
      })

   const sorted = entityList(refined)
   sorted.sort(Code.compare)

   return sorted.clone()
}

function clone(entitiesFromState) {
   const entities = entityList(entitiesFromState)
   return entities.clone()
}

function withToggledMarkerFlag(entitiesFromState, code) {
   const entities = entityList(entitiesFromState)
   const idx = entities.findIndexOfEntity(code)
   if(idx > -1) {
      entities[idx].marked = fscl.variable.toggle (entities[idx].marked)
   } else {
      console.error(`helper function EntityManager:clone.withToggledMarkerFlag expected to find  ${code} in app state.`)
   }
   return entities.clone()
}

/**
 * For each EntityCode in newCodes, create a new Entity and
 * a) insert it at the right spot into a clone of entitiesFromState
 * b) add it to the parent, if that should exist
 * c) set the parent field to the code of the parent, if that should exist
 *
 * Root-level coded entities will be appended to the end of the list.
 * child-level coded entities will be inserted right below, parent field will be
 * updated accordingly
 *
 * @param  {[type]}  entitiesFromState [description]
 * @param  {[type]}  newCodes            a list of EntityCodes
 * @return {[type]}  a deep clone of entitiesFromState, with any new entities
 *                   inserted as required by newIds
 */
function withNewEntitiesFromCodes(
   entitiesFromAction,
   newCodes,
   format) {

   if(fscl.variable.exists(newCodes)) {

      const entities = entityList(entitiesFromAction)

      newCodes.forEach(code => {
         const newEntity = new Entity({
            initialState: Entity.State.PRISTINE,
            self: code,
            format: format
         })

         const parentIndex =  entities.findParentIndexOf(code, format)
         if(parentIndex >= 0) {
            const rawParent = entities[parentIndex]
            const parent = new Entity({
               initialState: Entity.State.CHANGED,
               unfolded: true,   // we will add child(ren), so show them...
               format: format,
               ...rawParent
            })

            newEntity.parent = parent.self
            parent.children.addChildCode(newEntity.self.value)
            entities[parentIndex] = parent.value
         }

         entities.push(newEntity.value)
      })

      entities.sort(Code.compare)
      return entities.clone()

   } else {
      return cloneDeep(entitiesFromAction)
   }
}


function withChangedEntity(entitiesFromState, entityChange) {

   const withChanged = []
   const fromState = entityList(entitiesFromState)

   fromState.forEach(e => {

      const clone = cloneDeep(e)

      if(isEqual(e.self, entityChange.code)) {

         clone.content.name = entityChange.name
            ? entityChange.name
            : e.content.name

         clone.content.description = entityChange.description
            ? entityChange.description
            : e.content.description

         // mark us for being caught in next SAVE
         clone.changed = true

         // as a user convenience, we enable future SAVE action for this entity
         clone.marked = true

         // in CHANGED, our parent must not FOLD/UNFOLD, as we a re inconsistent
         // with the server and future UNFOLDs may overwrite our unsaved state
         //clone.foldingEnabled = false
         const parentIdx =  fromState.findIndexOfEntity(e.parent)
         if(parentIdx >= 0) {
            withChanged[parentIdx].foldingEnabled = false
         }

         clone.error = null
      }

      withChanged.push(clone)
   })

   return withChanged;
}


/**
 * Clone the entitiesFromState, for each of the items in savedEntities, update
 * the corresonding cloned entity, append otherwise, return the clone.
 */
 function withSavedEntities(entitiesFromState, savedEntities, format) {

   const fromState = cloneDeep(entitiesFromState)
   const forState = entityList(fromState)

   savedEntities.forEach(saved => {
      const raw = cloneDeep(saved)
      const clone = new Entity({
         initialState: Entity.State.SAVED,
         format: format,
         ...raw })

      // if we have a parent, we must allow it to FOLD/UNFOLD again now.
      // It would have been blocked frm doing so during CHANGE cycle to
      // prevent loss of our unsaved data changes
      if(fscl.variable.exists(clone.parent)) {
         const parentIdx = fromState.findIndexOfEntity(clone.parent.value)
         if(parentIdx >= 0) {
            forState[parentIdx].foldingEnabled = true
         }
      }

      const i = forState.findIndexOfEntity(clone.self.value)
      if(i >= 0) {
         forState.splice(i, 1, clone.value)
      } else {
         forState.push(clone.value)
      }
   })

   return forState.clone()
}


function withNewChildrenOfParent(
   entitiesFromState,
   parent,
   children,
   format) {

   const forState = entityList(entitiesFromState)

   children.forEach(child => {
      const newChild = new Entity({
         initialState: Entity.State.SAVED,
         format: format,
         ...Entity.fromFlat(child)
      })
      forState.push(newChild.value)
   })
   forState.sort(Code.compare)

   // mark parent...
   const p = forState.findEntity(parent.self)
   if(fscl.variable.exists(p)) {
      p.unfolded = true;
   } else {
      throw new Error(`Severe inconsistency: could not find \
         ${parent.self.toString()} in state !`)
   }

   return forState.clone()
}

function withoutChildrenOfParent(entitiesFromState, parent) {

   // clone the state
   const clones = entityList(cloneDeep(entitiesFromState))

   // find the children and delete them...
   const indexes = clones.findChildIndexesOf(parent.self)
   // as we are deleting from the array, we walk backwards so
   // we can use the indices gained from previous instruction
   for(let i=indexes.length-1; i>=0; i--) {
      const idx = indexes[i]
      clones.splice(idx, 1)
   }

   // unmark parent...
   const clonedParent = clones.findEntity(parent.self)
   clonedParent.unfolded = false

   return clones
}


/**
 * Delete all those entities from the state which .self members are matching
 * the deletions unless there is an error indicated in deletion.
 * In that case, maintain the entity in the list and update its error member,
 * so it can be shown. 
 *
 * Return the updated list
 */
function withoutDeletedEntities(
   entitiesFromState,
   entitiesReqToDelete,
   actualDeletions) {

      const fromState = entityList(entitiesFromState)

      actualDeletions.forEach(del => {

         const deletion = del
         if(deletion.error && deletion.error != null)  {
            // handle error: all entities originally requested to be
            // deleted must be marked with the error
            entitiesReqToDelete.forEach(entity => {
               const i = fromState.findIndexOfEntity(entity.self)
               if(i >= 0) {
                  fromState[i].error = deletion.error
               } else {
                  logError(entity.self)
               }
            })

         } else {

            // server may have deleted unrequested children of a requested parent, so 
            // we walk through all of the deletedIds of the current deletion object
            deletion.deletedIds.forEach(id => {
               const i = fromState.findIndexOfEntity(id)   
               if(i >= 0) {
                  // remove the entity from state
                  fromState.splice(i, 1)
               } else {
                  logError(id)
               }
            })
         }
      })

      function logError(code) {
         const msg = `EntityManager.clone.withoutDeletedEntities: expected to find entity {${code.project} : ${code.entity}} in the state, but did not. This is okay in case server deleted children of requested parent which where not unfolded.`
         console.log(msg)
      }

      return fromState.clone()
}


function copyEntitiesWhenMarked(source, destination) {

   const to = entityList(destination)

   source.forEach(t => {
      if(t.marked) {
         to.upsert(t)
      }
   })

   return to.clone()
}

export default {
   init: {
      //entities: setup,
      rawEntities: setup
   },
   clone: {
      entities: clone,
      withNewChildrenOfParent: withNewChildrenOfParent,
      withoutChildrenOfParent: withoutChildrenOfParent,
      withNewEntitiesFromCodes: withNewEntitiesFromCodes,
      withToggledMarkerFlag: withToggledMarkerFlag,
      withChangedEntity: withChangedEntity,
      withSavedEntities: withSavedEntities,
      withoutDeletedEntities: withoutDeletedEntities,
      withCopiedEntitiesWhenMarked: copyEntitiesWhenMarked
   }
}
