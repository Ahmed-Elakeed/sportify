# Sportify 🏆
**Sportify** is a comprehensive sports management backend solution, designed to streamline the management of sports teams, players, and competitions. This system is built using **Java Spring Boot**, incorporating modern backend technologies like **JWT-based authentication**, **AOP logging**, **custom exception handling**, and **RESTful APIs**. It’s a highly scalable solution ready for deployment on **AWS**, with detailed API documentation via **Swagger**.

🔍 **Key Features:**
- User Authentication: Secure login and registration with JWT-based authentication.
- Competition Management: Organizers can create, manage, and monitor competitions effortlessly.
- Player Management: Add, update, and remove players from competitions with role-based permissions.
- Performance Tracking: Players can submit scores and track their performance over time.
- User Profiles: Customizable profiles with personal information and profile pictures.
- Search Functionality: Users can search for other players and competitions based on various criteria.

This project utilizes a robust technology stack, including:
- **Java** and **Spring Boot** for the backend
- **JPA** and **Hibernate** for database interactions
- **MySQL** as the database
- **Docker** for containerization
- **AWS** for cloud services
- **Swagger** for API documentation
- **GitHub Actions** for CI/CD

Additionally, I've implemented an **AOP logging system**, a **global exception handler** with custom exceptions, and **event listeners** to enhance functionality and maintainability.


**ER Diagram**

<img width="607" alt="Screenshot 2024-12-01 at 8 57 33 PM" src="https://github.com/user-attachments/assets/e54d71fb-2868-4311-87d7-3330298257d7">


**Class Diagram**

![Screenshot 2024-12-01 at 9 19 34 PM](https://github.com/user-attachments/assets/9222a8ea-be5f-4e55-b996-564832434f9a)


**Use Case Diagram**

<img width="551" alt="Screenshot 2024-12-01 at 9 32 43 PM" src="https://github.com/user-attachments/assets/b52bf1a5-302c-4905-91a3-a27777bd28d7">


This project employs a **Layered Architecture** to ensure modularity, scalability, and a clear separation of concerns. The architecture consists of distinct layers: the Controller layer handles HTTP requests and serves as the entry point; the Service layer contains business logic and coordinates data processing; and the Repository layer manages data persistence using Spring Data JPA. Core domain models reside in the Domain layer, while DTOs facilitate data transfer between layers. Supporting packages include Mapper for data transformation, Validation for enforcing business rules, Security for authentication and authorization, Exception for error handling, Util for reusable utilities, and AOP for Aspect-Oriented Programming to provide logging and cross-cutting concerns. This design ensures clean, maintainable, and secure code that adheres to industry best practices.

<img width="253" alt="Screenshot 2024-12-01 at 9 39 39 PM" src="https://github.com/user-attachments/assets/3f07a1c1-6514-46c1-9893-25ed2a7af1a3">


📝 **Future Improvements:**
- Implementing real-time notifications for competition updates.
- Integrating a chat feature for player interaction.
- Adding analytics for performance insights.
  

Feel free to check it out, provide feedback, or connect if you're interested in discussing it further!

#Sportify #Java #SpringBoot #WebDevelopment #OpenSource #SoftwareEngineering #Portfolio
