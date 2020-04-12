import React from 'react';
import { FSCLHeader } from '../../lib/core/ui/FSCLHeaderUI';
import ViewNames from '../../lib/core/ui/ViewNames';

export const FunctionsHeader = ({saveable, deletable, project}) =>
   <FSCLHeader
      viewName={ViewNames.FUNCTIONS}
      saveable={saveable}
      deletable={deletable}
      project={project}
   />


export default FunctionsHeader;
