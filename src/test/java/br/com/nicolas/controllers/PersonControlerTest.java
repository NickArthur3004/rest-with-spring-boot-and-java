package br.com.nicolas.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import br.com.nicolas.exceptions.ResourceNotFoundException;
import br.com.nicolas.models.Person;
import br.com.nicolas.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class PersonControlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
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


    @DisplayName("Given Person Object When Create Person Then Return Saved Person")
    @Test
    void testGivenPersonObject_WhenCreatePerson_ThenReturnSavedPerson() throws Exception {

        //given
        given(service.create(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //When
        ResultActions response = mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(person)));

        //Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));

    }

    @DisplayName("Given List Of Person When FindAllPerson Then Return Persons List")
    @Test
    void testGivenListOfPerson_WhenFindAllPerson_ThenReturnPersonsList() throws Exception {

        //given
        Person person2 = new Person();
        person2.setFirstName("Ana");
        person2.setLastName("Lopes");
        person2.setAddress("Barueri");
        person2.setGender("female");
        person2.setEmail("Ana@gmail.com");

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);

        given(service.findAll())
                .willReturn(persons);

        //When
        ResultActions response = mockMvc.perform(get("/person"));

        //Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(persons.size())));

    }

    @DisplayName("Given Person ID When FindById Then Return Person Object")
    @Test
    void testGivenPersonID_WhenFindById_ThenReturnPersonObject() throws Exception {

        //given
        Long personId = 1L;
        given(service.findById(personId))
                .willReturn(person);

        //When
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        //Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));

    }

    @DisplayName("Given Invalid Person ID When FindById Then Return Not Found")
    @Test
    void testGivenInvalidPersonID_WhenFindById_ThenReturnNotFound() throws Exception {

        //given
        Long personId = 1L;
        given(service.findById(personId))
                .willThrow(ResourceNotFoundException.class);

        //When
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        //Then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("Given Update Person When Update Then Return Updated Person Object")
    @Test
    void testGivenUpdatePerson_WhenUpdate_ThenReturnUpdatedPersonObject() throws Exception {

        //given
        Long personId = 1L;

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Ana");
        updatedPerson.setLastName("Lopes");
        updatedPerson.setAddress("Barueri");
        updatedPerson.setGender("female");
        updatedPerson.setEmail("Ana@gmail.com");

        given(service.findById(personId))
                .willReturn(person);
        given(service.update(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //When
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        //Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));

    }

    @DisplayName("Given Unexistent Person When Update Then Return Not Found")
    @Test
    void testGivenUnexistentPerson_WhenUpdate_ThenReturnNotFound() throws Exception {

        //given
        Long personId = 1L;

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Ana");
        updatedPerson.setLastName("Lopes");
        updatedPerson.setAddress("Barueri");
        updatedPerson.setGender("female");
        updatedPerson.setEmail("Ana@gmail.com");

        given(service.findById(personId))
                .willThrow(ResourceNotFoundException.class);
        given(service.update(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(1));

        //When
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        //Then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("test Given Person Id When Delete Then Return No Content")
    @Test
    void testGivenPersonId_WhenDelete_ThenReturnNoContent() throws Exception {

        //given
        Long personId = 1L;
        willDoNothing().given(service).delete(personId);

        //When
        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));

        //Then
        response.andExpect(status().isNoContent())
                .andDo(print());

    }
}
