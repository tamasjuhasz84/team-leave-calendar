package hu.tomi.team_leave_calendar.config;

import hu.tomi.team_leave_calendar.model.TeamMember;
import hu.tomi.team_leave_calendar.repository.TeamMemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TeamMemberRepository teamMemberRepository;

    public DataInitializer(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    @Override
    public void run(String... args) {
        if (teamMemberRepository.count() > 0) {
            return;
        }

        List<TeamMember> teamMembers = List.of(
                new TeamMember("Alice"),
                new TeamMember("Bob"),
                new TeamMember("Charlie"),
                new TeamMember("Diana")
        );

        teamMemberRepository.saveAll(teamMembers);
    }
}