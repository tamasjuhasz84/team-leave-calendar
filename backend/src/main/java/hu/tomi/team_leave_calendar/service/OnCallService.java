package hu.tomi.team_leave_calendar.service;

import hu.tomi.team_leave_calendar.dto.OnCallWeekResponse;
import hu.tomi.team_leave_calendar.dto.TeamMemberResponse;
import hu.tomi.team_leave_calendar.model.LeaveRequest;
import hu.tomi.team_leave_calendar.model.LeaveStatus;
import hu.tomi.team_leave_calendar.model.TeamMember;
import hu.tomi.team_leave_calendar.repository.LeaveRequestRepository;
import hu.tomi.team_leave_calendar.repository.TeamMemberRepository;
import hu.tomi.team_leave_calendar.exception.BusinessRuleViolationException;
import hu.tomi.team_leave_calendar.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class OnCallService {

    private final TeamMemberRepository teamMemberRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public OnCallService(
            TeamMemberRepository teamMemberRepository,
            LeaveRequestRepository leaveRequestRepository
    ) {
        this.teamMemberRepository = teamMemberRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public List<OnCallWeekResponse> getOnCallSchedule(int weeks) {
        if (weeks < 1 || weeks > 52) {
            throw new BusinessRuleViolationException("Weeks must be between 1 and 52.");
        }
        List<TeamMember> teamMembers = teamMemberRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(TeamMember::getId))
                .toList();

        if (teamMembers.isEmpty()) {
            throw new ResourceNotFoundException("No team members found.");
        }

        LocalDate currentMonday = LocalDate.now()
                .with(DayOfWeek.MONDAY);

        return java.util.stream.IntStream.range(0, weeks)
                .mapToObj(index -> createOnCallWeekResponse(index, currentMonday, teamMembers))
                .toList();
    }

    private OnCallWeekResponse createOnCallWeekResponse(
            int index,
            LocalDate currentMonday,
            List<TeamMember> teamMembers
    ) {
        LocalDate weekStart = currentMonday.plusWeeks(index);
        LocalDate weekEnd = weekStart.plusDays(6);

        TeamMember onCallPerson = teamMembers.get(index % teamMembers.size());

        List<LeaveRequest> conflicts = findApprovedLeavesForWeek(
                onCallPerson.getId(),
                weekStart,
                weekEnd
        );

        LeaveRequest conflict = conflicts.isEmpty() ? null : conflicts.get(0);

        TeamMemberResponse suggestedReplacement = null;

        if (conflict != null) {
            suggestedReplacement = findSuggestedReplacement(
                    onCallPerson.getId(),
                    teamMembers,
                    weekStart,
                    weekEnd
            );
        }

        return new OnCallWeekResponse(
                weekStart,
                weekEnd,
                new TeamMemberResponse(
                        onCallPerson.getId(),
                        onCallPerson.getName()
                ),
                conflict != null,
                conflict != null ? conflict.getId() : null,
                suggestedReplacement
        );
    }

    private List<LeaveRequest> findApprovedLeavesForWeek(
            Long teamMemberId,
            LocalDate weekStart,
            LocalDate weekEnd
    ) {
        return leaveRequestRepository
                .findByTeamMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        teamMemberId,
                        LeaveStatus.APPROVED,
                        weekEnd,
                        weekStart
                );
    }

    private TeamMemberResponse findSuggestedReplacement(
            Long originalOnCallPersonId,
            List<TeamMember> teamMembers,
            LocalDate weekStart,
            LocalDate weekEnd
    ) {
        return teamMembers
                .stream()
                .filter(member -> !member.getId().equals(originalOnCallPersonId))
                .filter(member -> findApprovedLeavesForWeek(
                        member.getId(),
                        weekStart,
                        weekEnd
                ).isEmpty())
                .findFirst()
                .map(member -> new TeamMemberResponse(
                        member.getId(),
                        member.getName()
                ))
                .orElse(null);
    }
}