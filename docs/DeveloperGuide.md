---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/HealthSync-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/HealthSync-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/HealthSync-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/HealthSync-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/HealthSync-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/HealthSync-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `HealthSyncParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `HealthSyncParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `HealthSyncParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/HealthSync-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* Please note all entity objects in the `Model` component are immutable.
* * stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

The `Person` entity,
* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.

The `Note` entity,
* each person has a list of `Note` objects which are stored in a `ObservableList`.
* when rebuilding a `Person` object, the `Note` objects can be copied over from the old `Person` object to the new one. Since all objects are immutable, this should not pose any issues.

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `HealthSync`, which `Person` references. This allows `HealthSync` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/HealthSync-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `HealthSyncStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.HealthSync.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo feature (Patient medical record commands for now)

#### Proposed Implementation

The proposed undo mechanism is facilitated by `LogicManager` acting as the command invoker within the command pattern.
It implements the interface `CommandHistory` to manage a command history stack containing a set of `UndoableCommand` to support the *undo feature*.
Below are the implemented `CommandHistory` operations:

* `LogicManager#addCommand(UndoableCommand )` — Adds an `UndoableCommand` to the history stack *(more of deque data structure in the actual implementation)*.
The stack keeps track of at most 10 most recent actions.
* `LogicManager#undoLastCommand()` — Pops the most recent `UndoableCommand` from the history and restores the previous `AddressBook` state tracked by the `UndoableCommand` object that was popped.

Below is an example scenario on how the undo works with the command history stack maintained by the `LogicManager`

Step 1. The user launches the application for the first time.
The `LogicManager` will be initialized with an empty command history stack.

![UndoState0](images/UndoState0.png)

Step 2. The user executes `delete 1` command to delete the 1st patient medical record from the address book.
The `DeleteCommand` extends `UndoableCommand` and thus supports undoable behaviours.
The `DeleteCommand#execute()` is called and saves the current `AddressBook` state as the previous state before modifying it to carry out the delete command.
After executing `DeleteCommand` the command object itself will be pushed into the command history managed by the `LogicManager`.

![UndoState1](images/UndoState1.png)

Step 3. The user executes `add nric/S9974837D n/David …​` to add a new patient medical record.
The `AddCommand` command also calls `AddCommand#execute()`, similar to the delete command, it saves the current `AddressBook` as the previous state before carrying out the add command.

![UndoState2](images/UndoState2.png)
<div markdown="span" class="alert alert-info">
    :information_source: **Note:**
    If a command fails it won't continue execution and error handlers will handle the respective `CommandException` thrown.
    The command would not be pushed into the history stack.
</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command.
The `undo` command will call `LogicManager#undoLastCommand`, and this would pop the most recent `UndoableCommand` as explained above.
Once the `UndoableCommand` was popped the `UndoableCommand#undo()` is executed which reverts the `AddressBook` into the previous state saved within the command.

![UndoState3](images/UndoState3.png)

<div markdown="span" class="alert alert-info">
    :information_source: **Note:**
    If the command history stack is empty then there is nothing to undo and the message "No commands left to undo." is returned as output to the user.
</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagramLogic](images/UndoSequenceDiagram-Logic.png)

Step 5. The user then decides to execute the command `list`.
Commands like `ListCommand` that does not modify the address book does not extend `UndoableCommand` and would execute `ListCommand#execute()` without saving prev state for undo feature.
Thus, the `LogicManager` command history remains unchanged.

![UndoState4](images/UndoState4.png)

<div markdown="span" class="alert alert-info">
    :information_source: **Note:**
    Once the command history stack contains 10 items, when a new `UndoableCommand` is executed least recent command *(bottom of the stack)* is removed
    and push the new one on the top.
</div>

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo executes:**

* **Current choice:** Saves the entire address book as prev state in each undoable command.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo by
  itself. Instead of saving the whole address book within each command as prev state, only the change itself is saved.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

### \[Proposed\] Find feature (Draft)

#### Proposed Implementation
The `FindCommand` feature allows user to search for patients in the patient book efficiently. It is implemented using a `Predicate<Person>` to filer out the patient book.

The parsing of user input is handled by `FindCommandParser`. This parser constructs all the relevant predicates based on the prefixes and keywords given by the user. It then combines the predicates which is used to construct `FindCommand`.

#### Design considerations:

