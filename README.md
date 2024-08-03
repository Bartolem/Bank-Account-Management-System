# Bank Account Management System
The Bank Account Management System is a Java-based command line application designed to manage various bank accounts.

## Table of Contents
* [Introduction]
* [Used technologies]
* [Description]
* [Features]
* [Architecture]
* [Getting Started]
* [Usage]
* [License]

## Introduction
This is my first Java project. I did this for educational purposes. 

## Used technologies
* Java
* Maven
  
## Description
The Bank Account Management System allows users to perform transactions such as deposits, withdrawals, and transfers, calculate interest for savings accounts, and handle user authentication. The system is designed with modularity and extensibility in mind, allowing for easy addition of new account types and features.

## Features
* User Authentication: Secure login system to authenticate users.
* Account Management: Support for different account types including Savings and Checking accounts.
* Transaction History: Track all transactions and sort/filter them by various criteria.
* Interest Calculation: Automatically calculate and add interest for savings accounts.
* Daily and Monthly Limits: Enforce daily and monthly transaction limits.
* File Storage: Store transaction history and user credentials in CSV files.

## Architecture
The system is built using a modular architecture, with the following key components:
* Users: Represents the bank's customers.
* Accounts: Abstract base class for different types of accounts.
* Transactions: Represents financial transactions (deposit, withdrawal, transaction).
* Authentication: Handles user login and authentication.
* File Manipulation: Utility classes for reading/writing CSV files.

## Getting Started
### Prerequisites
* Java Development Kit (JDK) 8 or higher
* Maven

### Installation
* Download the jar file from: https://github.com/Bartolem/Bank-Account-Management-System/releases
* Put the jar file in the chosen directory
* Open the command line and change the directory by providing the current directory path
* Setup the application from the command line:
  ```
  java -jar BankAccountManagementSystem.jar -setup
  ```

## Usage
* You can run the application from the command line:
  ```
  java -jar BankAccountManagementSystem.jar
  ```
* If you want to run the application with the logging enabled:
   ```
  java -jar BankAccountManagementSystem.jar -log
  ```
