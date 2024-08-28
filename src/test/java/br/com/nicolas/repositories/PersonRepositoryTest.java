package br.com.nicolas.repositories;

import br.com.nicolas.integrationTests.testContainers.AbstractIntegrationsTest;
import br.com.nicolas.models.Person;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationsTest {

    @Autowired
    private PersonRepository repository;

    private Person person;

    @BeforeEach
    public void setup(){
        //Given
        person = new Person();
        person.setFirstName("Nicolas Arthur");
        person.setLastName("da Silva");
        person.setAddress("Barueri");
        person.setGender("Male");
        person.setEmail("nicolas@gmail.com");
    }

    @DisplayName("Given Person Object When Save Then Return Saved Person")
    @Test
    void testGivenPersonObject_WhenSave_ThenReturnSavedPerson(){

        //When
        Person savedPerson = repository.save(person);


        //Then
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);

    }

    @DisplayName("Given Person List When FindAll Then Return Person List")
    @Test
    void testGivenPersonList_WhenFindAll_ThenReturnPersonList(){

        //Given

        Person person2 = new Person();
        person2.setFirstName("Ana");
        person2.setLastName("Lopes");
        person2.setAddress("Barueri");
        person2.setGender("female");
        person2.setEmail("Ana@gmail.com");

        repository.save(person);
        repository.save(person2);

        //When
        List<Person> personList = repository.findAll();


        //Then
        assertNotNull(personList);
        assertEquals(2, personList.size());

    }

    @DisplayName("Given Person Object When FindById Then Return Person Object")
    @Test
    void testGivenPersonObject_WhenFindById_ThenReturnPersonObject(){

        repository.save(person);

        //When
        Person savedPerson = repository.findById(person.getId()).get();


        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getId(),savedPerson.getId());

    }

    @DisplayName("Given Person Object When FindByEmail Then Return Person Object")
    @Test
    void testGivenPersonObject_WhenFindByEmail_ThenReturnPersonObject(){

        repository.save(person);

        //When
        Person savedPerson = repository.findByEmail(person.getEmail()).get();


        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getId(),savedPerson.getId());
        assertEquals(person.getEmail(),savedPerson.getEmail());

    }

    @DisplayName("Given Person Object When UpdatePerson Then Return Updated Person Object")
    @Test
    void testGivenPersonObject_WhenUpdatePerson_ThenReturnUpdatedPersonObject(){

        repository.save(person);

        //When
        Person savedPerson = repository.findById(person.getId()).get();
        savedPerson.setFirstName("Leonardo");
        savedPerson.setEmail("leonardo@gmail.com");

        Person updatePerson = repository.save(savedPerson);


        //Then
        assertNotNull(updatePerson);
        assertEquals(savedPerson.getId(),updatePerson.getId());
        assertEquals(savedPerson.getFirstName(),updatePerson.getFirstName());
        assertEquals(savedPerson.getEmail(),updatePerson.getEmail());

    }

    @DisplayName("Given Person Object When Delete Then Return Remove Person")
    @Test
    void testGivenPersonObject_WhenDelete_ThenReturnRemovePerson(){

        repository.save(person);

        //When
        repository.deleteById(person.getId());
        Optional<Person> personOptional = repository.findById(person.getId());


        //Then
        assertTrue(personOptional.isEmpty());

    }

    @DisplayName("Given FirstName And LastName When FindByJPQL Then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_WhenFindByJPQL_ThenReturnPersonObject(){

        repository.save(person);

        //When
        Person savedPerson = repository.findByJPQL(person.getFirstName(), person.getLastName());


        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getId(),savedPerson.getId());
        assertEquals(person.getFirstName(),savedPerson.getFirstName());
        assertEquals(person.getLastName(),savedPerson.getLastName());

    }

    @DisplayName("Given FirstName And LastName When FindByJPQL Then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_WhenFindByJPQLNamedParameters_ThenReturnPersonObject(){

        repository.save(person);

        //When
        Person savedPerson = repository.findByJPQLNamedParameters(person.getFirstName(), person.getLastName());


        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getId(),savedPerson.getId());
        assertEquals(person.getFirstName(),savedPerson.getFirstName());
        assertEquals(person.getLastName(),savedPerson.getLastName());

    }

    @DisplayName("Given FirstName And LastName When FindByNativeSQL Then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_WhenFindByFindByNativeSQL_ThenReturnPersonObject(){

        repository.save(person);

        //When
        Person savedPerson = repository.findByNativeSQL(person.getFirstName(), person.getLastName());


        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getId(),savedPerson.getId());
        assertEquals(person.getFirstName(),savedPerson.getFirstName());
        assertEquals(person.getLastName(),savedPerson.getLastName());

    }

    @DisplayName("Given FirstName And LastName When FindByNativeSQLNamedParameters Then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_WhenFindByFindByNativeSQLNamedParameters_ThenReturnPersonObject(){

        repository.save(person);

        //When
        Person savedPerson = repository.findByNativeSQLNamedParameters(person.getFirstName(), person.getLastName());


        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getId(),savedPerson.getId());
        assertEquals(person.getFirstName(),savedPerson.getFirstName());
        assertEquals(person.getLastName(),savedPerson.getLastName());

    }

}
