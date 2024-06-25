package transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

public class TransactionDateRanges {
    public static LocalDateTime[] getDayRange(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusSeconds(1);
        return new LocalDateTime[]{startOfDay, endOfDay};
    }

    public static LocalDateTime[] getWeekRange(LocalDate date) {
        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);
        LocalDateTime startOfWeekTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekTime = endOfWeek.plusDays(1).atStartOfDay().minusSeconds(1);
        return new LocalDateTime[]{startOfWeekTime, endOfWeekTime};
    }

    public static LocalDateTime[] getMonthRange(LocalDate date) {
        LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime startOfMonthTime = startOfMonth.atStartOfDay();
        LocalDateTime endOfMonthTime = endOfMonth.plusDays(1).atStartOfDay().minusSeconds(1);
        return new LocalDateTime[]{startOfMonthTime, endOfMonthTime};
    }

    public static LocalDateTime[] getYearRange(LocalDate date) {
        LocalDate startOfYear = date.with(TemporalAdjusters.firstDayOfYear());
        LocalDate endOfYear = date.with(TemporalAdjusters.lastDayOfYear());
        LocalDateTime startOfYearTime = startOfYear.atStartOfDay();
        LocalDateTime endOfYearTime = endOfYear.plusDays(1).atStartOfDay().minusSeconds(1);
        return new LocalDateTime[]{startOfYearTime, endOfYearTime};
    }
}

