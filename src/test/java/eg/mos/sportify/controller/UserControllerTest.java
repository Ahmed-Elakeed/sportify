package eg.mos.sportify.controller;


import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.Gender;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.dto.user.UserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import eg.mos.sportify.dto.user.UserSearchDTO;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void loginAndRetrieveToken() throws Exception {
        Optional<User> user = userRepository.findByUsername("test");
        if(user.isEmpty()) {
            UserRequestDTO request = UserRequestDTO.builder()
                    .email("test@gmail.com")
                    .password("test")
                    .username("test")
                    .profile(ProfileDTO.builder()
                            .bio("test")
                            .dateOfBirth(LocalDate.now().toString())
                            .firstName("test")
                            .gender(Gender.MALE)
                            .lastName("test")
                            .location("test")
                            .profilePicture("test")
                            .phone("test")
                            .build())
                    .build();
            this.userService.register(request);
        }
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "test");
        loginRequest.put("password", "test");

        String response = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        token = objectMapper.readTree(response).get("data").asText();
    }

    @Test
    public void testUserRegister_Success() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .email("test1@gmail.com")
                .password("test1")
                .username("test1")
                .profile(ProfileDTO.builder()
                        .bio("test1")
                        .dateOfBirth(LocalDate.now().toString())
                        .firstName("test1")
                        .gender(Gender.MALE)
                        .lastName("test1")
                        .location("test1")
                        .profilePicture("test1")
                        .phone("test1")
                        .build())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    public void testUserRegister_Fail() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .email("test@gmail.com")
                .password("test1")
                .username("test")
                .profile(ProfileDTO.builder()
                        .bio("test1")
                        .dateOfBirth(LocalDate.now().toString())
                        .firstName("test1")
                        .gender(Gender.MALE)
                        .lastName("test1")
                        .location("test1")
                        .profilePicture("test1")
                        .phone("test1")
                        .build())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.success").value(false));

    }

    @Test
    public void testUserLogin_Success() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "test");
        loginRequest.put("password", "test");

    mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testUserLogin_Fail() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "not_exist_user");
        loginRequest.put("password", "test");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testGetUser_Success() throws Exception {
        User user = this.userRepository.findByUsername("test").get();
        mockMvc.perform(get("/users/"+user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token))
                .andExpect(jsonPath("$.data.profile.lastName").value("test"))
                .andExpect(jsonPath("$.success").value(true));

    }


    @Test
    public void testSearchUsers_Success() throws Exception {
        UserSearchDTO userSearchDTO = UserSearchDTO.builder()
                .firstName("test")
                .phone("test")
                .build();
        mockMvc.perform(get("/users/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(userSearchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].username").value("test"));
    }
}
