----------------------------------------------------------------------------------------------------------------------------------------------------------

​																					EXERCÍCIO 1

​	Why REST? REST = Spring MVC + Spring HATEOAS app with HAL representations of each resource

​	REST embraces the precepts of the web, including its architecture, benefits, and everything else. This is no surprise given its author, Roy Fielding, was involved in probably a dozen specs which govern how the web operates.

​	Benefits:

- Suitable actions ('GET', 'POST', 'PUT', 'DELETE', ...)
- Caching
- Redirection and forwarding
- Security

​	Tactics:

- Don’t remove old fields. Instead, support them.

- Use rel-based links so clients don’t have to hard code URIs.

- Retain old links as long as possible. Even if you have to change the URI, keep the rels so older clients have a path onto the newer features.

- Use links, not payload data, to instruct clients when various state-driving operations are available.




[Listing all records: ]

```bash
$ curl -v localhost:8080/employees | json_pp
```

> GET /employees HTTP/1.1
>
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
> (...)
>
> {
>  "_embedded" : {
>     "employeeList" : [
>        {
>           "_links" : {
>              "employees" : {
>                 "href" : "http://localhost:8080/employees"
>              },
>              "self" : {
>                 "href" : "http://localhost:8080/employees/1"
>              }
>           },
>           "firstName" : "Bilbo",
>           "id" : 1,
>           "lastName" : "Baggins",
>           "name" : "Bilbo Baggins",
>           "role" : "burglar"
>        },
>        {
>           "_links" : {
>              "employees" : {
>                 "href" : "http://localhost:8080/employees"
>              },
>              "self" : {
>                 "href" : "http://localhost:8080/employees/2"
>              }
>           },
>           "firstName" : "Frodo",
>           "id" : 2,
>           "lastName" : "Baggins",
>           "name" : "Frodo Baggins",
>           "role" : "thief"
>        }
>     ]
>  },
>  "_links" : {
>     "self" : {
>        "href" : "http://localhost:8080/employees"
>     }
>  }
> }


[Querying one existing record: ]

```bash
$ curl -v localhost:8080/employees/1 | json_pp
```

> GET /employees/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
> (...)
> {
>  "_links" : {
>     "employees" : {
>        "href" : "http://localhost:8080/employees"
>     },
>     "self" : {
>        "href" : "http://localhost:8080/employees/1"
>     }
>  },
>  "firstName" : "Bilbo",
>  "id" : 1,
>  "lastName" : "Baggins",
>  "name" : "Bilbo Baggins",
>  "role" : "burglar"
> }


