
import Content from './Content'
import EntityCode from './EntityCode'
import entityList from './EntityList'
import fscl  from '../lib'

export default class Entity {

   static State = Object.freeze({
      PRISTINE: 1, SAVED: 2, CHANGED: 3, ERROR: 0, UNDEFINED: -1
   })

   static fromFlat(e) {
      return ({
         self: e.self,
         parent: e.parent,
         children: e.children,
         content: { name: e.name, description: e.description },
         error: e.error
      })
   }

   constructor({
         initialState,
         foldingEnabled,
         unfolded,
         format,
         self = {project: "", entity: ""},
         parent = null,
         children = [],
         content = {name: "", description: ""},
         error = null
      } = {}) {

      switch(initialState) {
         case Entity.State.PRISTINE:
            this._pristine = true
            this._marked = true
            this._changed = true
            this._foldingEnabled = false
            this._unfolded = false
            break

         case Entity.State.CHANGED:
            if(fscl.variable.exists(unfolded)) {
               this._pristine = false
               this._marked = true
               this._changed = true
               this._foldingEnabled = false
               this._unfolded = unfolded
            } else throw new Error("Entity.constructor(initialState: CHANGED): must specify parameter unfolded (bool)")
            break

         case Entity.State.SAVED:
         default:
            this._pristine = false
            this._marked = false
            this._changed = false
            this._foldingEnabled = (children.length > 0) ? true: false
            this._unfolded = (children.length > 0) ? unfolded: false

      }

      this._self = new EntityCode({format: format, ...self})
      this._parent = parent ? new EntityCode({format: format, ...parent}) : null
      this._children = children
      this._content = new Content(content)
      this._error = error
   }

   get state() {
      if(this._error != null) {
         return Entity.State.ERROR
      }

      if(this._pristine === true) {
         if(this._changed === true) {
            if(this._marked === true) {
               if(this._unfolded === false) {
                  if(this._foldingEnabled === false) {
                     return Entity.State.PRISTINE
                  }
               }
            }
         }
      } else {
         if(this._changed === false) {
            if(this._children.length > 0) {
               if(this._foldingEnabled === true) {
                  return Entity.State.SAVED
               }
            } else {
               if(this._foldingEnabled === false) {
                  return Entity.State.SAVED
               }
            }
         } else {
            if(this._foldingEnabled === false) {
               return Entity.State.CHANGED
            }
         }
      }

      return Entity.State.UNDEFINED
   }

   get parent() {
      return this._parent
   }

   set parent(parent) {
      this._parent = parent
   }

   get self() {
      return this._self
   }

   get children() {
      return entityList(this._children)
   }

   get content() {
      return this._content
   }

   get changed() {
      return this._changed
   }

   set changed(bool) {
      this._changed = bool
   }

   get marked() {
      return this._marked
   }

   set marked(bool) {
      this._marked = bool
   }

   get pristine() {
      return this._pristine
   }

   set pristine(bool) {
      this._pristine = bool
   }

   get unfolded() {
      return this._unfolded
   }

   set unfolded(bool) {
      this._unfolded = bool
   }

   get foldingEnabled() {
      return this._foldingEnabled
   }

   set foldingEnabled(bool) {
      this._foldingEnabled = bool
   }

   get error() {
      return this._error
   }

   get value() {
      return {
         self: this._self.value,
         parent: this._parent ? this._parent.value : null,
         children: this._children,
         content: this._content.value,
         error: this._error,
         changed: this._changed,
         marked: this._marked,
         pristine: this._pristine,
         unfolded: this._unfolded,
         foldingEnabled: this._foldingEnabled
      }
   }

   toString() {
      return `{code=${this._self.toString()}, changed=${this._changed}, pristine=${this._pristine}, content=${this._content.toString()}`
   }
}
