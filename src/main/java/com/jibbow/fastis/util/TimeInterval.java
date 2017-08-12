package com.jibbow.fastis.util;


import java.time.Duration;
import java.time.temporal.Temporal;

/**
 * Created by Jibbow on 8/12/17.
 */
public class TimeInterval {
    private Temporal startDateTime;
    private Temporal endDateTime;

    public TimeInterval(Temporal firstDate, Temporal lastDate) {
        this.startDateTime = firstDate;
        this.endDateTime = lastDate;
    }

    public static TimeInterval between(Temporal firstDate, Temporal lastDate) {
        return new TimeInterval(firstDate, lastDate);
    }

    public boolean overlaps(TimeInterval other) {
        return Duration.between(getEndDateTime(), other.getStartDateTime()).isNegative()
                && !Duration.between(getStartDateTime(), other.getEndDateTime()).isNegative();
    }

    public boolean startsBefore(TimeInterval other) {
        return !Duration.between(getStartDateTime(), other.getStartDateTime()).isNegative();
    }

    public boolean startsBefore(Temporal other) {
        return !Duration.between(startDateTime, other).isNegative();
    }

    public boolean endsAfter(TimeInterval other) {
        return Duration.between(getEndDateTime(), other.getEndDateTime()).isNegative();
    }

    public boolean endsAfter(Temporal other) {
        return Duration.between(endDateTime, other).isNegative();
    }

    public Duration getDuration() {
        return Duration.between(startDateTime, endDateTime);
    }

    public boolean containsTime(Temporal time) {
        return !Duration.between(startDateTime, time).isNegative()
                && Duration.between(endDateTime, time).isNegative();
    }

    public Temporal getStartDateTime() {
        return startDateTime;
    }

    public Temporal getEndDateTime() {
        return endDateTime;
    }
}
