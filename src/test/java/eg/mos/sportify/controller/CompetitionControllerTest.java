package eg.mos.sportify.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import eg.mos.sportify.domain.enums.Gender;
import eg.mos.sportify.dto.competition.CompetitionChangeStatusDTO;
import eg.mos.sportify.dto.competition.CompetitionRequestDTO;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.dto.user.UserRequestDTO;
import eg.mos.sportify.repository.CompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompetitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Autowired
    private CompetitionRepository competitionRepository;

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
    @Transactional
    public void testAddCompetition_Success() throws Exception {
        CompetitionRequestDTO competitionRequestDTO = CompetitionRequestDTO.builder()
                .name("test1")
                .status(CompetitionStatus.UPCOMING)
                .description("test1")
                .startDate(LocalDateTime.now().plusDays(7))
                .endDate(LocalDateTime.now().plusDays(30))
                .maxScore(100)
                .build();
        mockMvc.perform(post("/competitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(competitionRequestDTO))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    public void testChangeCompetitionStatus_Success() throws Exception {
        Competition competition = this.setupCompetition();
        CompetitionChangeStatusDTO competitionChangeStatusDTO = CompetitionChangeStatusDTO.builder()
                .competitionId(competition.getCompetitionId())
                .status(CompetitionStatus.CANCELLED)
                .build();
        mockMvc.perform(put("/competitions/change-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(competitionChangeStatusDTO))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    private Competition setupCompetition() {
        User user = this.userRepository.findByUsername("test").get();
        Competition competition = Competition.builder()
                .name("test")
                .status(CompetitionStatus.UPCOMING)
                .description("test")
                .startDate(LocalDateTime.now().plusDays(7))
                .endDate(LocalDateTime.now().plusDays(30))
                .maxScore(100)
                .admin(User.builder().userId(user.getUserId()).build())
                .auditData(AuditData.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();
        return this.competitionRepository.save(competition);
    }

    @AfterEach
    public void clearCompetition() {
        this.competitionRepository.deleteAll();
    }
}
