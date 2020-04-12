import { FSCLHeader } from '../lib/ui/FSCLHeader';

export const LocationsHeader = ({saveable, deletable, project}) =>
   <FSCLHeader
      viewName={ViewNames.LOCATIONS}
      saveable={saveable}
      deletable={deletable}
      project={project}/>
