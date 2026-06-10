package hu.tomi.team_leave_calendar.dto;

import java.time.LocalDate;

public class OnCallWeekResponse {

    private LocalDate weekStart;
    private LocalDate weekEnd;
    private TeamMemberResponse onCallPerson;
    private boolean hasConflict;
    private Long conflictingLeaveRequestId;
    private TeamMemberResponse suggestedReplacement;

    public OnCallWeekResponse(
            LocalDate weekStart,
            LocalDate weekEnd,
            TeamMemberResponse onCallPerson,
            boolean hasConflict,
            Long conflictingLeaveRequestId,
            TeamMemberResponse suggestedReplacement
    ) {
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.onCallPerson = onCallPerson;
        this.hasConflict = hasConflict;
        this.conflictingLeaveRequestId = conflictingLeaveRequestId;
        this.suggestedReplacement = suggestedReplacement;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public TeamMemberResponse getOnCallPerson() {
        return onCallPerson;
    }

    public boolean getHasConflict() {
        return hasConflict;
    }

    public Long getConflictingLeaveRequestId() {
        return conflictingLeaveRequestId;
    }

    public TeamMemberResponse getSuggestedReplacement() {
        return suggestedReplacement;
    }
}