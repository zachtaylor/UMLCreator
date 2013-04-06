UMLCreator
==========

CS361 group assignment

Team Rocket:

Sam Dembinski
Zach Taylor
Kaoutar Maghfour

Use Cases:

Use Case:  CreateDiagram
Participating Actors:  User
Priority Level:  High
Flow of Events:
1.	The user runs the application.
2.	A movable start and end point are automatically generated in the upper left and lower right corners of the screen, respectively.
3.	The user optionally edits and saves the diagram as they wish (See EditDiagram and SaveDiagram use cases).
Entry Conditions:
-	The program has been executed at some point
Exit Conditions:
-	A diagram exists, with a start and end point.


Use Case:  EditDiagram
Participating Actors:  User
Priority Level:  Medium
Flow of Events: 
1.	The user selects an edit option.
2.	The edit option is performed on the diagram. 
3.	The edit options are as follow: delete, creating state figure, create nested state figure, draw transition.
Entry Condition: 
-	The diagram exists.
Exit Condition: 
-	The new changes are shown on the diagram.


Use Case: CheckForErrors
Participating Actors: User
Priority Level: Low
Flow of Events:
1.  The user selects the check for errors option
2.  A pop-up window tells the user if their UML Diagram has errors or not
Entry Condition:
-   None
Exit Condition:
-   A pop-up report has been displayed


Use Case:  SaveDiagram
Priority Level:  High
Participating Actors: User
Flow of Events:
1.	User selects the “Save” option.
2.	If there is no path associated with the diagram, the user is prompted for a path.
3.	The file is saved to the specified path.
4.	The user is returned to the diagram view.
Entry Conditions:
-	There is a diagram open.
Exit Conditions:
-	The diagram has been saved to a file.
-	The diagram has not been altered.


Use Case:  OpenDiagram
Participating Actors:  User
Priority Level:  High
Flow of Events:
1.	The user selects “open” option.
2.	The user navigates to a predetermined file location and selects it.
3.	The diagram is displayed to the user.
Entry Condition: 
-	The file exists.
Exit Condition: 
-	The user sees the diagram


Use Case:  GenerateXML
Priority Level:  Low
Participating Actors: User
Flow of Events: 
1.	User selects the “Export XML” option.
2.	If there is no XML path associated with the current diagram, the user is prompted to specify a path different from the file path of the diagram.
3.	The file is saved to the newly specified path.
4.	The user is returned to the diagram view.
Entry Conditions:
-	There is a diagram currently open.
Exit Conditions:
-	The diagram has been saved in XML format to the specified path.
-	The diagram has not been altered.


Use Case:  DrawnDiagramSimulation
Participating Actors:  User
Priority Level:  Low
Flow of Events:
1.	The user selects the “Simulate” option.
2.	A text file is generated based off the currently open diagram listed each state, each state transition, and the effects after each transition.
Entry Conditions:
-	A diagram is currently open in the program
Exit Conditions:
-	A text file describing the states, transitions, and effects of each transition exists.
-	The diagram remains open.


Use Case:  XMLSimulation
Participating Actors:  User
Priority Level:  Low
Flow of Events:
1.	The user selects the “SimulateXML” option.
2.	A window opens allowing the user to select a file location.
3.	The user navigates to an already existing XML representation of a diagram.
4.	The user is prompted for an input file to be executed
5.  The user navigates to an already existing text file
6.	A text file is generated based off the XML representation listing the effects of each transition.
Entry Conditions:
-	An XML representation of a UML state diagram exists.
-	The user must generate an XML representation of a particular diagram if one does not already exist for it.
Exit Conditions:
-	A text file describing the states, transitions, and effects of each transition exists.
-	The program remains open.