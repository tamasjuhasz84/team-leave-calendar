package hu.tomi.team_leave_calendar.service;

import hu.tomi.team_leave_calendar.dto.OnCallWeekResponse;
import hu.tomi.team_leave_calendar.model.LeaveRequest;
import hu.tomi.team_leave_calendar.model.LeaveStatus;
import hu.tomi.team_leave_calendar.model.TeamMember;
import hu.tomi.team_leave_calendar.repository.LeaveRequestRepository;
import hu.tomi.team_leave_calendar.repository.TeamMemberRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OnCallServiceTest {

    private final TeamMemberRepository teamMemberRepository = mock(TeamMemberRepository.class);
    private final LeaveRequestRepository leaveRequestRepository = mock(LeaveRequestRepository.class);

    private final OnCallService onCallService = new OnCallService(
            teamMemberRepository,
            leaveRequestRepository
    );

    @Test
    void onCallScheduleShouldSuggestReplacementWhenConflictExists() {
        TeamMember alice = new TeamMember(1L, "Alice");
        TeamMember bob = new TeamMember(2L, "Bob");
        TeamMember charlie = new TeamMember(3L, "Charlie");
        TeamMember diana = new TeamMember(4L, "Diana");

        when(teamMemberRepository.findAll())
                .thenReturn(List.of(alice, bob, charlie, diana));

        LeaveRequest bobLeave = new LeaveRequest(
                bob,
                LocalDate.now().with(java.time.DayOfWeek.MONDAY),
                LocalDate.now().with(java.time.DayOfWeek.MONDAY).plusDays(2),
                "Conference",
                LeaveStatus.APPROVED,
                null
        );

        when(leaveRequestRepository
                .findByTeamMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        eq(1L),
                        eq(LeaveStatus.APPROVED),
                        any(LocalDate.class),
                        any(LocalDate.class)
                ))
                .thenReturn(List.of());

        when(leaveRequestRepository
                .findByTeamMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        eq(2L),
                        eq(LeaveStatus.APPROVED),
                        any(LocalDate.class),
                        any(LocalDate.class)
                ))
                .thenReturn(List.of(bobLeave));

        when(leaveRequestRepository
                .findByTeamMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        eq(3L),
                        eq(LeaveStatus.APPROVED),
                        any(LocalDate.class),
                        any(LocalDate.class)
                ))
                .thenReturn(List.of());

        List<OnCallWeekResponse> schedule = onCallService.getOnCallSchedule(2);

        OnCallWeekResponse secondWeek = schedule.get(1);

        assertTrue(secondWeek.getHasConflict());
        assertEquals("Bob", secondWeek.getOnCallPerson().getName());
        assertNotNull(secondWeek.getSuggestedReplacement());
        assertEquals("Alice", secondWeek.getSuggestedReplacement().getName());
    }
}