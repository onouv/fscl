import { createStore, applyMiddleware, combineReducers } from 'redux'
import thunk from 'redux-thunk'

import functions from './function/redux/FunctionReducer'
import projects from './project/redux/ProjectReducer'
import components from './component/redux/ComponentReducer'
import modal from './lib/modals/ModalReducer'

// Note: the names given to reducers here will define the branch name in the
// application state (this is a feature of redux.combineReducers()). Please
// consider this when connecting the state to component props.
//
// So, the state will be structured like specified in fscl/doc/design/api/state


// middleware for logging the state
const logger = store => next => action => {
   if(action.type) {
      let result;
      console.groupCollapsed("dispatching", action.type);
      console.log('prev state', store.getState());
      console.log('action', action);
      result = next(action);
      console.log('next state', store.getState());
      console.groupEnd();
      return result;
   } else {
      return next(action);
   }

}

const reducerTree = combineReducers({
   functions,
   components,
   projects,
   modal
});

const initialState = {
   modal: {
      modalType: null,
      modalProps: {}
   },
   functions: {
      entities: [],
      controls: {
         saveable: false,
         deletable: false,
         linkable: false
      },
      links: {
         source: null,
         components: [],
         targets: []
      },
      error: null
   },
   components: {
      entities: [],
      controls: {
         saveable: false,
         deletable: false,
         linkable: false
      },
      links: {
         source: null,
         functions: [],
         targets: []
      },
      error: null
   },
   projects: {
      projects: [],
      controls: {
         saveable: false,
         deletable: false
      }
      // no error
   }
};

const storeFactory = () =>
   applyMiddleware(logger, thunk)(createStore)(reducerTree, initialState)

export default storeFactory;
