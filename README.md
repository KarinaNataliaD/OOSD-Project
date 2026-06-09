# OOSD-Project
Checklist:
– Are you in a Group?
No I am not in a group I will be working on this project on my own. 

– What programming language are you selecting? Which version?
At the moment I am thinking of using React + TypeScript with Node.js for the backend

– Do you have your GitHub account set up?
Yes I do have my account set up

– Do you have a public repository for your Project?
Yes I do have my public repository for my project 

– What is the link to your GitHub repository?
https://github.com/KarinaNataliaD/OOSD-Project

– If you are in a group, does everyone have access to the github repo?
I am not in a group for this project. 

– Do you have a “Hello World” program that compiles and runs?
Yes it does 

– Where is the entry point to your project? (src/main/Main.java for
example)
C:\Users\karin\OneDrive\Documents\GitHub\OOSD-Project\project_root\src\Main.java

## Planned Final Submission

For the final submission, I plan to demonstrate a fully functional restaurant ordering system that supports:
- Dynamic menu item creation
- Multiple pricing strategies
- Order customization
- Queue-based order management
- UML documentation
- Multiple implemented design patterns

The application will demonstrate object-oriented programming principles and several software design patterns working together in a practical system.

## Current Challenges

Some challenges encountered during development include:
- Designing relationships between design patterns cleanly
- Refactoring existing code to properly implement patterns
- Ensuring low coupling between components
- Managing scalability as more features are added


-------------------------------------
Project Description: 
A restaurant ordering system where customers can place orders, customize menu items with toppings, and track orders through a kitchen life cycle. The system uses a Swing GUI to demonstrate the 6 design patterns working together in a real application. 

--------------------------------------
How to Run: 

GUI (recommended): 
cd project_root
mvn compile
mvn exec:java -Dexec.mainClass="com.restaurant.MainGUI" 

Terminal Demo 
mvn exec:java -Dexec.mainClass="com.restaurant.Main"

--------------------------------------
What the Final Demo Shows: 
1. Creating a new order for a customer 
2. Adding menu items using the Factory Method pattern 
3. Customising items with toppings using the Decorator pattern 
4. Placing the order which triggers the Observer pattern 
5. Advancing the order through PLACED -> PREPARING -> READY -> DELIVERED 
6. Switching pricing strategies to show Regular, Happy Hour, and Loyalty Discounts 
7. Using Undo to reverse actions via Command Pattern 

--------------------------------------
Known Bugs: 

1. .class files occasionally appear in source folders when compiling outside of Maven.
- Fix: run Java: Clean Java Language Server Workspace in VS Code.
2. The Metals language server sometimes reports incorrect package declaration errors on first load. 
- Fix: run Java: Reload Projects in VS Code command palette.

--------------------------------------
What I would add with more time given: 

1. A database layer to be able to continue placing orders between sessions 
2. A proper login screen for different staff roles (cashier vs kitchen)
3. Menu item editing and a full admin panel 
4. Print receipt functionality  
5. Maybe like sound notifications for when people know when their order is ready. 
