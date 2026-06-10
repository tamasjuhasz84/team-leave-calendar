package hu.tomi.team_leave_calendar.repository;

import hu.tomi.team_leave_calendar.model.LeaveRequest;
import hu.tomi.team_leave_calendar.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByTeamMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long teamMemberId,
            LocalDate endDate,
            LocalDate startDate
    );

    List<LeaveRequest> findByTeamMemberIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long teamMemberId,
            LeaveStatus status,
            LocalDate weekEnd,
            LocalDate weekStart
    );

    List<LeaveRequest> findByTeamMemberId(Long teamMemberId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByTeamMemberIdAndStatus(Long teamMemberId, LeaveStatus status);
}