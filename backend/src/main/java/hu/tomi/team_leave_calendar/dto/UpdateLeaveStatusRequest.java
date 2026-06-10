package hu.tomi.team_leave_calendar.dto;

import hu.tomi.team_leave_calendar.model.LeaveStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateLeaveStatusRequest {

    @NotNull
    private LeaveStatus status;

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
}