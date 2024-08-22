package ch.martinelli.demoweb.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addTodo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "text": "add"
                                }"""))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(delete("/todos/{id}", mvcResult.getResponse().getHeader("Location")))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTodos() throws Exception {
        mockMvc.perform(get("/todos")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {"id":1,"text":"Blumen giessen","done":false},
                          {"id":2,"text":"Rasen mähen","done":false},
                          {"id":3,"text":"Einkaufen","done":false}
                        ]""".trim()));
    }

    @Test
    void getTodo() throws Exception {
        mockMvc.perform(get("/todos/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"id":1,"text":"Blumen giessen","done":false}""".trim()));
    }

    @Test
    void deleteTodo() throws Exception {
        mockMvc.perform(delete("/todos/{id}", 1))
                .andExpect(status().isOk());
    }
}