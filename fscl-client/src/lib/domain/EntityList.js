import { isEqual, cloneDeep } from 'lodash'
import Code from './Code'

/**
 * A list to contain objects of the type
 *
 * {
 *    self: {
 *       project: "..."
 *       entity: "special format applies"
 *    },
 *    parent: {
 *       project: "..."
 *       entity: "special format applies"
 *    },
 *    children: [{
 *       project: "..."
 *       entity: "special format applies"
 *    },
 *    ...],
 *    content: {
 *       name: "...",
 *       description: "..."
 *    },
 *    error: bool,
 *    changed: bool,
 *    marked: bool,
 *    pristine: bool,
 *    unfolded: bool,
 *    foldingEnabled: bool
 * }
 *
 * or any type mapping to the above, such as ./Entity
 *
 * @param  {Array}  [list=[]] [description]
 * @return {[type]}           [description]
 */

function entityList(list = []) {

   list.findEntity = (code) => {
      const index = list.findIndex(e => {
         return (isEqual(e.self, code))
      })
      if(index >= 0)
         return list[index]
      else
         return null
   }

   list.findIndexOfEntity = (code) => list.findIndex(e => isEqual(e.self, code))

   list.findParentIndexOf = (code, format) => {

      const parentProjectCode = code.project
      const parentEntityCode = new Code({code: code.entity, ...format}).parent
      const index = list.findIndex(e =>
         (
            (isEqual(e.self.project, parentProjectCode)) &&
            (e.self.entity === parentEntityCode)
         )
      )

      return index
   }


   list.findChildIndexesOf = (code) => {

      const childIndexes = []

      for(let i=0; i < list.length; i++) {
         const e = list[i]
         if(e.self.project === code.project ) {
            if(e.self.entity.startsWith(code.entity)) {
               if(e.self.entity.length > code.entity.length) {
                  childIndexes.push(i)
               }
            }
         }
      }

      return childIndexes
   }

   list.upsert = (entity) => {
      const index =  list.findIndex(e => isEqual(e.self, entity.self))
      if(index > -1) {
         list.splice(index, 1, entity)
      } else {
         list.push(entity)
      }

      list.sortByCode()

   }

   /**
    * [addChildCode description]
    * @param {[EntityCode.value]} childCode an object like {project: "...", entity: "..."}
    */
   list.addChildCode = ( childCode ) => {

      list.push(childCode)
      //list.sort(...)

   }

   list.sortByCode = () => {
      list.sort((a, b) => {
         const comp = compareProjectCode(a.self.project, b.self.project)
         if(comp === 0) {
            // investigate the entity field
            return compareEntityCode(a.self.entity, b.self.entity)
         } else {
            // project field prevails
            return comp
         }
      })
   }

   list.pickAllExcept = (exclusions) => {

      const allExcept = []

      list.forEach(e => {
         if(exclusions.findIndex(x => isEqual(e.self, x.self)) < 0) {
            allExcept.push(e)
         }
      })

      return allExcept
   }

   list.clone = () => {
      const clones = cloneDeep(list)
      return clones
   }

   return list
}

function compareProjectCode(a, b) {

   const compare = a.localeCompare(b, undefined)
   return compare

}

function compareEntityCode(a, b) {
   const compare = a.localeCompare(
      b,
      undefined,
      {numeric: true})

   return compare
}


export default entityList