##### Predicate Implementation
* **Alternative 1 (current choice):** Implement predicates as separate classes (e.g. name, gender, nric, etc.)
  * Pros: Better code readability as its own filtering logic is encapsulated in its own class.
  * Cons: Increases the number of classes, some duplication of similar logic code.

* **Alternative 2:** Combining predicate logic in a single class.
  * Pros: Reduces the number of classes.
  * Cons: Reduced readability and maintainability with mixed predicate test logic.

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Dr. Emily Chen is a General Practitioner
* She aims to enhance clinical efficiency and maintain high-quality care
* Challenges include time constraints and documentation overload
* She needs seamless workflow management and a keyboard-driven system
* Her personality is dedicated and empathetic, with a focus on patient care
* Can type fast hence loves to play type racer during her free time
* Prefers to type over mouse interactions

**Value proposition**: manage patient medical records faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                  | So that I can…​                                         |
| -------- | ------------------------------------------ |-----------------------------------------------|---------------------------------------------------------|
| `* * *`  | user                                       | view all my patient's medical records         | have a clear overview of all my records                 |
| `* * *`  | user                                       | add a patient's medical record                | record new patients I work with                         |
| `* * *`  | user                                       | edit a patient's medical record               | amend necessary details of a medical record when needed |
| `* * *`  | user                                       | delete a patient's medical record             | remove patient's medical record that I no longer need   |
| `* * *`  | user                                       | find patients with specific keywords          | locate existing patient records efficiently             |
| `* * *`  | user                                       | list all patients' appointment notes          | view all existing appointment notes                     |
| `* * *`  | user                                       | list a particular patients' appointment notes | view a particular patient's existing appointment notes  |
| `* * *`  | user                                       | add a patient's appointment note              | record keep the details of each appointment             |
| `* * *`  | user                                       | edit a patient's appointment note             | update the details of an appointment                    |
| `* * *`  | user                                       | delete a patient's appointment note           | remove entries that I no longer need                    |
| `* * *`  | user                                       | access a help page for quick reference        | have a better idea where to get started                 |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `HealthSync` and the **Actor** is the `user`, unless specified otherwise)

#### Use case: List all patients' appointment notes

**MSS**

1.  User requests to list patients
2.  HealthSync shows a list of patients

    Use case ends.

**Extensions**

* 2a. The list of patients is empty.

  Use case ends.

#### Use case: List a particular patients' appointment notes

**MSS**

1.  User requests to view a particular patients' appointment notes
2.  HealthSync shows a list of patients

    Use case ends.

**Extensions**

* 1a. The given patient index is invalid.
  * 1a1. HealthSync shows an error message.

    Use case resumes at step 1.
* 2a. The list of patients is empty.

  Use case ends.


#### Use case: Add a patient's appointment note

**MSS**

1.  User requests to list patients
2.  HealthSync shows a list of patients
3.  User requests to add an appointment note for a given patient
4.  HealthSync adds the appointment note

    Use case ends.

**Extensions**

* 2a. The list of patients is empty.

  Use case ends.

* 3a. The given patient index is invalid.

    * 3a1. HealthSync shows an error message.

      Use case resumes at step 2.

#### Use case: Edit a patient's appointment note

**MSS**

1.  User requests to list patients
2.  HealthSync shows a list of patients
3.  User requests to edit an appointment note for a given patient
4.  HealthSync edits the appointment note

    Use case ends.

**Extensions**

* 2a. The list of patients is empty.

  Use case ends.

* 3a. The given patient index is invalid.

    * 3a1. HealthSync shows an error message.

      Use case resumes at step 2.

* 3b. The given appointment note index is invalid.

    * 3b1. HealthSync shows an error message.

      Use case resumes at step 2.

#### Use case: Delete a patient's appointment note

**MSS**

1.  User requests to list patients
2.  HealthSync shows a list of patients
3.  User requests to delete a specific person in the list
4.  HealthSync deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given patient index is invalid.

    * 3a1. HealthSync shows an error message.

      Use case resumes at step 2.

* 3b. The given appointment note index is invalid.

    * 3b1. HealthSync shows an error message.

      Use case resumes at step 2.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 patients without a noticeable sluggishness in performance for typical usage.
3. Should be able to hold up to 200 appointment note per patient without a noticeable sluggishness in performance for typical usage.
4. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Patient's medical record**: Essential information about a patient, including name, NRIC, phone number and other relevant details
* **Patient's appointment note**: Information on a scheduled patient appointment, including date, time and assessment


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
