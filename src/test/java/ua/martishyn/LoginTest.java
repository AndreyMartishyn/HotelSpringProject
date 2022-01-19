package ua.martishyn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ua.martishyn.controller.MainController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class LoginTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainController mainController;

    @Test
    void shouldRedirectFromMainPageToLoginWhenGuest() throws Exception {
        this.mockMvc.perform(get("/main")).andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(("http://localhost/login")));
    }
    @Test
    @Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/messages-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnMainPageAfterSuccessfulLogin() throws Exception {
        this.mockMvc.perform(formLogin().user("admin").password("123"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
    @Test
    void shouldNotLoginWhenWrongCredentials() throws Exception {
        this.mockMvc.perform(post("/login").param("wrongUser", "wrongPassword"))
                .andDo(print())
                .andExpect(redirectedUrl("/login?error"));
    }
}



