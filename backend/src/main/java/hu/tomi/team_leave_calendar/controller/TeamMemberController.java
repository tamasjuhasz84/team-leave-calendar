package hu.tomi.team_leave_calendar.controller;

import hu.tomi.team_leave_calendar.dto.TeamMemberResponse;
import hu.tomi.team_leave_calendar.service.TeamMemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/team-members")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    public TeamMemberController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @GetMapping
    public List<TeamMemberResponse> getAllTeamMembers() {
        return teamMemberService.getAllTeamMembers();
    }
}