package com.hwr.hwrzeiterfassung.response.models;

import com.hwr.hwrzeiterfassung.database.tables.Project;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TimeCompact {
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean pause;
    private String note;
    private Project project;
}