[Querying a record that doesn't exist: ]

```bash
$ curl -v localhost:8080/employees/99
```

> GET /employees/99 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
> (...)
> Could not find employee 99	


[Creating a new Employee record: ]

```bash
$ curl -X POST localhost:8080/employees -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "gardener"}' | json_pp
```

> {
>    "_links" : {
>       "employees" : {
>          "href" : "http://localhost:8080/employees"
>       },
>       "self" : {
>          "href" : "http://localhost:8080/employees/5"
>       }
>    },
>    "firstName" : "Samwise",
>    "id" : 5,
>    "lastName" : "Gamgee",
>    "name" : "Samwise Gamgee",
>    "role" : "gardener"
> }


[Updating this last record's role: ]

```bash
$ curl -X PUT localhost:8080/employees/3 -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "ring bearer"}' | json_pp
```

> {
>    "_links" : {
>       "employees" : {
>          "href" : "http://localhost:8080/employees"
>       },
>       "self" : {
>          "href" : "http://localhost:8080/employees/7"
>       }
>    },
>    "firstName" : "Samwise",
>    "id" : 7,
>    "lastName" : "Gamgee",
>    "name" : "Samwise Gamgee",
>    "role" : "ring bearer"
> }


[Deleting one record: ]

```bash
$ curl -X DELETE localhost:8080/employees/5 | json_pp
```



Everytime we run the program, the **LoadDatabase** class is called again, and inserts the 2 first employees into the database. The data we "store" gets deleted at the end. To solve this we could implement a database independently from the REST API, that wouldn't be loaded at the start. The REST API would only insert, access and delete data into that DB. 



If searching for a non-existent http://localhost:8080/employees/987987, we would get the HTTP status NOT_FOUND (404), because that's what is set on the class EmployeeNotFoundAdvice.



![image-20201114204133071](/home/alexis/snap/typora/23/.config/Typora/typora-user-images/image-20201114204133071.png)



EMPLOYEE - class that defines an employee ready to be stored in a database (@Entity annotation), whose attributes are id (@Id indicates it is the primary key and @GeneratedValue indicates that the value is generated automatically), firstName, lastName and role.

EMPLOYEECONTROLLER - class that, given a repository and an assembler of employees, has routes to different operations involving the employees. @GetMapping corresponds to a HTTP GET and @PostMapping corresponds to a HTTP POST. @PutMapping corresponds to a HTTP PUT and @DeleteMapping corresponds to a HTTP DELETE. The method all() is routed "/employees" and returns a collection of all the records from the repository modeled into Employees. The same goes for the one(id) method but instead of getting all records from the repository, it gets only the one who matches the id passed. If there isn't one, it throws an EmployeeNotFoundException. This method is routed to "/employees/{id}" where the id matches the employee id. newEmployee(employee) method saves an employee into the repository and returns its entity model (data + links). It is routed to "/employees". The replaceEmployee(employee, id) method finds a record by its id and changes its name and role properties by the ones in the new employee. If there isn't any record with the id passed as argument, it just creates a new  record of the new employee. It returns the entity model of the new employee. This method is routed to "/employees/{id}". deleteEmployee(id) method is also routed to "/employees/{id}" and, after finding the record which matches its id with the one passed as an argument, it simply deletes it and returns an empty response entity. 

EMPLOYEEMODELASSEMBLER - class that converts a non-model object (employee) into a model based object (EntityModel<Employee>).

EMPLOYEENOTFOUNDADVICE - class that only responds if an EmployeeNotFoundException is thrown (because of the annotation @ExceptionHandler) in which case it issues an HTTP 404 (HttpStatus.NOT_FOUND) and prints the message defined in EmployeeNotFoundException class.

EMPLOYEENOTFOUNDEXCEPTION - class that renders an HTTP 404 with the text "Could not find employee " + id.

EMPLOYEEREPOSITORY - interface that, because it extends from JpaRepository, supports the creation, update, search and deletion of instances

LOADDATABASE - class that runs all CommandLineRunner beans to initialize the DB with a copy of both repositories (employee and order). Using them, upon initialization, the class will create and store 2 entities for each and then query for all of the records. 

ORDER - class that defines an order ready to be stored in a database (@Entity annotation), in a table called "CUSTOMER_ORDER", because the default name (the one of the class - Order) isn't a valid table name. This class' attributes are description and status.

ORDERCONTROLLER - class that, given a repository and an assembler of orders, has routes to different operations involving the orders. @GetMapping corresponds to a HTTP GET and @PostMapping corresponds to a HTTP POST. @PutMapping corresponds to a HTTP PUT and @DeleteMapping corresponds to a HTTP DELETE.  The method all() is routed to "/orders" and returns a collection of all the records from the repository modeled into Orders. The same goes for the one(id) method but instead of getting all records from the repository, it gets only the one who matches the id passed. If there isn't one, it throws a OrderNotFoundException. This method is routed to "/order/{id}" where the id matches the order id. newOrder(order) method saves an order into the repository with the status "IN_PROGRESS" and returns its entity model (data + links). It is routed to "/orders". The cancel(id) method finds a record by its id and, if its status is "IN_PROGRESS", it changes it to "CANCELLED". Otherwise, it throws the Http status "METHOD_NOT_ALLOWED" with a Problem in the body called "Method Not Allowed" and the message "You can't cancel an order that is in the " + order.getStatus() + " status.". This method is routed to "/orders/{id}/cancel". complete(id) method is also to "/orders/{id}/complete" and, after finding the record which matches its id with the one passed as an argument, changes its state to "COMPLETED" if it is in the state "IN_PROGRESS". Otherwise,  it throws the Http status "METHOD_NOT_ALLOWED" with a Problem in the body called "Method Not Allowed" and the message "You can't cancel an order that is in the " + order.getStatus() + " status.". 

ORDERMODELASSEMBLER

ORDERNOTFOUNDEXCEPTION

ORDERREPOSITORY - interface that extends from JpaRepository and allows interacting with orders in the DB.

PAYROLLAPPLICATION - main class

STATUS - enumerate of the 3 states available for the Order: IN_PROGRESS, COMPLETED AND CANCELLED. 

-------------------

---

​																					EXERCÍCIO 2



```bash
$ docker run --name "mysql5" -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=demo -e MYSQL_USER=alexis -e MYSQL_PASSWORD=password-p 3306:3306 -d mysql/mysql-server:5.7
```



- **Generate**: Maven Project
- **Java Version**: 11
- **Spring Boot**: 2.4.0
- **Group**: ies.lab3
- **Artifact**: jpadata-w-restinterface
- **Name**: jpadata-w-restinterface
- **Description**: Rest API for a Simple Employee Management Application
- **Package Name** : ies.lab3
- **Packaging**: jar (This is the default value)
- **Dependencies**: Web, JPA, MySQL, DevTools



[class Employee: ]

```java
package ies.lab3.jpadatawrestinterface;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
    private long id;
    private String firstName;
    private String lastName;
    private String emailId;

    public Employee(){}

    public Employee(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName(){
        return lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    @Column(name = "email_address", nullable = false)
    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String email) {
        this.emailId = email;
    }

    @Override
    public String toString() {
        return "Employee [id = " + id + ", firstName = " + firstName+ ", lastName = " + lastName +
                ", emailId = " + emailId + "]";
    }
}
```



[interface EmployeeRepository: ]

```java
package ies.lab3.jpadatawrestinterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
}
```



[class EmployeeController: ]

```java
package ies.lab3.jpadatawrestinterface;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException{

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Employee Not Found for this id :: " + employeeId));

        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                   @Valid @RequestBody Employee employeeDetails) throws
            ResourceNotFoundException{

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new
                ResourceNotFoundException("Employee Not Found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());

        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);

    }

    @DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id")Long employeeId) throws
            ResourceNotFoundException{

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new
                ResourceNotFoundException("Employee Not Found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;

    }
}
```



[application.properties: ]

```properties
#MySQL

spring.datasource.url=jdbc:mysql://127.0.0.1:3306/demo

spring.datasource.username=demo

spring.datasource.password=password

spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect

#Strategy to auto updatethe schemas  (create, create-drop, validate, update)

spring.jpa.hibernate.ddl-auto = update
```

```json
{
	"firstName":"Alexandra", 

	"lastName":"Carvalho",

	"emailId":"alexandracarvalho@ua.pt"

}
```



[EmployeeRepository interface that supports search by email: ]

```java
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    public Employee findByEmail(String emailId);

}
```

[EmployeeController method that searches by email: ]

```java
@GetMapping("/employees?email={email}")
public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable(value = "email") String employeeEmail)
        throws ResourceNotFoundException{

    Employee employee = employeeRepository.findByEmail(employeeEmail);

    return ResponseEntity.ok().body(employee);
}
```



The **@Table** annotation is used to specify details about that table of the entity in the DB. In this case, it specifies its name as "employees".

The **@Column** annotation is used to access a column of the DB table.

The **@Id** annotation is used to specify the primary key of a record. In this case, it corresponds to a generated value of the id field.



The **@AutoWired** annotation is used to inject collaborating beans into the current bean. In this case, we're injecting the getters and setters of the employeeRepository into our employeeController.

---

---

​																				EXERCÍCIO 3



- **Generate**: Maven Project
- **Java Version**: 11
- **Spring Boot**: 2.4.0
- **Group**: ies.lab3
- **Artifact**: data-to-presentation
- **Name**: data-to-presentation
- **Description**: Micro Issues Reporting Application
- **Package Name** : ies.lab3
- **Packaging**: jar (This is the default value)
- **Dependencies**: Spring Web, Spring Data JPA, H2 Database, Thymeleaf, DevTools



[Issue Controller class: ]

```java
package ies.lab3.datatopresentation;
import org.springframework.web.bind.annotation.*;

@RestController
public class IssueController {
    @GetMapping("/issuereport")
    public String getReport(){
        return "/issues/issuereport_form";
    }

    @PostMapping("/issuereport")
    public String submitReport(){
        return "/issues/issuereport_form";
    }

    @GetMapping("/issues")
    public String getIssues(){
        return "/issues/issuereport_list";
    }

}
```



[IssueReport Entity class: ]

```java
package ies.lab3.datatopresentation;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "issues")
public class IssueReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String email;
    private String url;
    private String description;
    private boolean marked_as_private;
    private boolean updates;
    private boolean done;
    private Date created;
    private Date updated;

    public IssueReport(){}

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMarked_as_private(boolean marked_as_private) {
        this.marked_as_private = marked_as_private;
    }

    public void setUpdates(boolean updates) {
        this.updates = updates;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMarked_as_private() {
        return marked_as_private;
    }

    public boolean isUpdates() {
        return updates;
    }

    public boolean isDone() {
        return done;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }
}
```



[Templates W/ Timeleaf: ]

```html
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">

<head>
	<title>Vogella Issuereport</title>
    <link rel="stylesheet" href="./style.css" />
    <meta charset="UTF-8" />
</head>

<body>
    <div class="container issue_list">
        <h2>Issues</h2>
        <br />
        <table>
            <tr>
                <th>Url</th>
                <th class="desc">Description</th>
                <th>Done</th>
                <th>Created</th>
            </tr>
            <th:block th:each="issue : ${issues}">
                <tr>
                    <td ><a th:href="@{${issue.url}}" th:text="${issue.url}"></a></td>
                    <td th:text="${issue.description}">...</td>
                    <td><span class="status" th:classappend="${issue.done} ? done : pending"></span></td>
                    <td th:text="${issue.created}">...</td>
                </tr>
            </th:block>
        </table>
    </div>
</body>
</html>
```

```java
package ies.lab3.datatopresentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IssueController {
    @GetMapping("/issuereport")
    public String getReport(Model model){
        model.addAttribute("issuereport", new IssueReport());
        return "/issues/issuereport_form";
    }

    @PostMapping(value = "/issuereport")
    public String submitReport(IssueReport issueReport, Model model){
        model.addAttribute("issuereport", new IssueReport());
        model.addAttribute("submitted", true);
        return "/issues/issuereport_form";
    }

    @GetMapping("/issues")
    public String getIssues(Model model){
        return "/issues/issuereport_list";
    }

}
```



[Setting up Database integration requires an Issue Repository and some changes in Issue Controller : ]

```java
package ies.lab3.datatopresentation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface IssueRepository extends JpaRepository<IssueReport,Long> {
    @Query(value = "SELECT i FROM IssueReport i WHERE marked_as_private = false")
    List<IssueReport> findAllButPrivate();
    List<IssueReport> findAllByEmail(String email);
}
```

```java
package ies.lab3.datatopresentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IssueController {
    IssueRepository issueRepository;

    public IssueController(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @GetMapping("/issuereport")
    public String getReport(Model model, @RequestParam(name = "submitted", required = false) boolean submitted) {
        model.addAttribute("submitted", submitted);
        model.addAttribute("issuereport", new IssueReport());
        return "issues/issuereport_form";
    }

    @PostMapping("/issuereport")
    public String submitReport(IssueReport issueReport, RedirectAttributes ra) {
        IssueReport result = this.issueRepository.save(issueReport);
        ra.addAttribute("submitted", true);
        return "redirect:/issuereport";
    }

    @GetMapping("/issues")
    public String getIssues(Model model) {
        model.addAttribute("issues", this.issueRepository.findAllButPrivate());
        return "issues/issuereport_list";
    }
}
```



Para apresentar também o Id, a tabela deve ficar assim:

```html
    <table>
        <tr>
            <th>Id</th>
            <th>Url</th>
            <th class="desc">Description</th>
            <th>Done</th>
            <th>Created</th>
        </tr>
        <th:block th:each="issue : ${issues}">
            <tr>
                <td><a th:text="@{${issue.id}}"></a></td>
                <td ><a th:href="@{${issue.url}}" th:text="${issue.url}"></a></td>
                <td th:text="${issue.description}">...</td>
                <td><span class="status" th:classappend="${issue.done} ? done : pending"></span></td>
                <td th:text="${issue.created}">...</td>
            </tr>
        </th:block>
    </table>
```


**@RestController** = **@Controller** + **@ResponseBody**



O @Controller e o @RESTController acedem à base de dados através da interface do IssueRepository, que herda as propriedades do JpaRepository.