package hu.tomi.team_leave_calendar.dto;

public class TeamMemberResponse {

    private Long id;
    private String name;

    public TeamMemberResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}