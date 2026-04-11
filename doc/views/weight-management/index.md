# Weight Management View

In some domains, for example aircraft or railway rolling stock design, management of the overall weight of a vehicle is success critical. In this view, this task is supported.

The view aggregates a total system weight balance based on the current design state. It maintains a record of all physical components in the design structure and summarizes the weight of all these components as far as known at the given moment in time.

This view computes certain performance indicators such as

- ratio of weighted components to overall number of components
- deviation from linearized estimation assuming all unassigned components follow a certain statistical weight distribution (Gauss, Weibull)
to allow system engineers to assess the maturity and/or completeness of the weight balance.

This view may compute combined weight of all components

- of same type
- constituting a system
- located at a given location

to allow system engineers to identify useful levers for weight reduction. The view may also rank components based on their relative weight to highlight points of largest impact on weight reduction.
