"use strict;"

export const urls = {

   functions: {

      base: 'http://localhost:8081/api/v4/functions',

      // http://localhost:8081/api/v4/functions/P001200
      functions : (projectCode="") => {
         const base = urls.functions.base
         return ( `${base}/${projectCode}` )
      },

      // http://localhost:8081/api/v4/functions/{projectCode}/{entityCode}
      function: (entityCode) => {
         const functions = urls.functions.functions(entityCode.project)
         return (`${functions}/${entityCode.entity}`)
      },

      // http://localhost:8081/api/v4/functions/new/P001200
      new: (projectCode) => {
         const url = `${urls.functions.base}/new/${projectCode}`
         return url
      },

      components: (entityCode) => {
         const url = `${urls.functions.base}/components/${entityCode.project}/${entityCode.entity}`
         return url
      },

      link: {

         // /api/v4/functions/link/components/{project}/{entity}
         components: (entityId) => {
            const url = `${urls.functions.base}/link/components/${entityId.project}/${entityId.entity}`
            return url
         }
      },

      unlink: {

         // /api/v4/functions/unlink/components/{project}/{entity}
         components: (entityId) => {
            const url = `${urls.functions.base}/unlink/components/${entityId.project}/${entityId.entity}`
            return url
         }
      }
   },
   components: {

      base: 'http://localhost:8083/api/v4/components',

      // http://localhost:8083/api/v4/components/functions/{proj}/{ent}
      functions: (entityCode) => {
         const url = `${urls.components.base}/functions/${entityCode.project}/${entityCode.entity}`
         return url
      },

      link: {

         // http://localhost:8083/api/v4/components/link/functions/{project}/{entity}
         functions: (entityId) => {
            const url = `${urls.components.base}/link/functions/${entityId.project}/${entityId.entity}`
            return url
         }
      },

      unlink: {

         // http://localhost:8083/api/v4/components/unlink/functions/{project}/{entity}
         functions: (entityId) => {
            const url = `${urls.components.base}/unlink/functions/${entityId.project}/${entityId.entity}`
            return url
         }

      },

      // http://localhost:8081/api/v4/components/P001200
      components : (projectCode="") => {
         const url = `${urls.components.base}/${projectCode}`
         return url
      },

      // http://localhost:8081/api/v4/functions/{projectCode}/{entityCode}
      component: (entityCode) => {
         const components = urls.components.components(entityCode.project)
         const url = `${components}/${entityCode.entity}`
         return url
      },

      // http://localhost:8081/api/v4/functions/new/P001200
      new: (projectCode) => {
         const url = `${urls.components.base}/new/${projectCode}`
         return url
      }

   },
   entities: {
      partial: (entityCode) => (`/${entityCode.project}/${entityCode.entity}`)
   },
   projects: {
      base: 'http://localhost:8080/api/v4',
      projects: () => (`${urls.projects.base}/projects`),
      newId: () => (`${urls.projects.projects()}/newidrequest`),
      new: () => (`${urls.projects.projects()}/new`),
      update: (code) => (`${urls.projects.projects()}/${code}`),
      delete: (code) => (`${urls.projects.projects()}/${code}`)
   }
}

export const timeouts = {
   projects: {
      cachedCodes: {
         timeoutSeconds: "10"
      }
   }
}
