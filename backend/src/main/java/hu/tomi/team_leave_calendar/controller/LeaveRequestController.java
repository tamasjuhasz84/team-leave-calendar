package hu.tomi.team_leave_calendar.controller;

import hu.tomi.team_leave_calendar.dto.CreateLeaveRequestRequest;
import hu.tomi.team_leave_calendar.dto.LeaveRequestResponse;
import hu.tomi.team_leave_calendar.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import hu.tomi.team_leave_calendar.dto.UpdateLeaveStatusRequest;
import hu.tomi.team_leave_calendar.model.LeaveStatus;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @GetMapping
    public List<LeaveRequestResponse> getLeaveRequests(
            @RequestParam(required = false) Long teamMemberId,
            @RequestParam(required = false) LeaveStatus status
    ) {
        return leaveRequestService.getLeaveRequests(teamMemberId, status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveRequestResponse createLeaveRequest(
            @Valid @RequestBody CreateLeaveRequestRequest request
    ) {
        return leaveRequestService.createLeaveRequest(request);
    }

    @PatchMapping("/{id}/status")
    public LeaveRequestResponse updateLeaveRequestStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLeaveStatusRequest request
    ) {
        return leaveRequestService.updateLeaveRequestStatus(id, request);
    }
}