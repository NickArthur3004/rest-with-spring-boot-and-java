package br.com.nicolas.integrationTests.controllers;

import br.com.nicolas.config.TestConfigs;
import br.com.nicolas.integrationTests.testContainers.AbstractIntegrationsTest;
import br.com.nicolas.models.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationsTest {

    private static RequestSpecification specification;

    private static ObjectMapper objectMapper;

    private static Person person;

    @BeforeAll
    public static void setup(){
        //Given
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person(
                1L,
                "Nicolas Arthur",
                "da Silva",
                "Barueri",
                "Male",
                "nicolas@gmail.com");
    }

    @Test
    @Order(1)
    @DisplayName("integration Test Given Person Object when CreateOnePerson Should Return A Person Object")
    void integrationTestGivenPersonObject_when_CreateOnePerson_ShouldReturnAPersonObject() throws JsonProcessingException {

        var content = given()
                .spec(specification)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                            .asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getEmail());

        assertTrue(createdPerson.getId() > 0);
        assertEquals("Nicolas Arthur",createdPerson.getFirstName());
        assertEquals("da Silva",createdPerson.getLastName());
        assertEquals("Barueri",createdPerson.getAddress());
        assertEquals("Male",createdPerson.getGender());
        assertEquals("nicolas@gmail.com",createdPerson.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("integration Test Given Person Object when UpdateOnePerson Should Return A Updated Person Object")
    void integrationTestGivenPersonObject_when_UpdateOnePerson_ShouldReturnAUpdatedPersonObject() throws JsonProcessingException {

        person.setFirstName("Nick Arthur");
        person.setEmail("nick@gmail.com");
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

        Person updatedPerson = objectMapper.readValue(content, Person.class);

        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());
        assertNotNull(updatedPerson.getEmail());

        assertTrue(updatedPerson.getId() > 0);
        assertEquals("Nick Arthur",updatedPerson.getFirstName());
        assertEquals("da Silva",updatedPerson.getLastName());
        assertEquals("Barueri",updatedPerson.getAddress());
        assertEquals("Male",updatedPerson.getGender());
        assertEquals("nick@gmail.com",updatedPerson.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("integration Test Given Person Object when FindById Should Return A Person Object")
    void integrationTestGivenPersonObject_when_FindById_ShouldReturnAPersonObject() throws JsonProcessingException {

        var content = given()
                .spec(specification)
                .pathParam("id", person.getId())
                .when()
                .get("/{id}")
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        Person foundPerson = objectMapper.readValue(content, Person.class);

        assertNotNull(foundPerson);
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
        assertNotNull(foundPerson.getEmail());

        assertTrue(foundPerson.getId() > 0);
        assertEquals("Nick Arthur",foundPerson.getFirstName());
        assertEquals("da Silva",foundPerson.getLastName());
        assertEquals("Barueri",foundPerson.getAddress());
        assertEquals("Male",foundPerson.getGender());
        assertEquals("nick@gmail.com",foundPerson.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("integration Test when FindById Should Return A Person List")
    void integrationTest_when_FindAll_ShouldReturnAPersonList() throws JsonProcessingException {

        Person anotherPerson = new Person(
                2L,
                "Alanis",
                "da Silva",
                "Barueri",
                "Female",
                "alanis@gmail.com");

        given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(anotherPerson)
                .when()
                .post()
                .then()
                .statusCode(200);

        var content = given()
                .spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

        List<Person> peaple = Arrays.asList(objectMapper.readValue(content, Person[].class));

        Person foundPersonOne = peaple.get(1);

        assertEquals("Alanis",foundPersonOne.getFirstName());
        assertEquals("da Silva",foundPersonOne.getLastName());
        assertEquals("Barueri",foundPersonOne.getAddress());
        assertEquals("Female",foundPersonOne.getGender());
        assertEquals("alanis@gmail.com",foundPersonOne.getEmail());

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertNotNull(foundPersonOne.getEmail());

        assertTrue(foundPersonOne.getId() > 0);


        Person foundPersonTwo = peaple.get(0);

        assertNotNull(foundPersonTwo.getId());
        assertNotNull(foundPersonTwo.getFirstName());
        assertNotNull(foundPersonTwo.getLastName());
        assertNotNull(foundPersonTwo.getAddress());
        assertNotNull(foundPersonTwo.getGender());
        assertNotNull(foundPersonTwo.getEmail());

        assertTrue(foundPersonOne.getId() > 0);

        assertEquals("Nick Arthur",foundPersonTwo.getFirstName());
        assertEquals("da Silva",foundPersonTwo.getLastName());
        assertEquals("Barueri",foundPersonTwo.getAddress());
        assertEquals("Male",foundPersonTwo.getGender());
        assertEquals("nick@gmail.com",foundPersonTwo.getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("integration Test Given Person Object when Delete Should Return No Content")
    void integrationTestGivenPersonObject_when_Delete_ShouldReturnNoContent() throws JsonProcessingException {
        given()
                .spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204);
    }

}
