package eg.mos.sportify.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.Gender;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.dto.user.UserRequestDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProfileControllerTest {

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
    public void testUpdateUserProfile_Success() throws Exception {
        ProfileDTO profileDTO = ProfileDTO.builder()
                .phone("testUpdated")
                .profilePicture("testUpdated")
                .bio("testUpdated")
                .gender(Gender.FEMALE)
                .location("testUpdated")
                .lastName("testUpdated")
                .firstName("testUpdated")
                .dateOfBirth("testUpdated")
                .build();
        User user = this.userRepository.findByUsername("test").get();
        mockMvc.perform(put("/profiles/"+user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDTO))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.gender").value("FEMALE"));
    }

    @Test
    public void testUpdateUserProfile_Fail() throws Exception {
        ProfileDTO profileDTO = ProfileDTO.builder()
                .phone("testUpdated")
                .profilePicture("testUpdated")
                .bio("testUpdated")
                .gender(Gender.FEMALE)
                .location("testUpdated")
                .lastName("testUpdated")
                .firstName("testUpdated")
                .dateOfBirth("testUpdated")
                .build();
        mockMvc.perform(put("/profiles/100000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDTO))
                        .header("Authorization", token))
                .andExpect(jsonPath("$.success").value(false));
    }
}
