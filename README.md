# Smart Cash Flow Project

Welcome to the Smart Cash Flow project! This documentation aims to provide you with information on how to use, understand, and contribute to this project.

## Introduction

The Smart Cash Flow project is designed to help businesses control their commercial cash flow. The main objective is to provide accurate information to facilitate business decision-making.

In addition to information on what has already been implemented, this documentation includes a section dedicated to potential future developments that should be considered in conjunction with business decisions.

## Prerequisites

Before getting started, make sure you have the following installed:

- Git ([installation guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git))
- Java 8 or later ([installation guide](https://www.oracle.com/java/technologies/javase-downloads.html))
- Docker ([installation guide](https://docs.docker.com/get-docker/))
- Docker Compose ([installation guide](https://docs.docker.com/compose/install/))

## Usage

### Clone the repository

To clone the repository, run the following command:

```sh
git clone https://github.com/cabelin/cashflow-conciliation.git
```

### Start the containers
To start the containers, navigate to the project directory and run the following command:

```sh
docker-compose up -d
```

### Build the project

To build the project, navigate to the project directory and run the following command:

```sh
./gradlew build
```

### Run the project

To run the project, navigate to the project directory and run the following command:

```sh
./gradlew bootRun
```

This will start the Spring Boot application and you can access it by navigating to [http://localhost:8080](http://localhost:8080) in your web browser.

### Run unit tests

To run unit test, navigate to the project directory and run the following command:

```sh
./gradlew test
```

### Access the API documentation

To access the API documentation, you can navigate to [/docs/swagger.yml](/docs/swagger.yml) in the project folder. However, please note that the Swagger UI may not display the documentation properly when accessed locally. As an alternative, we recommend using an online Swagger editor to view the API documentation in a more user-friendly manner

## Contributing

To contribute to the project, please create a fork of the repository, make your changes, and then submit a pull request. Before submitting a pull request, make sure to test your changes thoroughly and ensure that they meet the project's coding standards.
