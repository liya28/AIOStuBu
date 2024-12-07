![AIOStubu](readme%20resources/aiostubu_screenshot.png)
---
# AIOStuBu
The All-In-One (AIO) Studdy Buddy (Stubu) is an offline-first productivity Windows desktop application designed to equip students with all the essential tools they need to excel in their studies. 

Written in Java with JavaFX.

## Table of Contents
- [App Features](#features)
- [Software Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [User Data/File Handling](#user-datafile-handling)
- [Calendar Compliance with RFC 2445](calendar-compliance-w-rfc-2445)
- [Contribution (for members)](#contribution)
- [Members (with Github Profile Links)](#members-w-github-profile-links)
- [Final Message](#final-message)

## Features
- **Pomodoro Timer** - Study in a time-controlled manner!
- **Flashcards** - Evaluate your knowledge with flashcards!
- **Calendar** - Schedule your events and plot your deadlines!
- **Todo List** - List down things you need to do!
- **Notes** - Create HTML-formatted documents!

## Architecture
In order to simulate an industry-level software codebase and to enforce 
[Separation of Concerns](https://www.geeksforgeeks.org/separation-of-concerns-soc/) 
during software development, the group initially planned for an 
[Onion Architecture](https://medium.com/expedia-group-tech/onion-architecture-deed8a554423) 
for AIOStuBu, but due to time constraints and inexperience with managing medium-sized projects, 
we later settled for a
[Layered Software Architecture](https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/ch01.html).

The dependency arrow of the project goes inwards as follows: 

`Presentation Layer` --> `Controller Layer` --> `Domain & Data Persistence Layer` 

### Domain & Data Persistence Layer (Stewboo.Core)
The focus on creating an offline-first application led to the merging of the **domain layer** with the 
**data persistence layer** for simplicity. This integration allows for efficient data handling and business 
logic execution without the overhead of managing separate layers.

### Controller Layer (Stewboo.UI)
The controller layer in the `Stewboo.UI` package module acts as an intermediary between the user interface (FXML Pages)
and the business logic. It handles incoming requests from the presentation layer, processes them, 
and interacts with the domain layer to retrieve or manipulate data. This separation allows for clear pathways of 
communication within the application, making it easier to manage user interactions and maintain the app's state.

### Presentation Layer (FXML Pages)
The presentation layer, found in the 
[`resources`](StewBoo/src/main/resources/io/serateam/stewboo/ui/controls) 
directory of the project, 
is responsible for rendering the user interface components of the application. 
It utilizes FXML pages that define the structure and layout of the user interface.

## Design Patterns
This project utilizes one or more design patterns to provide a seamless and robust programming structure 
that serves as the pillars of the software architecture being used in this app.
### Singleton Pattern 
Multiple classes in the `Stewboo.Core` use the Singleton Pattern. All the classes implementing 
[IService](StewBoo/src/main/java/io/serateam/stewboo/core/services/IService.java) 
use the Singleton pattern.
- [PomodoroService](StewBoo/src/main/java/io/serateam/stewboo/core/services/pomodoro/PomodoroService.java)
- [NotesService](StewBoo/src/main/java/io/serateam/stewboo/core/services/notes/NotesService.java)
- [TodoListService](StewBoo/src/main/java/io/serateam/stewboo/core/services/todolist/TodoListService.java)
- [FlashCardService](StewBoo/src/main/java/io/serateam/stewboo/core/services/flashcard/FlashCardService.java)
- [CalendarService](StewBoo/src/main/java/io/serateam/stewboo/core/services/calendar/CalendarService.java)

Other than IService classes, the following classes also implement a Singleton Pattern:
- [StubuCalendarList](StewBoo/src/main/java/io/serateam/stewboo/core/services/calendar/StubuCalendarList.java)
- [DeckList](StewBoo/src/main/java/io/serateam/stewboo/core/services/flashcard/DeckList.java)


### Observer Pattern 
The pomodoro feature in `Stewboo.Core` uses the [Observer Pattern](https://refactoring.guru/design-patterns/observer) 
in order to notify the Pomodoro UI for changes in the Pomodoro countdown timer that were executed in the domain layer.
Listeners implementing 
[`IPomodoroListener`](StewBoo/src/main/java/io/serateam/stewboo/core/services/pomodoro/IPomodoroListener.java)
must implement all the functions defined in `IPomodoroListener` to be notified of any changes in Pomodoro.

## User Data/File Handling
Throughout the user's interaction with the application, snapshots of their work and settings are 
periodically saved in JSON text files. This allows users to close the application at any time and 
easily resume their tasks later without losing any data. 

Upon reopening the application, AIOStuBu automatically loads these saved snapshots. 
JSON files are saved via a custom JSON Service class that utilizes the GSon library. 
A link to the implementation can be found [here](https://github.com/liya28/AIOStuBu/blob/main/StewBoo/src/main/java/io/serateam/stewboo/core/utility/JSONService.java).

Saved JSON files are located in the `aiostubu/` directory within the project which is created upon the 
application's first boot.


## Calendar Compliance with RFC 2445
AIOStuBu utilizes the CalendarFX library to implement its calendar feature. 
Currently, the model of the AIOStuBu Calendar Entry class is compliant with the iCalendar guidelines stipulated in 
[**RFC 2445: Internet Calendaring and Scheduling Core Object Specification**](https://www.rfc-editor.org/rfc/rfc2445). 
This specification outlines the structure and format for calendar data, enabling interoperability between 
different calendar systems and applications. CalendarFX is also compliant with this specification, 
especially for its 
[CalendarFX Entry class recurrence pattern rules](https://dlsc-software-consulting-gmbh.github.io/CalendarFX/#_entry).

### Why Not RFC 5545?
While RFC 2445 serves as the foundation for AIOStuBu's calendar entry model class, 
it is important and fair to note that it has been superseded by [RFC 5545](https://www.rfc-editor.org/rfc/rfc5545), 
which refines and expands upon its predecessor's specifications.

The decision to initially implement RFC 2445 was influenced by its compatibility with CalendarFX. 
Transitioning to RFC 5545 would necessitate significant code changes, particularly concerning the recurrence rules. 
Considering the objectives of this capstone project, we believe that the effort required to develop a 
compatibility function for both specifications outweighs the potential benefits. 
Therefore, the team opted to continue with RFC 2445.

## Contribution
If you are a group member, please read the following texts.

### Preparing Your Machine
1. You must use Intellij IDEA for this project. [Download IntelliJ for Windows](https://www.jetbrains.com/idea/download/?fromIDE=%2F&section=windows).
2. For ease when doing styling and UI work, use Scene Builder. [Download Scene Builder](https://gluonhq.com/products/scene-builder/).
3. Some components utilize JFoenix for styling. Install the JAR file of JFoenix into the libraries that Scene Builder will use. Do this by going to _Library_ > _Library Settings_ > _JAR/FXML Manager_. Scene Builder has a built-in search function that lets you search for repositories online, so you do not need to download anything externally.

### Coding Your Feature
1. Create a branch from origin/main (or a branch that you are planning to base from).
2. OPTIONAL: Name your branch: \<feature>-dev (e.g. pomodoro-dev).
3. Start coding.
4. Divide your code changes into specific commits. This ensures traceability so we can revert to old versions of your code when unexpected things happen. Be meticulous with commit descriptions.

### Submitting Your Code For Review
1. Push your branch. This will add this as a remote branch in our Github repo.
2. In our Github repo, create the pull request. Do not merge the branch.
3. Upon a successful review (with minor tweaks, if applicable), the reviewers will merge the branch themselves.


### Code Syntax Bible
To ensure uniformity in our code, please refer to the following code blocks. **PLEASE BE CONSISTENT**.

_Note: all the coding syntax practices we learned in class shall be applicable except for the selected few that are 
defined here (save for some that are only here just for emphasis)._
````java
// I. Variables
public static final int UNIVERSAL_VARIABLE = 10;    // constant/final variables
public static int UniversalCounter = 0;             // static variables
private String _fieldText = "My text";              // private instance variables
String fieldText = "My text";                       // instance variables

/*
 * I.a. FXML Variables
 *    -- @FXML annotations should be in-line
 *    -- Shall use <ComponentName>_<Noun>
 */
@FXML private JFXButton btn_calendar;
@FXML private AnchorPane anchorPane_nav;
@FXML private TextField textField_search;
@FXML private ImageView imageView_userProfile;
@FXML private ImageView imageView_menuOpen;
@FXML private AnchorPane anchorPane_navPaneSlider;

/* II. Scoping
 *    -- Both opening and closing curly braces must be on their own line (BSD/Allman style). 
 *       We need visual breaks when reading code. 
 * */
class MyClass
{
    public MyClass()
    {
        // ...
    }
}

/* III. Methods 
 *  -- Be descriptive when naming methods.   
 *  -- Shall use <Verb><Noun(optional/encouraged)><Complement/Context(optional)> with camel casing.
 */
public void initialize()
{
    // ...
}

public void initializeMenu()
{
    // ...
}

public void initializeMenuVariables()
{
    // ...
}

public void serializeJsonStringIntoData()
{
    // ...
}

public void singLetItGoByIdinaMenzel()
{
    // ...
}
````

## Members (with Github Profile Links)
1. [Aliyah Khaet Regacho | BSCS-2](https://github.com/liya28) 
2. [Harley S. Reyes | BSCS-2](https://github.com/muhadma)
3. [John Earl C. Echavez | BSCS-2](https://github.com/EarlJohnHub)
4. [Rafael A. Mendoza | BSCS-2](https://github.com/GReturn) 

## Final Message
In compliance with CSIT227 - Object-oriented Programming 1 A.Y. 2024-2025. All Rights Reserved.

First and foremost, we would like to thank the Almighty God for giving us the wisdom and strength to 
complete this capstone project. With His blessings, we are able to comprehend, formulate, and overcome the various 
obstacles in this capstone project.

We would also like to thank our parents for giving us great understanding of the importance of this 
project by providing us adequate time and their full support for the completion of this project.

Lastly, we thank **Mr. Jay Vince D. Serato** 🧡, for setting up the foundation of our understanding on the various concepts 
required for the completion of this project. Keep staying _cool and normal_, Sir Vince!



_Made with love ❤️, by SeraTeam A.Y. 2024-2025_


END.
