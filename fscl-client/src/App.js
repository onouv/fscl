import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';

import { Home } from './Home';
import { ProjectContainer } from './project/redux/ProjectContainer';
import FunctionList from './function/FunctionList/FunctionList';
import LinkedComponentsList from './function/LinkedComponentsList/LinkedComponentsList'
import LinkedFunctionsList from './component/LinkedFunctionsList/LinkedFunctionsList'
import ComponentList from './component/ComponentList/ComponentList'
import ModalRoot from './lib/modals/ModalRoot'
import links from './lib/links'
import './App.css';

const LocoDummy = () => <h1>LOCATION View is not yet supported</h1>
const SysDummy = () => <h1>SYSTEM View is not yet supported</h1>

function App() {

   return (
      <div className="app">
         <Router>
            <Switch>
               <Route
                  path='/'
                  exact={true}
                  component={Home}/>
               <Route
                  path='/projects'
                  exact={true}
                  component={ProjectContainer}
               />
               <Route
                  path={`${links.functions.path()}`}
                  exact={true}
                  component={FunctionList}
               />
               <Route
                  path={`${links.functions.components.path()}`}
                  exact={true}
                  component={LinkedComponentsList}
               />
               <Route
                  path={`${links.systems.path()}`}
                  exact={true}
                  component={SysDummy}
               />
               <Route
                  path={`${links.components.path()}`}
                  exact={true}
                  component={ComponentList}
               />
               <Route
                  path={`${links.components.functions.path()}`}
                  exact={true}
                  component={LinkedFunctionsList}
               />
               <Route
                  path={`${links.locations.path()}`}
                  exact={true}
                  component={LocoDummy}
               />
            </Switch>
         </Router>
         <ModalRoot/>
      </div>
  );
}

export default App;
