import { FSCLHeader } from '../lib/ui/FSCLHeader';

export const SystemsHeader = ({saveable, deletable, project}) =>
   <FSCLHeader
      viewName={ViewNames.SYSTEMS}
      saveable={saveable}
      deletable={deletable}
      project={project}/>
