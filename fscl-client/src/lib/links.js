/**
 * an object containing all internal links to the different parts of the
 *
 *
 * @type {Object}
 */
export const links = {
   projects: {
      url: () => `/projects`,
      path: () => `/projects`
   },
   functions: {
      url: (project) => `/functions/${project}`,
      path: () => `/functions/:project`,
      systems: {
         url:    (entityId) => `/functions/systems/${entityId.project}/${entityId.entity}`,
         path: () => `/functions/systems/:project/:entity`
      },
      components: {
         url:  (entityId) => `/functions/components/${entityId.project}/${entityId.entity}`,
         path: () => `/functions/components/:project/:entity`
      }
   },
   systems: {
      url:  (project) => `/systems/${project}`,
      path: () => `/systems/:project`
   },
   components: {
      url:  (project) => `/components/${project}`,
      path: () => `/components/:project`,
      functions: {
         url:  (entityId) => `/components/functions/${entityId.project}/${entityId.entity}`,
         path: () => `/components/functions/:project/:entity`
      },
      systems: {
         url:  (entityId) => `/components/systems/${entityId.project}/${entityId.entity}`,
         path: () => `/components/systems/:project/:entity`
      },
      locations: (entityId) => `/components/locations/${entityId.project}/${entityId.entity}`
   },
   locations: {
      url: (project) => `/locations/${project}`,
      path: () => `/functions:project`
   }
}

export default links;
