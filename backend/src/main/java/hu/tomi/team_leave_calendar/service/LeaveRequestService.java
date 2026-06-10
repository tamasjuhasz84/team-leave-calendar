package hu.tomi.team_leave_calendar.service;

import hu.tomi.team_leave_calendar.dto.CreateLeaveRequestRequest;
import hu.tomi.team_leave_calendar.dto.LeaveRequestResponse;
import hu.tomi.team_leave_calendar.dto.TeamMemberResponse;
import hu.tomi.team_leave_calendar.model.LeaveRequest;
import hu.tomi.team_leave_calendar.model.LeaveStatus;
import hu.tomi.team_leave_calendar.model.TeamMember;
import hu.tomi.team_leave_calendar.repository.LeaveRequestRepository;
import hu.tomi.team_leave_calendar.repository.TeamMemberRepository;
import hu.tomi.team_leave_calendar.exception.BusinessRuleViolationException;
import hu.tomi.team_leave_calendar.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import hu.tomi.team_leave_calendar.dto.UpdateLeaveStatusRequest;

import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final TeamMemberRepository teamMemberRepository;

    public LeaveRequestService(
            LeaveRequestRepository leaveRequestRepository,
            TeamMemberRepository teamMemberRepository
    ) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    public List<LeaveRequestResponse> getLeaveRequests(Long teamMemberId, LeaveStatus status) {
        List<LeaveRequest> leaveRequests;

        if (teamMemberId != null && status != null) {
            leaveRequests = leaveRequestRepository.findByTeamMemberIdAndStatus(teamMemberId, status);
        } else if (teamMemberId != null) {
            leaveRequests = leaveRequestRepository.findByTeamMemberId(teamMemberId);
        } else if (status != null) {
            leaveRequests = leaveRequestRepository.findByStatus(status);
        } else {
            leaveRequests = leaveRequestRepository.findAll();
        }

        return leaveRequests
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LeaveRequestResponse createLeaveRequest(CreateLeaveRequestRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessRuleViolationException("Start date cannot be after end date.");
        }

        TeamMember teamMember = teamMemberRepository.findById(request.getTeamMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found."));

        boolean hasOverlap = !leaveRequestRepository
                .findByTeamMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        request.getTeamMemberId(),
                        request.getEndDate(),
                        request.getStartDate()
                )
                .isEmpty();

        if (hasOverlap) {
            throw new BusinessRuleViolationException("Leave request overlaps with an existing leave request.");
        }

        LeaveRequest leaveRequest = new LeaveRequest(
                teamMember,
                request.getStartDate(),
                request.getEndDate(),
                request.getReason(),
                LeaveStatus.PENDING,
                request.getComments()
        );

        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        return mapToResponse(savedLeaveRequest);
    }

    private LeaveRequestResponse mapToResponse(LeaveRequest leaveRequest) {
        TeamMember teamMember = leaveRequest.getTeamMember();

        return new LeaveRequestResponse(
                leaveRequest.getId(),
                new TeamMemberResponse(
                        teamMember.getId(),
                        teamMember.getName()
                ),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getReason(),
                leaveRequest.getStatus(),
                leaveRequest.getComments()
        );
    }

    public LeaveRequestResponse updateLeaveRequestStatus(Long id, UpdateLeaveStatusRequest request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found."));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessRuleViolationException(
                    "Only pending leave requests can be updated."
            );
        }

        leaveRequest.setStatus(request.getStatus());

        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        return mapToResponse(savedLeaveRequest);
    }
}