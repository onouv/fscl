export const Scopes = {
   FUNCTIONS: {
      BASE: "FUNCTIONS",
      LINKS: {
         COMPONENTS: "FUNCTIONS/LINKS/COMPONENTS",
         LOCATIONS:  "FUNCTIONS/LINKS/LOCATIONS",
         SYSTEMS:  "FUNCTIONS/LINKS/SYSTEMS",
      },
      TARGETS: {
         COMPONENTS: "FUNCTIONS/TARGETS/COMPONENTS",
         LOCATIONS:  "FUNCTIONS/TARGETS/LOCATIONS",
         SYSTEMS:  "FUNCTIONS/TARGETS/SYSTEMS",
      }
   },
   SYSTEMS: "SYSTEMS",
   COMPONENTS: {
      BASE: "COMPONENTS",
      LINKS: {
         FUNCTIONS: "COMPONENTS/LINKS/FUNCTIONS",
         LOCATIONS:  "COMPONENTS/LINKS/LOCATIONS",
         SYSTEMS:  "COMPONENTS/LINKS/SYSTEMS",
      },
      TARGETS: {
         FUNCTIONS: "COMPONENTS/TARGETS/FUNCTIONS",
         LOCATIONS:  "COMPONENTS/TARGETS/LOCATIONS",
         SYSTEMS:  "COMPONENTS/TARGETS/SYSTEMS",
      }
   },
   LOCATIONS: "LOCATIONS"
}


export const Types = {
   NEW: "ENTITY/NEW",
   LOAD: "ENTITY/LOAD",
   SAVE: "ENTITY/SAVE",
   DELETE: "ENTITY/DELETE",
   CHANGE: "ENTITY/CHANGE",
   TOGGLE_FLAG: "ENTITY/TOGGLE_FLAG",
   FOLD: "ENTITY/FOLD",
   UNFOLD: "ENTITY/UNFOLD",
   SELECT: "ENTITY/SELECT",
   MODAL: {
      HIDE: "ENTITY/MODAL/HIDE"
   },
   ERROR: "ENTITY/RAISE_ERROR"
}


export default {
   Types: Types,
   Scopes: Scopes
}
