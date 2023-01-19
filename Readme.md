# Java Online Marathon
## To-Do List Project. REST API
### The Task:
Create REST-controllers for managing the following resources:
 - Collection of all the users: `/api/users`
 - Collection of todos for the user: `/api/users/{u_id}/todos`
 - Collection of collaborators for the todo: `/api/users/{u_id}/todos/{t_id}/collaborators`
 - Collection of tasks for the todo: `/api/users/{u_id}/todos/{t_id}/tasks`
---
1) Use GET, POST, PUT, DELETE methods to manage the Collections.
2) Add security rules as from the previous stage (use BasicAuth to begin with)
3) Customize exception handling, use `ResponseStatusException`
4) Use [Postman](https://www.postman.com/downloads/) to demonstrate the functionality
5) Optionally try to implement JWT authentication (the [JJWT](https://github.com/jwtk/jjwt) library included in the pom.xml)


![](C:\Users\obutr\Downloads\ToDoList\ERD.png)

There are three predefined users in the DB with roles ADMIN and USER.

| Login         | Password | Role  |
|---------------|:--------:|:-----:|
| mike@mail.com |   1111   | ADMIN |
| nick@mail.com |   2222   | USER  |
| nora@mail.com |   3333   | USER  |