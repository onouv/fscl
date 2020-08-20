import { FSCLHeader } from '../lib/ui/FSCLHeader';

export const ComponentsHeader = ({saveable, deletable, project}) =>
   <FSCLHeader
      viewName={ViewNames.COMPONENTS}
      saveable={saveable}
      deletable={deletable}
      project={project}/>
