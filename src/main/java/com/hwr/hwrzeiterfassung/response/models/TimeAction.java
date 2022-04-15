package com.hwr.hwrzeiterfassung.response.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Time action.
 */
@AllArgsConstructor
@Data
public class TimeAction {
    private boolean isStart;
    private boolean isBreak;
    private int projectId;
}
