# Application Views  

FSCL is a system of services and web clients built around a common information [meta model](../core-domain-model/index.md). The meta model can be basis for many different application views. A view is a set of functionalities and extensions to the meta model.

This document collects ideas of features for the application views which are planned to be initially implemented as micro services and associated web clients.

## [Automation View](automation-view/automation-view.md)

## [Process View](process-view/index.md)

## [Electrical View](electrical-view/index.md)

# Mechanical View

F: Allow specifying component weights and dimensions  
F: Allow specifying Component mechanical interface points (foundations, flanges, hinges, fastening points…). This may be done by assigning registered drawings  
F: Component spatial arrangements  
F: Allow data exchange to specialized CAD systems  

# Safety View

F: Allow specifying safety requirements as cleartext  
F: Allow assigning functions, systems and components to safety requirements  
F: Allow specifying safety implementations as free text with references to safety requirements  
F: Allow selecting existing functions, systems, components and locations as links in safety implementations  
F: Dito for registered documents and calculations in any of the other views  

# Change Tracking

F: If an element in any of the views has been linked to any other element in the same view or any other view and it is either changed or deleted, the original information as used in these associated use points must be maintained and the element must be marked as changed at the other link end. The element in its root view must be marked as linked and as deviating from these use points. It must be possible to maintain such deviations with respect to any number of use points.

# Document Management

F: Allow registering (uploading) of documents such as drawings and calculations, specs such as flow/ pressure curves and control cuircuit and PI diagrams for a pump system  
R: any document format shall be allowed
F: Allow viewing of pdf formatted documents  
F: Propose a unique standard drawing number, allow editing of this number but enforce uniqueness before saving  
F: Allow downloading of document  
F: Allow linking (n-m) of a specific document version to Functions, Components, Systems, Locations  
F: Allow saving of different versions of a document  

# [Weight Management View](weight-management/index.md)
