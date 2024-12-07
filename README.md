# AIOStuBu
The All-In-One (AIO) Studdy Buddy (Stubu) is an offline-first productivity application designed to equip students with all the essential tools they need to excel in their studies. 

Written in Java with JavaFX.
![](readme%20resources/aiostubu_screenshot.png)


## Table of Contents
- [Features](#features)
- [Software Architecture](#architecture)
- [Class Diagram](#class-diagram)
- [Design Patterns](#design-patterns)
- [User Data/File Handling](#user-datafile-handling)
- [Calendar Compliance with RFC 2445](calendar-compliance-w-rfc-2445)
- [Contribution (for members)](#contribution)
- [Members w/ Github Profile Links](#members-w-github-profile-links)
- [Final Words](#final-words)

## Features
- Notes - Create HTML-formatted documents!
- Calendar - Schedule your events!
- Todo List - List down things you need to do!
- Pomodoro Timer - Study in a time-controlled manner!
- Flashcards - Evaluate your knowledge with flashcards!

## Architecture
In order to simulate common industry-level software codebases, the group initially planned 
for an [Onion Architecture](https://medium.com/expedia-group-tech/onion-architecture-deed8a554423) for AIOStubu, 
but due to time constraints and inexperience with managing medium-sized projects, we later settled for a
[Model View Controller]() software architecture, as with the case with most JavaFX applications.

### Domain Layer (Stewboo.Core)

### Controller Layer (Stewboo.UI)

### Presentation Layer

## Class Diagram

### IService
### Pomodoro
### Calendar
### Notes
### Flashcards
### TodoList

## Design Patterns
This project utilizes one or more design patterns to provide a seamless and robust software architecture.
### Model-View-Controller Pattern
JavaFX enforces the MVC Pattern
### Singleton Pattern 
Classes that implements IService use the Singleton pattern.
### Observer Pattern 
Pomodoro in Core uses observer/listener pattern


## User Data/File Handling
Throughout the user's interaction with the application, snapshots of their work and settings are 
periodically saved in JSON text files. This allows users to close the application at any time and 
easily resume their tasks later without losing any data. 

Upon reopening the application, AIOStubu automatically loads these saved snapshots. 
JSON files are saved via a custom JSON Service class that utilizes the GSon library. 
A link to the implementation can be found [here](https://github.com/liya28/AIOStuBu/blob/main/StewBoo/src/main/java/io/serateam/stewboo/core/utility/JSONService.java).

Saved JSON files are located in the `aiostubu/` directory within the project upon the application's first boot.

## Calendar Compliance with RFC 2445
TBD. Why not RFC 5545?

## Contribution
If you are a group member, please read the following texts.

### Preparing Your Machine
1. You must use Intellij IDEA for this project. [Download IntelliJ for Windows](https://www.jetbrains.com/idea/download/?fromIDE=%2F&section=windows).
2. For ease when doing styling and UI work, use Scene Builder. [Download Scene Builder](https://gluonhq.com/products/scene-builder/).
3. Some components utilize JFoenix for styling. Install the JAR file of JFoenix into the libraries that Scene Builder will use. Do this by going to _Library_ > _Library Settings_ > _JAR/FXML Manager_. Scene Builder has a built-in search function that lets you search for repositories online, so you do not need to download anything.

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
        ...
    }
}

/* III. Methods 
 *  -- Be descriptive when naming methods.   
 *  -- Shall use <Verb><Noun(optional/encouraged)><Complement/Context(optional)> with camel casing.
 */
public void initialize()
{
    ...
}

public void initializeMenu()
{
    ...
}

public void initializeMenuVariables()
{
    ...
}

public void serializeJsonStringIntoData()
{
    ...
}

public void singLetItGoByIdinaMenzel()
{
    ...
}
````

## Members (w/ Github Profile Links)
1. [Aliyah Khaet Regacho](https://github.com/liya28) 
2. [Harley S. Reyes](https://github.com/muhadma)
3. [John Earl C. Echavez](https://github.com/EarlJohnHub)
4. [Rafael A. Mendoza](https://github.com/GReturn) 

## Final Words
In compliance with CSIT227 - Object-oriented Programming 1. All Rights Reserved.

_guys naa moy pang final words mahatag ni Sir Vince dri?_ -raf


end.
