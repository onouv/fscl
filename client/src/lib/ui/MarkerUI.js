import React from 'react';
import MarkedFlag from './MarkedFlagUI'

export const MarkerUI = ({entity, onMarkChange}) => {

   const handleFlagChange = handler => event => {
      const id = event.target.id;
      const projectEntitySeparatorIndex = id.indexOf('*')
      const project = id.slice(0, projectEntitySeparatorIndex)
      const entity = id.slice(projectEntitySeparatorIndex + 1, id.lastIndexOf('*'))
      const code = { project: project, entity: entity }
      handler(code);
   }

   function buildId(code, uid) {
      return `${code.project}*${code.entity}*${uid}`
   }

   return (<MarkedFlag
      code={entity.self}
      checked={entity.marked}
      onChange={handleFlagChange(onMarkChange)}
      buildId={buildId}
   />)
}

export default MarkerUI
