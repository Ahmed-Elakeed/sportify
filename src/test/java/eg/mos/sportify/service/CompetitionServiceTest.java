package eg.mos.sportify.service;


import eg.mos.sportify.domain.AuditData;
import eg.mos.sportify.domain.Competition;
import eg.mos.sportify.domain.User;
import eg.mos.sportify.domain.enums.CompetitionStatus;
import eg.mos.sportify.dto.ApiResponse;
import eg.mos.sportify.dto.competition.CompetitionChangeStatusDTO;
import eg.mos.sportify.dto.competition.CompetitionRequestDTO;
import eg.mos.sportify.dto.competition.CompetitionResponseDTO;
import eg.mos.sportify.event.CompetitionListener;
import eg.mos.sportify.exception.AuthorizationException;
import eg.mos.sportify.exception.NotFoundException;
import eg.mos.sportify.repository.CompetitionRepository;
import eg.mos.sportify.repository.UserRepository;
import eg.mos.sportify.security.AuthUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CompetitionServiceTest {

    @MockBean
    private CompetitionRepository competitionRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CompetitionListener competitionListener;

    @Autowired
    private CompetitionService competitionService;


    @Test
    public void testAddCompetition_Success() {
        User user = User.builder().username("test").userId(1L).build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

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
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);
        doNothing().when(competitionListener).addCompetitionPostProcess(any());

        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getUsernameFromToken)
                    .thenReturn(user.getUsername());


            ApiResponse<CompetitionResponseDTO> addCompetitionResponse = competitionService.addCompetition(
                    CompetitionRequestDTO.builder()
                            .name("test")
                            .status(CompetitionStatus.UPCOMING)
                            .startDate(LocalDateTime.now())
                            .maxScore(100)
                            .endDate(LocalDateTime.now())
                            .description("test")
                            .build()
            );

            assertEquals(addCompetitionResponse.getData().getName(), "test");
            assertTrue(addCompetitionResponse.isSuccess());


        }
    }

    @Test
    public void testAddCompetition_Fail() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        assertThrows(AuthorizationException.class, () -> competitionService.addCompetition(CompetitionRequestDTO.builder().build()));
    }

    @Test
    public void testChangeCompetitionStatus_Success() {
        User user = User.builder().username("test").userId(1L).build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        Competition competition = Competition.builder()
                .competitionId(1L)
                .name("test")
                .admin(user)
                .status(CompetitionStatus.UPCOMING)
                .startDate(LocalDateTime.now())
                .maxScore(100)
                .endDate(LocalDateTime.now())
                .description("test")
                .auditData(AuditData.builder().createdAt(LocalDateTime.now()).build())
                .build();
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));
        competition.setStatus(CompetitionStatus.CANCELLED);
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        try (MockedStatic<AuthUserDetailsService> mockedStatic = mockStatic(AuthUserDetailsService.class)) {
            mockedStatic.when(AuthUserDetailsService::getUsernameFromToken)
                    .thenReturn(user.getUsername());

            ApiResponse<CompetitionResponseDTO> changeCompetitionResponse = competitionService.changeCompetitionStatus(
                    CompetitionChangeStatusDTO.builder()
                            .competitionId(competition.getCompetitionId())
                            .status(CompetitionStatus.CANCELLED)
                            .build()
            );

            assertTrue(changeCompetitionResponse.isSuccess());
            assertEquals(changeCompetitionResponse.getData().getStatus(), CompetitionStatus.CANCELLED);

        }
    }

    @Test
    public void testChangeCompetitionStatus_Fail(){
        when(competitionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()->competitionService.changeCompetitionStatus(CompetitionChangeStatusDTO.builder().competitionId(1L).build()));
    }

}
