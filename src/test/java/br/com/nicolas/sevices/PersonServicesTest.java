package br.com.nicolas.sevices;

import br.com.nicolas.exceptions.ResourceNotFoundException;
import br.com.nicolas.models.Person;
import br.com.nicolas.repositories.PersonRepository;
import br.com.nicolas.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

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

    @DisplayName("Given Person Object When Save Person Then Return Person Object")
    @Test
    void testGivenPersonObject_WhenSavePerson_ThenReturnPersonObject(){

        //given
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person)).willReturn(person);

        //When
        Person savedPerson = service.create(person);

        //Then
        assertNotNull(savedPerson);
        assertEquals("Nicolas Arthur",savedPerson.getFirstName());

    }

    @DisplayName("Given Existing Email When Save Person Then Throws Exception")
    @Test
    void testGivenExistingEmail_WhenSavePerson_ThenThrowsException(){

        //given
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person));

        //When
        assertThrows(ResourceNotFoundException.class, () -> {
            service.create(person);
        });

        //Then
        verify(repository, never()).save(any(Person.class));

    }

    @DisplayName("Given Person List When FindAllPerson Then Return Persons List")
    @Test
    void testGivenPersonList_WhenFindAllPerson_ThenReturnPersonList(){

        Person person2 = new Person();
        person2.setFirstName("Ana");
        person2.setLastName("Lopes");
        person2.setAddress("Barueri");
        person2.setGender("female");
        person2.setEmail("Ana@gmail.com");

        //given
        given(repository.findAll()).willReturn(List.of(person,person2));

        //When
        List<Person> personList = service.findAll();

        //Then
        assertNotNull(personList);
        assertEquals(2, personList.size());

    }

    @DisplayName("Given Empty Person List When FindAllPerson Then Return Empty Persons List")
    @Test
    void testGivenEmptyPersonList_WhenFindAllPerson_ThenReturnEmptyPersonList(){

        //given
        given(repository.findAll()).willReturn(Collections.emptyList());

        //When
        List<Person> personList = service.findAll();

        //Then
        assertTrue(personList.isEmpty());
        assertEquals(0, personList.size());

    }

    @DisplayName("Given Person Id When FindById Then Return Person Object")
    @Test
    void testGivenPersonId_WhenFindById_ThenReturnPersonObject(){

        //given
        given(repository.findById(anyLong())).willReturn(Optional.of(person));

        //When
        Person savedPerson = service.findById(1L);

        //Then
        assertNotNull(savedPerson);
        assertEquals("Nicolas Arthur",savedPerson.getFirstName());

    }

    @DisplayName("Given Person Object When UpdatePerson Then Return Updated Person")
    @Test
    void testGivenPersonObject_WhenUpdatePerson_ThenReturnUpdatedPerson(){

        //given
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person));

        person.setEmail("novo@gmail.com");
        person.setFirstName("Leonardo");
        given(repository.save(person)).willReturn(person);

        //When
        Person updatedPerson = service.update(person);

        //Then
        assertNotNull(updatedPerson);
        assertEquals("Leonardo",updatedPerson.getFirstName());
        assertEquals("novo@gmail.com", updatedPerson.getEmail());

    }

    @DisplayName("Given Person Id When DeletePerson Then Do Nothing")
    @Test
    void testGivenPersonId_WhenDeletePerson_ThenDoNothing(){

        //given
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        willDoNothing().given(repository).delete(person);

        //When
        service.delete(person.getId());

        //Then
        verify(repository,times(1)).delete(person);

    }

}
