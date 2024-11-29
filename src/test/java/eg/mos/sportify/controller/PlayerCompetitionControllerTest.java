package eg.mos.sportify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.PlayerCompetition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import eg.mos.sportify.domain.enums.Gender;
import eg.mos.sportify.domain.enums.PlayerRole;
import eg.mos.sportify.dto.player_competition.PlayerCompetitionRequestDTO;
import eg.mos.sportify.dto.player_competition.PlayerScoreRequestDTO;
import eg.mos.sportify.dto.profile.ProfileDTO;
import eg.mos.sportify.dto.user.UserRequestDTO;
import eg.mos.sportify.repository.CompetitionRepository;
import eg.mos.sportify.repository.PlayerCompetitionRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PlayerCompetitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private PlayerCompetitionRepository playerCompetitionRepository;


    @BeforeEach
    public void loginAndRetrieveToken() throws Exception {
        Optional<User> user = userRepository.findByUsername("test");
        if (user.isEmpty()) {
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
    public void testAddPlayerToCompetition_Success() throws Exception {
        User user = userRepository.findByUsername("test").get();
        Competition competition = this.setupCompetition();
        PlayerCompetitionRequestDTO playerCompetitionRequestDTO = PlayerCompetitionRequestDTO.builder()
                .competitionId(competition.getCompetitionId())
                .userId(user.getUserId())
                .build();
        mockMvc.perform(post("/player-competition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(playerCompetitionRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testUpdatePlayerScore_Success() throws Exception {
        User user = userRepository.findByUsername("test").get();
        Competition competition = this.setupCompetition();
        this.playerCompetitionRepository.save(PlayerCompetition.builder()
                .score(0)
                .player(user)
                .competition(competition)
                .role(PlayerRole.PLAYER)
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build());
        PlayerScoreRequestDTO playerScoreRequestDTO = PlayerScoreRequestDTO.builder()
                .competitionId(competition.getCompetitionId())
                .userId(user.getUserId())
                .score(80)
                .build();
        mockMvc.perform(put("/player-competition/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerScoreRequestDTO))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testUpdatePlayerScore_Fail() throws Exception {
        User user = userRepository.findByUsername("test").get();
        Competition competition = this.setupCompetition();
        this.playerCompetitionRepository.save(PlayerCompetition.builder()
                .score(0)
                .player(user)
                .competition(competition)
                .role(PlayerRole.PLAYER)
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build());
        PlayerScoreRequestDTO playerScoreRequestDTO = PlayerScoreRequestDTO.builder()
                .competitionId(competition.getCompetitionId())
                .userId(user.getUserId())
                .score(-80)
                .build();
        mockMvc.perform(put("/player-competition/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerScoreRequestDTO))
                        .header("Authorization", token))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Score must be zero or more")));
    }

    @Test
    public void testRemovePlayerFromCompetition_Success() throws Exception {
        User user = userRepository.findByUsername("test").get();
        Competition competition = this.setupCompetition();
        this.playerCompetitionRepository.save(PlayerCompetition.builder()
                .score(0)
                .player(user)
                .competition(competition)
                .role(PlayerRole.PLAYER)
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build());
        PlayerCompetitionRequestDTO playerCompetitionRequestDTO = PlayerCompetitionRequestDTO.builder()
                .competitionId(competition.getCompetitionId())
                .userId(user.getUserId())
                .build();
        mockMvc.perform(delete("/player-competition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(playerCompetitionRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetUserCompetitionReport_Success() throws Exception {
        User user = userRepository.findByUsername("test").get();
        Competition competition = this.setupCompetition();
        this.playerCompetitionRepository.save(PlayerCompetition.builder()
                .score(0)
                .player(user)
                .competition(competition)
                .role(PlayerRole.PLAYER)
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build());
        mockMvc.perform(get("/player-competition/" + user.getUserId() + "/competition-report")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    private Competition setupCompetition() throws Exception {
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
    public void clearCompetition() throws Exception {
        this.playerCompetitionRepository.deleteAll();
        this.competitionRepository.deleteAll();
    }
}
