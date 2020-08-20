class Error {

   constructor(name, message) {
      this._name = name
      this._message = message
   }

   get name() {
      return this._name
   }

   get message() {
      return this._message
   }

   get value() {
      return {
         name: this._name,
         message: this._message
      }
   }

   toString() {
      return `${this._name} : ${this._message}`
   }
}

export default Error;
