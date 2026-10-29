package kz.suleimen.studentapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentApiIntegrationTest {
    @Autowired
    private MockMvc mvc;

    private static final String STUDENT = """
        {"fullName":"Suleimen Yeldossuly","email":"suleimen38320@example.com","groupName":"IT2-2310"}
        """;

    @Test
    void createAndReadStudent() throws Exception {
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(STUDENT))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/students/")))
            .andExpect(jsonPath("$.fullName").value("Suleimen Yeldossuly"));

        mvc.perform(get("/api/students"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].groupName").value("IT2-2310"));
    }

    @Test
    void updateAndDeleteStudent() throws Exception {
        String location = mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(STUDENT))
            .andReturn().getResponse().getHeader("Location");

        mvc.perform(put(location).contentType(MediaType.APPLICATION_JSON).content("""
            {"fullName":"Suleimen Yeldossuly","email":"updated38320@example.com","groupName":"IT2-2310"}
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("updated38320@example.com"));

        mvc.perform(delete(location)).andExpect(status().isNoContent());
        mvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    void missingStudentReturns404() throws Exception {
        mvc.perform(get("/api/students/999999")).andExpect(status().isNotFound());
    }

    @Test
    void invalidStudentReturns400() throws Exception {
        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fullName\":\"\",\"email\":\"invalid\",\"groupName\":\"\"}"))
            .andExpect(status().isBadRequest());
    }
}
