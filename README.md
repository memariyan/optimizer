# Optimizer

## Overview
The **Optimizer** project is designed to solve complex optimization problems, such as the Vehicle Routing Problem (VRP). It leverages multiple libraries, including Google OR-Tools, and custom algorithms to achieve efficient solutions. The project integrates with external services and follows a distributed architecture using Kafka and MongoDB.

## Features
- Solves optimization problems like VRP.
- Utilizes Google OR-Tools and custom algorithms.
- Implements a message-driven architecture using Kafka.
- Stores and processes data with MongoDB.
- Fetches distance matrices from an external Map Service.
- Supports asynchronous task processing.

## Architecture
![Optimizer Architecture](https://github.com/memariyan/optimizer/blob/main/architecture.png)](https://github.com/memariyan/optimizer/blob/main/architecture.png)

### Workflow
1. **Flow Management** sends a message to Kafka.
2. **Optimizer Service** saves the orders in MongoDB.
3. It calculates the **distance matrix** using an external **Map Service**.
4. A scheduler fetches data after a delay.
5. The system processes the optimization task and fetches necessary tasks from Kafka.
6. Finally, the optimizer sends a reply message back via Kafka.

## Installation & Usage
### Prerequisites
- Java 17+
- Spring Boot
- Maven
- MongoDB
- Kafka

### Setup
```bash
# Clone the repository
git clone https://github.com/memariyan/optimizer.git
cd optimizer

# Build the project
mvn clean install
```

### Running the Service
```bash
java -jar target/optimizer.jar
```
