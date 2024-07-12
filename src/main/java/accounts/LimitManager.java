package accounts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class LimitManager {
    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;
    private final Map<LocalDate, BigDecimal> dailyUsage;
    private final Map<YearMonth, BigDecimal> monthlyUsage;

    public LimitManager(BigDecimal dailyLimit, BigDecimal monthlyLimit) {
        this.dailyLimit = dailyLimit;
        this.monthlyLimit = monthlyLimit;
        this.dailyUsage = new HashMap<>();
        this.monthlyUsage = new HashMap<>();
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        if (dailyLimit.compareTo(BigDecimal.ZERO) >= 0) this.dailyLimit = dailyLimit;
    }

    public BigDecimal getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        if (monthlyLimit.compareTo(BigDecimal.ZERO) >= 0) this.monthlyLimit = monthlyLimit;
    }

    public BigDecimal getDailyUsage(LocalDate date) {
        return dailyUsage.getOrDefault(date, BigDecimal.ZERO);
    }

    public BigDecimal getMonthlyUsage(YearMonth month) {
        return monthlyUsage.getOrDefault(month, BigDecimal.ZERO);
    }

    public boolean checkDailyLimit(BigDecimal amount) {
        return getDailyUsage(LocalDate.now()).add(amount).compareTo(dailyLimit) > 0;
    }

    public boolean checkMonthlyLimit(BigDecimal amount) {
        return getMonthlyUsage(YearMonth.now()).add(amount).compareTo(monthlyLimit) > 0;
    }

    public Map<LocalDate, BigDecimal> getDailyUsage() {
        return dailyUsage;
    }

    public void updateDailyUsage(LocalDate date, BigDecimal amount) {
        dailyUsage.put(date, getDailyUsage(date).add(amount));
    }

    public Map<YearMonth, BigDecimal> getMonthlyUsage() {
        return monthlyUsage;
    }

    public void updateMonthlyUsage(YearMonth month, BigDecimal amount) {
        monthlyUsage.put(month, getMonthlyUsage(month).add(amount));
    }

    public void resetDailyUsage() {
        dailyUsage.clear();
    }

    public void resetMonthlyUsage() {
        monthlyUsage.clear();
    }
}
