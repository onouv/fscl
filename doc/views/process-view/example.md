# Feedwater Supply

A **Feedwater system** should supply a downstream system with 300L/min water at a pressure of 1.8 bar. The upstream water supply has a pressure of 1.2 bar. The feedwater system should be able to bridge an interruption of the  upstream supply for 4 min. 

## Functional Decomposition

The process engineer makes a  function breakdown model as shown in table below. She works backwards from the output requirements, as she happens to now that the inflow can be scaled up as needed. She also decides to provide the output pressure by including a storage tank with an appropriate elevation (using static pressure to have the option able to operate 4 min on control backup power only) and control the output flow from there. The engineer knows that storing water is done with a storage tank and bringing water up into the tank is done with a pump, and she is as hands-on character, so she calls these functions actually "Storage Tank" rather than "Store Feedwater" and "Feed Water Pump" rather than "Elevate Feedwater" or "Raise Pressure" (which also could have bin OK for her, but not for her colleague which is even more hands-on). 

In addition, the process engineer defined sub-functions to make sure there is no over- or underfill of the storage tank in case something goes wrong. From looking at some drawings of the building drawings offline she determined the approximate dimensions of the tank and defined minimum and maximum filling levels during operation.  The storage tank also has a maximum header for the water at the input nozzle that generates water pressure for the filling pump to overcome.  

| Designator | Name                        |                                   |
| ---------- | --------------------------- | --------------------------------- |
| **=1**     | **Feed Water Supply**       |                                   |
|            | `Var: InputPressure`        | `1.2 bar`                         |
|            | `Var: OutputPressure`       | `1.8 bar`                         |
|            | `Var: FlowRate`             | `300 L/min`                       |
|            | `Var: Gaptime`              | `4 min`                           |
| **=1.1**   | **Control Downstream Flow** |                                   |
| **=1.2**   | **Storage Tank**            |                                   |
|            | `Var: Capacity`             | `1200 L`                          |
|            | `Var: MinHeader`            | `1250 mm`                         |
|            | `Var: MaxHeader`            | `4500 mm`                         |
|            | `Var: InputHeader`          | `4850 mm`                         |
| **=1.2.1** | **Prevent Overfill**        |                                   |
| **=1.2.2** | **Prevent Underfill**       |                                   |
| **=1.3**   | **Feed Water Pump**         |                                   |
|            | `Var: FlowRate`             | `400 L/min`                       |
|            | `Calc1:`                    | `FlowRate = {=1.FlowRate * 1.33}` |
|            | `Var: SuctionHeader`        | `0`                               |
|            | `Var: DischargeHeader`      | `=1.2.InputHeader`                |


Notice how the flow rates at the output and at the input are modeled: 

* The required output flowrate is specified as a variable
* To catch up with an empty storage tank, the engineer has modeled an explicit calculation `Calc1` which specifies that the Pump should allow for a 30% flow reserve. The calculation references the appropriate variable of the overall system which happens to cmpute 400 L/min as requirement for the pump.

> **Conclusion**
>
> This leads to a domain model fullfilling the following requirements
> * Functions can have any number of Variables
> * Functions can have any number of Calculations
> * Calculations allow connecting Variables of other Functions to provide values for their own variables

> **Variation**
>
> Initially, it will be enough to 
> (1) state in a Calculation that a variable is depending on a list of any other variables without giving the exact relation. This would allow tracking changes in the dependencies and indicating to the user that a Function must be reevaluated
> (2) allow for direct assignment of one variable to another


| Designator | Name                        |                             | Variation |
| ---------- | --------------------------- | --------------------------- | --------- |
| **=1**     | **Feed Water Supply**       |                             |           |
|            | `Var: InputPressure`        | `1.2 bar`                   |           |
|            | `Var: OutputPressure`       | `1.8 bar`                   |           |
|            | `Var: FlowRate`             | `300 L/min`                 |           |
|            | `Var: Gaptime`              | `4 min`                     |           |
| **=1.1**   | **Control Downstream Flow** |                             |           |
| **=1.2**   | **Storage Tank**            |                             |           |
|            | `Var: Capacity`             | `1200 L`                    |           |
|            | `Var: MinHeader`            | `1250 mm`                   |           |
|            | `Var: MaxHeader`            | `4500 mm`                   |           |
|            | `Var: InputHeader`          | `4850 mm`                   |           |
| **=1.3**   | **Feed Water Pump**         |                             |           |
|            | `Var: FlowRate`             | `400 L/min`                 |           |
|            |                             | `FlowRate <= {=1.FlowRate}` | (1)       |
|            | `Var: SuctionHeader`        | `0`                         |           |
|            | `Var: DischargeHeader`      | `=1.2.InputHeader`          | (2)       |

