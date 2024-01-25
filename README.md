# Todo App Plus - Implementation of the JJJ stack

This project contains an example implementation of the JJJ stack.
The project is the central subject of the discussion:
Can the modern Java dev survive without application frameworks like Spring-Boot?

## Introduction

- Application frameworks (Spring-Boot, Quarkus, Micronaut, …) are very common in the Java world
- They provide features, tools, documentation, and big user bases
- They also provide abstractions, complexity, and dependencies
- Looking at other ecosystems like Rust or Golang, similar application frameworks are far simpler and smaller
- Is there a way to build simple applications in Java that …
    - … ship the features you want?
    - … provide decent tooling and developer experience?
    - … prevent you from building everything from scratch?
- Let's explore one stack that might fill that gap

## Overview

### Run the project

- `./mvnw clean install`
- `docker compose up`
- `java -jar target/todo-app-plus.jar`

### Check out the application

- Go to your browser and open the [application](http://localhost:8080)
- Click `Sign in`
- Log in with `admin / admin`, `user1 / user` or `user2 / user`
- Now you can create / edit / delete boards and manage todos of the boards

### Check out the code

- Each top-level-package is a 'module' and provides a package documentation!
- Check the comments in the code
- Special locations are marked as a `PoI` (point of interest)

## Summary: Frameworks and libraries

### The interesting ones

#### `Javalin`: Web framework

- What does it do?
    - Easy to define endpoints and handlers for calls to the endpoints
    - Resulting API is very clear (declared in an imperative manner)
    - Provides plugins for various needs (OpenAPI, SSL, Rendering, CORS, GraphQL, Micrometer, ...)
    - Integrations with other frameworks exist as well (eg. Pac4j... more on that later)
- Examples
    - *[TodoAppPlusMain](src/main/java/de/lpo/todo/TodoAppPlusMain.java): How to set up a Javalin server
    - *[ApplicationContext](src/main/java/de/lpo/todo/dependencies/ApplicationContext.java): DI without any frameworks
    - [ConfigService](src/main/java/de/lpo/todo/config/ConfigService.java): Server configuration without any frameworks
    - [DataSourceService](src/main/java/de/lpo/todo/database/DataSourceService.java): Create a datasource
      programmatically
    - *[DbMigrationService](src/main/java/de/lpo/todo/database/DbMigrationService.java): Perform Liquibase update
      programmatically
- Can it compete?
    - Writing web server with Javalin is a joy
    - The framework is very intuitive, and the documentation and provided examples are solid
    - Regarding the web server features of other application frameworks, it certainly can keep up!

#### `jOOQ`: Type safe SQL queries in Java

- What does it do?
    - On compile time, we generate classes to match the target database
    - In this case, we provide Liquibase migrations to the generator, so it does the generation based on the resulting
      database
    - These generated classes can be used to access all the tables in a type safe way
    - jOOQ provides a DSL that allows to write statements with a fluent api
- Examples
    - [pom.xml](pom.xml): Generate classes from Liquibase migrations
    - *[Generated classes](target/generated-sources/jooq/org/jooq/generated/Tables.java): Everything in the database has
      a class representation
    - [JooqService](src/main/java/de/lpo/todo/database/JooqService.java): Service for access to the DSL
    - [TodoRepo](src/main/java/de/lpo/todo/domain/repository/TodoRepo.java): Example for simple CRUD queries
    - *[BoardRepo](src/main/java/de/lpo/todo/domain/repository/BoardRepo.java): Example for complexer queries
- Can it compete?
    - In comparison to JPA and pre-made repositories, you write a lot more statements yourself
    - In the case of this project, writing your own repositories for database access was not an issue
    - It is much easier to predict what jOOQ does under the hood vs JPA
    - You don't have to cater to JPA entities, since you do the statements & mappings yourself!
    - All in all: jOOQ can fill the role of JPA wonderfully!
    - Note: jOOQ and JPA are not mutually exclusive (you can use jOOQ for the heavy lifting and JPA for the day-to-day
      queries)

#### `JTE`: Type safe template engine

- What does it do?
    - On compile time, classes are generated to access these templates in a type safe way
    - Supports all features you might know from other template engines
    - Templates can be rendered through the Javalin rendering plugin; however, this way you loose the type safety!
    - Support hot reloading of templates for development purposes
    - JTE has a plugin for IntelliJ!
- Examples
    - [pom.xml](pom.xml): Precompile templates and generate model classes
    - [Generated classes](target/generated-sources/jte/gg/jte/generated/precompiled/Templates.java): All templates are
      accessible in a type safe way
    - [JteService](src/main/java/de/lpo/todo/web/JteService.java): Support for HMR on DEV profile
    - *[JteHandler](src/main/java/de/lpo/todo/web/JteHandler.java): Custom integration into Javalin
    - *[BoardEditActionHandler](src/main/java/de/lpo/todo/web/handlers/BoardEditActionHandler.java): Example endpoint
      that responds a model to render
- Can it compete?
    - Compared to other templating engines? Without a doubt!
    - Has everything you'd need
    - Big plus for the type safe templates (although you loose plugin integration with Javalin)

#### `pac4j`: Security framework

- What does it do?
    - Provides extensive framework for securing your apps
    - You can configure a big variety of clients (OAuth, SAML, CAS, OIDC, Http, Kerberos, ...)
    - You can configure many authenticators (to check for tokens, roles, etc.)
    - You can configure many matchers (determine if request has to be authenticated, or perform side effects like
      setting headers)
    - There is an integration with Javalin; however, IT DOES NOT WORK...
        - The current artifact (pac4j-javalin, version 6.0.0) leads to runtime errors (imported classes are not
          found in pac4j-core)
        - Even the up-to-date classes that fix this have some bugs (request body is lost after security checks)
- Examples
    - [TodoAppPlusMain](src/main/java/de/lpo/todo/TodoAppPlusMain.java): 'before' calls that invoke security handlers
    - *[OidcSecurityService](src/main/java/de/lpo/todo/auth/OidcSecurityService.java): Security setup and integration
      into Javalin
    - [BoardSecurityService](src/main/java/de/lpo/todo/domain/BoardSecurityService.java): Additional handlers for domain
      related security
    - [workaround package](src/main/java/de/lpo/todo/auth/workaround/package-info.java): Copied classes, since some
      Maven artifacts don't work
- Can it compete?
    - In comparison to other security frameworks, this one seems to be the only scalable option
    - Not executable maven artifacts should be a dealbreaker
    - Loosing request information due to security flow should be a dealbreaker
    - But: The lack of alternatives made me stick with it
    - Comparing to the builtin Spring- and Quarkus-security, Pac4j for me was certainly a loss

#### `HTMX`: Dynamic websites without Javascript

- What does it do?
    - Provides attributes to perform AJAX requests
    - HTMX expects that the response of these AJAX requests contains HTML
    - Responded HTML is used to swap out elements in the DOM
    - This way, you can have dynamic behaviour without Javascript and JSON
- Examples
    - *[todos.jte](src/main/templates/todos.jte): Example template for a page
    - *[todo-list.jte](src/main/templates/todos/todo-list.jte): Multifunctional components & hx-vals
    - *[todo-item.jte](src/main/templates/todos/todo-item.jte): JTE loops / hx-post / hx-swap / hx-on\*
    - [board-list.jte](src/main/templates/boards/board-list.jte): Adding modals to DOM
    - [boards-add-modal.jte](src/main/templates/boards/modals/board-add-modal.jte): Handling modal logic
- Can it compete?
    - With other frontend frameworks? Yes, but...
    - You have to approach the entire architecture and development process in a different way
    - Your APIs need to respond HTML now
    - You most likely cannot avoid JS entirely anyway (handling CSRF-tokens / non-standard UI components, etc.)
    - All that being said: It makes 'going back to the roots' possible, which I enjoyed much in this JS-dominated world

### Frameworks & libraries: Honorable mentions

- `Squids`: Generate short identifiers to mask technical IDs to the outside world
- `Liquibase`: Perform database migrations and manage changelogs
- `HikariCP`: DataSource-implementation with connection pooling
- `Logback`: Logger implementation of SLF4j
- `Pico.css`: Minimal CSS framework
- `css.gg`: Open-source CSS icons
- `Testcontainers`: Run containerized instances your application's dependencies (like databases, identity providers,
  mail servers, ...)
