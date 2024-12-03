# AIOStuBu
The All-In-One (AIO) Studdy Buddy (Stubu) is an offline-first productivity application designed to equip students with all the essential tools they need to excel in their studies. 

Written in Java with JavaFX.

## Features
- Notes
- Calendar (compliant with RFC 2445)
- Todo List
- Pomodoro Timer
- Progress Tracker
- Custom Flashcards

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

_Note: all the coding syntax practices we learned in class shall be applicable except for the selected few that are defined here (save for some that are only here just for emphasis)._
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
end.
