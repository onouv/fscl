export default class Content {

   constructor({name="", description=""} = {}) {
      this._name = name
      this._description = description
   }

   get name() {
      return this._name
   }

   get description() {
      return this._description
   }

   get value() {
      return {
         name: this._name,
         description: this._description
      }
   }

   toString() {
      return `{name:${this._name}, description:${this._description}}`
   }
}