- `JUnit5`: Testing framework for Java
- `AssertJ`: Fluent API to write test assertions
- `Docker`: For an easy and reproducible local setup
- `PostgreSQL`: Relational DBMS
- `Keycloak`: Identity provider

## Summary: Overall experience

- This stack seems suitable for many projects
    - Performance-wise, it should be straight forward to deliver
    - Complexity-wise, it allows to scale to a certain degree of complexity without issues
    - Functionality-wise, many features can be implemented by using existing framework features
    - Dev-experience-wise, once the initial project is set up, you can just start developing
- But: in some regards, this stack still lacks behind the big application frameworks
    - Accessibility of features: Some features are harder to get running using this stack
    - Expect the unexpected: Unexpected requirements might be harder to fulfill
    - Dev-experience: The amount and quality of dev tools is just lower than for most application frameworks
    - Documentation: The level of docs and examples is just lower
    - Lesser known libraries: For some devs, the additional learning curve might be an issue
- My personal conclusion:
    - For smaller projects, I could totally imagine to use this stack (maybe with some alterations).
    - For enterprise project, I'd still use enterprise-grade application frameworks.
    - I am certainly more mindful now about what application frameworks bring to the table.
    - But now I know more frameworks, tools and approaches than before, which only can be beneficial.
    - I enjoyed this project!


