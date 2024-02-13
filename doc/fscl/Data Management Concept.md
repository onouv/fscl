
There is a [[Core Domain Model]] containing certain domain entities and links between them. Both of these things we call elements. The main characteristics of this core model are:
* the entities are structured in deep hierarchy trees (parent-child in 1-n)
* the entities may be linked within and across these trees 

For brevity we do not show links but only parent-child reations in the diagrams below but they are equally treated as *elements* and are  managed by the same concepts. 

Also, the core domain model has Functions, Components, Systems, Locations. All nodes shown below can be of any kind of the four types, in their respective hierachies. Links therefore can be spanning between different types of nodes as well, as they are all treated as *elements*, it makes no difference for the data management concept.  

We want to  **avoid a centralized data storage** for this complex information:
* to avoid a performance bottleneck
* to avoid a reliability problem (single point of failure)
* to allow easy addition and changing of views without having to change existing code bases of existing views or of a central service

We must **ensure all views are eventually updated** about all changes in all views, so they can *decide if a change is relevant for them*.

Each view must maintain its view specific model (**view model**) and a complete copy of the common model (**shadow model**). The latter is eventually maintained consistent with all other views. There is no central copy of the common model.

Different Views  maintain a different selection (subtrees and link sets) of these elements, i.e. a view model may choose only a subset of all elements in the shadow model. However, the relations of the elements that are picked must be maintained consistent on both models (e.g a is parent of b in both models):

```mermaid
flowchart TD
subgraph View Model
aV(a)
bV(b)
aV --> bV
end

subgraph Shadow Model
aS(a)
bS(b)
eS(e)
dS(d)
aS --> bS
bS --> dS
aS --> eS
end

aS -.- aV
bS -.- bV
```

## View Creates New Element

When a view wants to create a new element, 

(1) it must check in the shadow model if there is an appropriate candidat.  If there is, it must decide if that should be used 

If there is no such candidate or if it shall not be used, the view must
(2) create a new element in the view model
(3) dito in the shadow model
(4) link the two	
(5) Publish the creation event to the bus, so other views can react to it (see below)

(6 When a view subsequently receives the creation event from the bus (by means of its subscription), it must ignore it, since it already finds the  element in its shadow

(7) When one of the other views receives the creation event, they will not find the element in their shadow model copy and will introduce it to its shadow model. They must eventually propose the new element for decision wether or not it should be introduced to the view model. 
```mermaid
flowchart TD
	
	subgraph ViewA
		User
		
		subgraph ViewModel
			aV(a)
			bV(b)
			eV(e)
			
			aV --> eV
			aV --> bV
		end
		
		subgraph ShadowModel
			aS(a)
			bS(b)
			cS(c)
			dS(d)
			eS(e)
			
			ig{{ignore}}
			aS --> bS
			bS --> dS
			aS --> cS
			aS --> eS
			ig -.-> eS
		end
		User -. (3) .-> eS
	end

User -. (1) .-> ShadowModel
User -. (2) .-> eV

eV -. (4) .- eS
eS -- (5) --> Bus
Bus -- (6) --> ig
Bus -- (7) --> ViewB
```

=======================
```plantuml
!pragma useVerticalIf on
|c| Web Client
start
:Enter Element; <<input>>
:Click SAVE; <<input>>
:POST CreateRequest to Server; <<output>>
|s| Server
:Look Up Shadow-Element by ID;
if (element in shadow) then (yes)
	if (Diff Content of Request and Shadow) then (identical)
		:Respond 409 (CONTENT_IDENTICAL); <<output>>
	else (different)
		:Respond 409 (CONTENT_DIFFERENT, Shadow-Element); <<output>>
	endif
	|c|
	:409; <<input>>
	if (Response) then (CONTENT_IDENTICAL)
		:Show Cancel Option;
		if (User) then 
			:Click CANCEL;<<input>>
			:Clean UI;
			stop
		else 
			:Click OK; <<input>>
			:MergeRequest(SHADOW);
		endif
	else (CONTENT_DIFFERENT)
		:Show Content Decision Page from reponse data and original data;
		if (User) then 
			:Click OVERWRITE; <<input>>
			:MergeRequest(CLIENT, original data);
		else
			:Click KEEP SERVER; <<input>>
			:MergeRequest(SHADOW);
		endif
		
	endif
	:PUT MergeRequest; <<output>>
	|s|
	:Look Up Shadow-Element by ID;
	if (MergeRequest) then (CLIENT)
		:Overwrite Shadow-Element with request data (Flag MODIFIED);
		:Create View-Element from request data;
		:Link Shadow-Element and View-Element;
		:publish ModifiedEvent; <<output>>
		:Respond 201; <<output>>
	else (SHADOW)		
		:Copy Shadow-Element to ViewModel;
		:Merge additional view Content from request data into View-Element;
		:Link Shadow-Element and View-Element;		
		:Respond 204; <<output>>	
	endif
else (no)
	|s|
	:Create Shadow-Element from request data (Flag MODIFIED);	
	:Create View-Element from request data;
	:Link Shadow-Element and View-Element;
	:publish CreatedEvent; <<output>>
	:Respond 201; <<output>>	
endif
|c|
:Response 201 or 204; <<input>>
:Udate UI;
stop
```


## View Deletes an Element

When a View decides to delete an element, a consensual decision must be made wether or not this should happen in the shadow models of all views as well.

The Deleting View must 
(1) delete the element from its view model and shadow model
(2) publish the fact to the bus (DELETE event)
(3) flag the shadow element with DELETED 

The bus spreads the DELETE event to all views

(3) When the initiating view receives the DELETE event, it finds the element flagged as DELETED, and therefore ignores the event

(4) other views which are not using the element in their respective view models must delete the shadow element once they receive the DELETE event

(5) other views which DO use the element in their own model view must NOT delete anything, but instead 

(6) issue a RECREATE event for that very element, so that 

(7) any view that is NOT using the element in its view model must  **create it in shadow model with eventual proposal for view model**  and

(8) the view which originally initated the deletion can trigger a **reset the DELETED flag in the shadow element without eventual proposal for view model** 



