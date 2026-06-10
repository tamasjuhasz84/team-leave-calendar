package hu.tomi.team_leave_calendar.service;

import hu.tomi.team_leave_calendar.dto.TeamMemberResponse;
import hu.tomi.team_leave_calendar.model.TeamMember;
import hu.tomi.team_leave_calendar.repository.TeamMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    public TeamMemberService(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    public List<TeamMemberResponse> getAllTeamMembers() {
        return teamMemberRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TeamMemberResponse mapToResponse(TeamMember teamMember) {
        return new TeamMemberResponse(
                teamMember.getId(),
                teamMember.getName()
        );
    }
}