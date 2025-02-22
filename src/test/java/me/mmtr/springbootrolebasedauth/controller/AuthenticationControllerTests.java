package me.mmtr.springbootrolebasedauth.controller;

import me.mmtr.springbootrolebasedauth.data.Role;
import me.mmtr.springbootrolebasedauth.data.User;
import me.mmtr.springbootrolebasedauth.data.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import me.mmtr.springbootrolebasedauth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("classpath:templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void shouldReturnLoginViewWithoutParameters() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error", "logout"));
    }

    @Test
    public void shouldReturnLoginViewWithAnError() throws Exception {
        mockMvc.perform(get("/login").param("error", "errorParamValue"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeDoesNotExist("logout"))
                .andExpect(model().attribute("error", "Incorrect username or password provided"));
    }

    @Test
    public void shouldReturnLoginViewWithLogoutMessage() throws Exception {
        mockMvc.perform(get("/login").param("logout", "logoutParamValue"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("logout"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attribute("logout", "logoutParamValue"));
    }

    @Test
    public void shouldReturnRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        UserDTO userDTO = new UserDTO("username", "password");

        mockMvc.perform(post("/register")
                        .param("username", userDTO.getUsername())
                        .param("password", userDTO.getPassword())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).registerUser(userDTO, RoleName.USER);
    }

    @Test
    public void shouldReturnRegistrationFormWithErrorWhenUsernameExists() throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User(1L,
                "testUsername", "testPassword", new Role())));

        mockMvc.perform(post("/register")
                        .param("username", "username")
                        .param("password", "password")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));

        verify(userService, never()).registerUser(any(), any());
    }
}