import formats from './CodeFormats'
export default class Code {

   constructor({
      code,
      prefix=formats.functionFormat.prefix,
      separator=formats.functionFormat.separator } = {}) {

      this._prefix = prefix
      this._separator = separator

      // enforcing a few syntax rules:
      // 1) prefix must appear exactly at the first position
      if( !code.startsWith(prefix) ) {
         throw new Error("prefix must appear at the first position  of code")
      }

      // 2) separator must not show up in first postion of code
      if(code.startsWith(separator)) {
         throw new Error("value of separator must not show up in first postion of code")
      }

      // 3) code must not end with value of separator
      if(code.endsWith(separator)) {
         throw new Error("code must not end with value of separator")
      }

      this._code = code
   }

   get code() {
      return this._code
   }

   get prefix() {
      return this._prefix
   }

   get separator() {
      return this._separator
   }

   /*
    * =001.001.001 => =001.001
    * =001.100     => =001
    * =001         => null
    */
   get parent() {
      const i = this._code.lastIndexOf(this._separator)
      if(i < 0 )
         return null

      return this._code.slice(0, i)

   }
   /**
    * @param  {[type]} a a Code instance
    * @param  {[type]} b a Code instance
    * @return {[type]}   -1 if a should be sorted before b
    *                    1  if b should be sorted before a
    *                    0  if should be sorted equal
    *
    * a.self.entity: =001, b.self.entity: =002 : -1
    * a: =001, b: =001.001 : -1
    * a: =002, b: =002.001 : -1
    * a: =002, b: =002.002 : -1
    * a: =002.002, b: =002.001 : 1
    * a: =200.001, b: =200.001.002 : -1
    * a: =200, b: =200.001.002 : -1
    * a: =200, b: =200.001 : -1
    * a: =100, b: =200.001.002 : -1
    * a: =100.001, b: =200.001 : -1
    * a: =200, b: =100.001 : 1
    * a: =200.002.001, b: =100.001 : 1
    * a: =200.002.002, b: =200.002.001 : 1
    * a: #AAA/1AA, b: #AAA : 1
    * a: #AAA/1AA, b: #ABA : -1
    * a: #AAA/1AA, b: #AAA/2AA : -1
    *
    */
   static compare(a, b) {
      const compare = a.self.entity.localeCompare(
         b.self.entity,
         undefined,
         {numeric: true})

      return compare
   }


   get self() {
      const i = this._code.lastIndexOf(this._separator)
      return this._code.slice(i+1)
   }

   toString() {
      return this._code
   }
}
