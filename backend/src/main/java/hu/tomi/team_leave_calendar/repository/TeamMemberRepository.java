package hu.tomi.team_leave_calendar.repository;

import hu.tomi.team_leave_calendar.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}