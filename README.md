**Backend for VeryCool App**

This project is the backend for a platform to store and display ideas. It provides REST/GraphQL APIs to list and add new ideas for the VeryCool application: [https://verycoolapp.com/](https://verycoolapp.com/). The frontend design for this application can be found here: [Figma Design](https://www.figma.com/file/w1flltlnUTjkfiWFyhRnzX/Web-Application-Developer-Technical-Test?type=design&node-id=16%3A2&mode=design&t=3vU9Qf2EZJYZkoOD-1).

**Description**

This project is a REST/GraphQL API for managing and listing project ideas. Users can add, retrieve, and comment on ideas, each of which includes a name, image, author information, comments, and likes. The project aims to implement robust security, easy deployment, and out-of-the-box scalability using modern Java frameworks and technologies.

**Chosen Technologies**

- **Micronaut Framework**: A modern Java framework optimized for building lightweight, fast microservices.
- **SQLite**: A lightweight, file-based relational database chosen for its simplicity and rapid setup. Ideal for development and small-scale use cases. Thanks to **Micronaut Data**, we can easily swap the backend to other databases such as PostgreSQL or MariaDB if needed. Ideal for development and small-scale use cases. A lightweight relational database chosen for its simplicity and compatibility with Docker Compose for rapid setup.
- **Micronaut Data**: A persistence framework that integrates with various databases like SQLite, PostgreSQL, and others, similar to Hibernate but optimized for Micronaut. It allows easy swapping of the database backend.
- **Docker Compose**: Used to easily containerize the application and database, ensuring consistent and simple deployment.
- **Micronaut Security (OAuth + JWT)**: Provides OAuth2 authentication and JWT for secure endpoints, allowing a streamlined security implementation.
- **GraphQL Module**: Micronaut module that supports GraphQL queries and mutations alongside REST endpoints.

**License**

The Unlicense
