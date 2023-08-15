![Poject](.main/Teto.gif)

WGU C195 Software II - Performance Assessment
Purpose of application

This application aims to create a GUI-based appointment scheduling desktop application.
This application looks to prove competencies in Database & File Server Applications, Lambdas, Collection, Localization & Date/Time APIs, and Advanced Exception Control.

Author Information

Miguel-Angel Garcia
mgar547@my.wgu.edu

application version: 1.0
date: 3/05/2022

IDE and java module Information

IntelliJ IDEA 2022.2.1 (Ultimate Edition
Build #IU-222.3739.54, built on August 16, 2022
Runtime version: 17.0.3+7-b469.37 aarch64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.

JDK: 17.0.4
JavaFX 17 SDK
Maven Dependant

Additional report

When it came to the customer report, I chose to display the number of customers per Country. I created the query by joining the Customer, First_Level_Divisions, and Countries tables. Country is on the left column, and the Count of Customers is on the right column. I then placed this information in a table view in the Reports section.

How to run the program

As the program starts, a login screen is presented. The user will be required to have a valid username and password that matches the information in a MySQL database. This program requires Java 17 and has not been tested with any other JVM.
