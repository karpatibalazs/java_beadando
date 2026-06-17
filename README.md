# Agentic Workflow Motor

A Java-based console application designed to simulate and manage file-based agentic workflows. This project focuses heavily on applying strong Object-Oriented Programming (OOP) principles, clean architecture, and rigorous testing practices. 

The application architecture and business logic were developed to strictly satisfy pre-defined structural and unit tests, demonstrating a strong specification-driven implementation approach.

## 🚀 Key Features

* **Specification-Driven Implementation:** The system was built from the ground up to comply with rigorous, pre-defined structural tests (e.g., `AgentTest`, `WorkflowStepTest`, `StructuredOutputTest`), ensuring the architecture perfectly aligns with expected requirements.
* **File-Based Workflow Parsing:** Reads and processes workflow configurations from text files with robust error handling and custom exceptions (`WorkflowFormatException`).
* **Strict Encapsulation:** Implements defensive copying techniques to protect internal states and ensure data integrity across the application.
* **Modern Java Syntax:** Utilizes modern language features such as `switch` expressions for cleaner, more readable logic.
* **Comprehensive Testing:** High test coverage using JUnit 5. The test suite validates not only the happy paths but also edge cases, file operations, and expected exception handling (e.g., duplicate step validation, missing headers).

## 🏗️ Architecture & Core Components

* **`Agent`**: The core engine that loads workflow definitions from text files, manages the sequence of `WorkflowStep` objects, and executes the simulation.
* **`WorkflowStep`**: Represents an individual node in the workflow, encapsulating prompts, system instructions, and expected output types.
* **`StructuredOutput` & `SchemaType`**: Defines and validates the expected schema formats (e.g., INT, STRING, LIST_INT) for the simulated LLM-like outputs.

## 🛠️ Technologies & Tools
* **Language:** Java 
* **Testing Framework:** JUnit 5
* **Concepts:** Test-Driven mindset, OOP, Clean Code, Defensive Copying, Custom Exception Handling
