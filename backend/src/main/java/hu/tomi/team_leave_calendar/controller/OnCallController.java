package hu.tomi.team_leave_calendar.controller;

import hu.tomi.team_leave_calendar.dto.OnCallWeekResponse;
import hu.tomi.team_leave_calendar.service.OnCallService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/on-call")
public class OnCallController {

    private final OnCallService onCallService;

    public OnCallController(OnCallService onCallService) {
        this.onCallService = onCallService;
    }

    @GetMapping
    public List<OnCallWeekResponse> getOnCallSchedule(
            @RequestParam(defaultValue = "8") int weeks
    ) {
        return onCallService.getOnCallSchedule(weeks);
    }
}