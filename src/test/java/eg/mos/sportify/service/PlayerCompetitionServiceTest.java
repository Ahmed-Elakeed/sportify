package eg.mos.sportify.service;

import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.PlayerCompetition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import eg.mos.sportify.domain.enums.PlayerRole;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.player_competition.PlayerCompetitionRequestDTO;
import eg.mos.sportify.dto.player_competition.PlayerScoreRequestDTO;
import eg.mos.sportify.dto.user.UserCompetitionReport;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.CompetitionRepository;
import eg.mos.sportify.repository.PlayerCompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerCompetitionServiceTest {

    @MockBean
    private PlayerCompetitionRepository playerCompetitionRepository;
    @MockBean
    private CompetitionRepository competitionRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private PlayerCompetitionService playerCompetitionService;


    @Test
    public void testAddPlayerToCompetition_Success() {
        User user = User.builder().username("test").userId(1L).build();
        Competition competition = Competition.builder()
                .competitionId(1L)
                .name("test")
                .admin(user)
                .status(CompetitionStatus.UPCOMING)
                .startDate(LocalDateTime.now())
                .maxScore(100)
                .endDate(LocalDateTime.now())
                .description("test")
                .build();
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));
        PlayerCompetition playerCompetition = PlayerCompetition.builder()
                .competition(competition)
                .player(user)
                .score(50)
                .role(PlayerRole.PLAYER)
                .playerCompetitionId(1L)
                .build();
        when(playerCompetitionRepository.findByUserIdAndCompetitionId(1L, 1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerCompetitionRepository.save(any(PlayerCompetition.class))).thenReturn(playerCompetition);
        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getCurrentUser)
                    .thenReturn(user);
            ApiResponse<String> addPlayerToCompetitionResponse = playerCompetitionService.addPlayerToCompetition(
                    PlayerCompetitionRequestDTO.builder()
                            .competitionId(1L)
                            .userId(1L)
                            .build()
            );

            assertTrue(addPlayerToCompetitionResponse.isSuccess());
            assertEquals(addPlayerToCompetitionResponse.getMessage(), "Player added to competition successfully.");
        }
    }

    @Test
    public void testAddPlayerToCompetition_Fail1() {
        when(competitionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> playerCompetitionService.addPlayerToCompetition(
                PlayerCompetitionRequestDTO.builder()
                        .competitionId(1L)
                        .userId(1L)
                        .build()
        ));
    }

    @Test
    public void testAddPlayerToCompetition_Fail2() {
        Competition competition = Competition.builder()
                .competitionId(1L)
                .name("test")
                .admin(User.builder().username("test").userId(1L).build())
                .status(CompetitionStatus.UPCOMING)
                .startDate(LocalDateTime.now())
                .maxScore(100)
                .endDate(LocalDateTime.now())
                .description("test")
                .build();
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));
        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getCurrentUser)
                    .thenReturn(User.builder().username("not_test").userId(2L).build());
            assertThrows(AuthorizationException.class, () -> playerCompetitionService.addPlayerToCompetition(
                    PlayerCompetitionRequestDTO.builder()
                            .competitionId(1L)
                            .userId(1L)
                            .build()
            ));
        }
    }

    @Test
    public void testUpdatePlayerScore_Success() {
        User user = User.builder().username("test").userId(1L).build();
        Competition competition = Competition.builder()
                .competitionId(1L)
                .name("test")
                .admin(user)
                .status(CompetitionStatus.UPCOMING)
                .startDate(LocalDateTime.now())
                .maxScore(100)
                .endDate(LocalDateTime.now())
                .description("test")
                .build();
        PlayerCompetition playerCompetition = PlayerCompetition.builder()
                .competition(competition)
                .player(user)
                .score(50)
                .role(PlayerRole.PLAYER)
                .playerCompetitionId(1L)
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build();
        when(playerCompetitionRepository.findByUserIdAndCompetitionId(1L, 1L)).thenReturn(Optional.of(playerCompetition));
        when(playerCompetitionRepository.save(any(PlayerCompetition.class))).thenReturn(playerCompetition);
        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getCurrentUser)
                    .thenReturn(user);

            ApiResponse<String> updatePlayerScoreResponse = playerCompetitionService.updatePlayerScore(
                    PlayerScoreRequestDTO.builder()
                            .competitionId(1L)
                            .userId(1L)
                            .score(60)
                            .build()
            );

            assertTrue(updatePlayerScoreResponse.isSuccess());
            assertEquals(updatePlayerScoreResponse.getMessage(), "Player score updated successfully.");
        }
    }

    @Test
    public void testUpdatePlayerScore_Fail1() {
        User user = User.builder().username("test").userId(1L).build();
        Competition competition = Competition.builder()
                .competitionId(1L)
                .name("test")
                .admin(user)
                .status(CompetitionStatus.UPCOMING)
                .startDate(LocalDateTime.now())
                .maxScore(100)
                .endDate(LocalDateTime.now())
                .description("test")
                .build();
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));
        when(playerCompetitionRepository.findByUserIdAndCompetitionId(1L, 1L)).thenReturn(Optional.empty());
        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getCurrentUser)
                    .thenReturn(user);
            assertThrows(NotFoundException.class, () -> playerCompetitionService.updatePlayerScore(
                    PlayerScoreRequestDTO.builder()
                            .competitionId(1L)
                            .userId(1L)
                            .score(60)
                            .build()
            ));
        }
    }

    @Test
    public void testUpdatePlayerScore_Fail2() {
        User user = User.builder().username("test").userId(1L).build();
        Competition competition = Competition.builder()
                .competitionId(1L)
                .name("test")
                .admin(user)
                .status(CompetitionStatus.UPCOMING)
                .startDate(LocalDateTime.now())
                .maxScore(100)
                .endDate(LocalDateTime.now())
                .description("test")
                .build();
        PlayerCompetition playerCompetition = PlayerCompetition.builder()
                .competition(competition)
                .player(user)
                .score(50)
                .role(PlayerRole.PLAYER)
                .playerCompetitionId(1L)
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build();
        when(playerCompetitionRepository.findByUserIdAndCompetitionId(1L, 1L)).thenReturn(Optional.of(playerCompetition));
        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getCurrentUser)
                    .thenReturn(User.builder().username("not_test").userId(2L).build());

            assertThrows(AuthorizationException.class, () -> playerCompetitionService.updatePlayerScore(
               PlayerScoreRequestDTO.builder()
                       .competitionId(1L)
                       .userId(1L)
                       .score(60)
                       .build()
            ));
        }
    }

    @Test
    public void testRemovePlayerFromCompetition_Success() {
        User user = User.builder().username("test").userId(1L).build();
        Competition competition = Competition.builder()
                .competitionId(1L)
                .name("test")
                .admin(user)
                .status(CompetitionStatus.UPCOMING)
                .startDate(LocalDateTime.now())
                .maxScore(100)
                .endDate(LocalDateTime.now())
                .description("test")
                .build();
        PlayerCompetition playerCompetition = PlayerCompetition.builder()
                .competition(competition)
                .player(user)
                .score(50)
                .role(PlayerRole.PLAYER)
                .playerCompetitionId(1L)
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build();
        when(playerCompetitionRepository.findByUserIdAndCompetitionId(1L, 1L)).thenReturn(Optional.of(playerCompetition));
        doNothing().when(playerCompetitionRepository).delete(any(PlayerCompetition.class));
        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getCurrentUser)
                    .thenReturn(user);

            assertDoesNotThrow(() -> playerCompetitionService.removePlayerFromCompetition(
                    PlayerCompetitionRequestDTO.builder()
                            .competitionId(1L)
                            .userId(1L)
                            .build()
            ));
            verify(playerCompetitionRepository, times(1)).delete(any(PlayerCompetition.class));
        }
    }

    @Test
    public void testRemovePlayerFromCompetition_Fail() {
        when(playerCompetitionRepository.findByUserIdAndCompetitionId(1L, 1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> playerCompetitionService.removePlayerFromCompetition(
                PlayerCompetitionRequestDTO.builder()
                        .competitionId(1L)
                        .userId(1L)
                        .build()
        ));
    }

    @Test
    public void testGetUserCompetitionReport_Success() {
        User user = User.builder().username("test").userId(1L).build();
        Competition competition = Competition.builder()
                .competitionId(1L)
                .name("test")
                .admin(user)
                .status(CompetitionStatus.UPCOMING)
                .startDate(LocalDateTime.now())
                .maxScore(100)
                .endDate(LocalDateTime.now())
                .description("test")
                .build();
        PlayerCompetition playerCompetition = PlayerCompetition.builder()
                .competition(competition)
                .player(user)
                .score(50)
                .role(PlayerRole.PLAYER)
                .playerCompetitionId(1L)
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerCompetitionRepository.findAllByPlayerUserId(1L)).thenAnswer(invocation -> List.of(playerCompetition));

        ApiResponse<UserCompetitionReport> getUserCompetitionReportResponse = playerCompetitionService.getUserCompetitionReport(1L);
        assertTrue(getUserCompetitionReportResponse.isSuccess());
        assertEquals(getUserCompetitionReportResponse.getMessage(), "User competition report");
        assertEquals(getUserCompetitionReportResponse.getData().getUserCompetitionRecordList().size(), 1);
    }

    @Test
    public void testGetUserCompetitionReport_Fail() {
        when(playerCompetitionRepository.findAllByPlayerUserId(1L)).thenAnswer(invocation -> List.of());
        assertThrows(NotFoundException.class, () -> playerCompetitionService.getUserCompetitionReport(1L));
    }
}
