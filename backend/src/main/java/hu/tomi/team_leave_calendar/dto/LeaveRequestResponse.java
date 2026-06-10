package hu.tomi.team_leave_calendar.dto;

import hu.tomi.team_leave_calendar.model.LeaveStatus;

import java.time.LocalDate;

public class LeaveRequestResponse {

    private Long id;
    private TeamMemberResponse teamMember;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
    private String comments;

    public LeaveRequestResponse(
            Long id,
            TeamMemberResponse teamMember,
            LocalDate startDate,
            LocalDate endDate,
            String reason,
            LeaveStatus status,
            String comments
    ) {
        this.id = id;
        this.teamMember = teamMember;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public TeamMemberResponse getTeamMember() {
        return teamMember;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public String getComments() {
        return comments;
    }
}