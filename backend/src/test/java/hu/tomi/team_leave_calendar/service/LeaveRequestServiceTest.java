package hu.tomi.team_leave_calendar.service;

import hu.tomi.team_leave_calendar.dto.CreateLeaveRequestRequest;
import hu.tomi.team_leave_calendar.dto.LeaveRequestResponse;
import hu.tomi.team_leave_calendar.model.LeaveRequest;
import hu.tomi.team_leave_calendar.model.LeaveStatus;
import hu.tomi.team_leave_calendar.model.TeamMember;
import hu.tomi.team_leave_calendar.repository.LeaveRequestRepository;
import hu.tomi.team_leave_calendar.repository.TeamMemberRepository;
import hu.tomi.team_leave_calendar.dto.UpdateLeaveStatusRequest;
import hu.tomi.team_leave_calendar.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaveRequestServiceTest {

    private final LeaveRequestRepository leaveRequestRepository = mock(LeaveRequestRepository.class);
    private final TeamMemberRepository teamMemberRepository = mock(TeamMemberRepository.class);

    private final LeaveRequestService leaveRequestService = new LeaveRequestService(
            leaveRequestRepository,
            teamMemberRepository
    );

    @Test
    void createLeaveRequestShouldCreatePendingLeaveRequest() {

        TeamMember alice = new TeamMember(1L, "Alice");

        CreateLeaveRequestRequest request = new CreateLeaveRequestRequest();
        request.setTeamMemberId(1L);
        request.setStartDate(LocalDate.of(2026, 6, 15));
        request.setEndDate(LocalDate.of(2026, 6, 20));
        request.setReason("Holiday");
        request.setComments("Summer trip");

        when(teamMemberRepository.findById(1L))
                .thenReturn(Optional.of(alice));

        when(leaveRequestRepository
                .findByTeamMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        anyLong(),
                        any(),
                        any()
                ))
                .thenReturn(List.of());

        LeaveRequest savedLeaveRequest = new LeaveRequest(
                alice,
                request.getStartDate(),
                request.getEndDate(),
                request.getReason(),
                LeaveStatus.PENDING,
                request.getComments()
        );

        when(leaveRequestRepository.save(any(LeaveRequest.class)))
                .thenReturn(savedLeaveRequest);

        LeaveRequestResponse response =
                leaveRequestService.createLeaveRequest(request);

        assertNotNull(response);

        assertEquals("Alice",
                response.getTeamMember().getName());

        assertEquals(LeaveStatus.PENDING,
                response.getStatus());

        assertEquals("Summer trip",
                response.getComments());

        verify(leaveRequestRepository)
                .save(any(LeaveRequest.class));
    }

    @Test
    void createLeaveRequestShouldThrowExceptionWhenDatesOverlap() {
        TeamMember alice = new TeamMember(1L, "Alice");

        CreateLeaveRequestRequest request = new CreateLeaveRequestRequest();
        request.setTeamMemberId(1L);
        request.setStartDate(LocalDate.of(2026, 6, 15));
        request.setEndDate(LocalDate.of(2026, 6, 20));
        request.setReason("Holiday");

        LeaveRequest existingLeaveRequest = new LeaveRequest(
                alice,
                LocalDate.of(2026, 6, 18),
                LocalDate.of(2026, 6, 22),
                "Existing holiday",
                LeaveStatus.APPROVED,
                null
        );

        when(teamMemberRepository.findById(1L))
                .thenReturn(Optional.of(alice));

        when(leaveRequestRepository
                .findByTeamMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        1L,
                        request.getEndDate(),
                        request.getStartDate()
                ))
                .thenReturn(List.of(existingLeaveRequest));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> leaveRequestService.createLeaveRequest(request)
        );

        assertEquals(
                "Leave request overlaps with an existing leave request.",
                exception.getMessage()
        );

        verify(leaveRequestRepository, never())
                .save(any(LeaveRequest.class));
    }

    @Test
    void updateLeaveRequestStatusShouldUpdateStatus() {
        TeamMember bob = new TeamMember(2L, "Bob");

        LeaveRequest leaveRequest = new LeaveRequest(
                bob,
                LocalDate.of(2026, 6, 17),
                LocalDate.of(2026, 6, 20),
                "Conference",
                LeaveStatus.PENDING,
                null
        );

        UpdateLeaveStatusRequest request = new UpdateLeaveStatusRequest();
        request.setStatus(LeaveStatus.APPROVED);

        when(leaveRequestRepository.findById(1L))
                .thenReturn(Optional.of(leaveRequest));

        when(leaveRequestRepository.save(any(LeaveRequest.class)))
                .thenReturn(leaveRequest);

        LeaveRequestResponse response =
                leaveRequestService.updateLeaveRequestStatus(1L, request);

        assertEquals(LeaveStatus.APPROVED, response.getStatus());

        verify(leaveRequestRepository)
                .save(leaveRequest);
    }
}