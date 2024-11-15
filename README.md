**Examination System Project**
==========================

This is an online examination system project that allows multiple students to give online interviews. The system provides a user-friendly interface for students to attempt exams and for administrators to manage the exam process.

**Getting Started**
---------------

Before running the application, please note the following:

* Create a database named `examinationDB` in your database management system.
* Update the `application.properties` file with your database username and password.

**User Control API Endpoints**
---------------------------

The following API endpoints are available for user control:

### UserController

* `GET /get/all`: Get all users
* `GET /get/{id}`: Get a user by ID
* `GET /get/email`: Get a user by email
* `GET /get/role`: Get users by role
* `POST /add`: Add a new user
* `PUT /update/{userId}`: Update a user
* `DELETE /delete/{id}`: Delete a user by ID

### CategoryController

* `GET /get/all`: Get all categories
* `GET /get/{id}`: Get a category by ID
* `POST /add`: Add a new category
* `PUT /update/{id}`: Update a category
* `DELETE /delete/{id}`: Delete a category

### CollegeController

* `GET /get/all`: Get all colleges
* `GET /get/{id}`: Get a college by ID
* `POST /add`: Add a new college
* `PUT /update/{id}`: Update a college
* `DELETE /delete/{id}`: Delete a college

### ExamController

* `GET /get/all`: Get all exams
* `GET /get/{id}`: Get an exam by ID
* `POST /add`: Add a new exam
* `PUT /update/{id}`: Update an exam
* `DELETE /delete/{id}`: Delete an exam

### McqQuestionsController

* `GET /get/all`: Get all MCQ questions
* `GET /get/{id}`: Get an MCQ question by ID
* `POST /add`: Add a new MCQ question
* `PUT /update/{id}`: Update an MCQ question
* `DELETE /delete/{id}`: Delete an MCQ question

### ProgrammingQuestionsController

* `GET /get/all`: Get all programming questions
* `GET /get/{id}`: Get a programming question by ID
* `POST /add`: Add a new programming question
* `PUT /update/{id}`: Update a programming question
* `DELETE /delete/{id}`: Delete a programming question

### StudentExamDetailsController

* `GET /get/all`: Get all student exam details
* `GET /get/{id}`: Get a student exam detail by ID

### StudentMcqAnswerController

* `GET /get/all`: Get all student MCQ answers
* `GET /get/{id}`: Get a student MCQ answer by ID
* `POST /add`: Add a new student MCQ answer

### StudentProgrammingAnswerController

* `GET /get/all`: Get all student programming answers
* `GET /get/{id}`: Get a student programming answer by ID
* `POST /add`: Add a new student programming answer

### ExamCategoryDetailsController

* `GET /get/all`: Get all exam category details
* `GET /get/{id}`: Get an exam category detail by ID
* `POST /add`: Add a new exam category detail
* `PUT /update/{id}`: Update an exam category detail
* `DELETE /delete/{id}`: Delete an exam category detail

### McqOptionsController

* `GET /get/all`: Get all MCQ options
* `GET /get/{id}`: Get an MCQ option by ID
* `DELETE /delete/{id}`: Delete an MCQ option

### ProgrammingTestCaseController

* `GET /get/all`: Get all programming test cases
* `GET /get/{id}`: Get a programming test case by ID
* `POST /add`: Add a new programming test case
* `PUT /update/{id}`: Update a programming test case
* `DELETE /delete/{id}`: Delete a programming test case

**Note**: This is not an exhaustive list of API endpoints. Additional endpoints may be added as the project evolves.

### PostMan Collection
<a href="https://www.postman.com/warped-astronaut-762065/workspace/examination-module/collection/29290869-8c9f7985-5a6f-4537-9df8-1a4efc6fed4c?action=share&creator=29290869">Postman Collection</a>

**ERD**
---------------------------
![ER Diagram](gitimages/ERD3_2.svg)
