import Code from './Code'

export default class EntityCode {

   constructor({ project = "", entity="", format } = {}) {

      this._project = project
      this._entity = new Code({code: entity, ...format})

   }

   get project() {
      return this._project
   }

   get entity() {
      return this._entity
   }

   get value() {
      return {
         project: this._project,
         entity: this._entity.code
      }
   }

   toString() {
      return `${this._project} : ${this._entity.toString()}`
   }

   static fromFlat(code) {
      return `${code.project} : ${code.entity}`
   }
}
