package hasanalmunawr.dev.backend_spring.dashboard.service.impl;

import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.category.api.CategoryResponse;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.dashboard.api.ExpenseCategorySummary;
import hasanalmunawr.dev.backend_spring.dashboard.api.TransactionSummaryResponse;
import hasanalmunawr.dev.backend_spring.dashboard.service.DashboardService;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import hasanalmunawr.dev.backend_spring.web.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final GeneralRepository generalRepository;
    private final TaskProcessor taskProcessor;
    private final CurrentUserService currentUserService;

    @Override
    public ResponseEntity<?> getTransactionSummaryDashboard() {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            List<TransactionSummaryResponse> result = generalRepository.getDashboardRepository().getTransactionSummaryByUser(currentUser.getId());

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, result);
        });
    }

    @Override
    public ResponseEntity<?> getExpenseCategoryDashboard(Integer year, Integer month) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            List<ExpenseCategorySummary> result = generalRepository.getDashboardRepository().getExpenseSummaryByCategory(currentUser.getId(), year, month);
            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, result);
        });
    }

    @Override
    public ResponseEntity<?> getAvgSpending(Integer year, Integer month) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            List<BigDecimal> totals = generalRepository.getDashboardRepository().findMonthlyExpenseTotalsByUserAndYear(currentUser.getId(), year, month);
            if (totals.isEmpty()) {
                return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, 0.0);
            }

            BigDecimal sum = totals.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal avg = sum.divide(BigDecimal.valueOf(totals.size()), 2, RoundingMode.HALF_UP);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, avg);
        });
    }

    @Override
    public ResponseEntity<?> getTotalIncome(Integer year, Integer month) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();
            YearMonth current = YearMonth.of(year, month);
            YearMonth previous = current.minusMonths(1);

            // Ambil income bulan lalu
            List<BigDecimal> lastMonthlyIncome = generalRepository
                    .getDashboardRepository()
                    .getMonthlyIncome(currentUser.getId(), previous.getYear(), previous.getMonthValue());

            // Ambil income bulan ini
            List<BigDecimal> thisMonthlyIncome = generalRepository
                    .getDashboardRepository()
                    .getMonthlyIncome(currentUser.getId(), year, month);

            BigDecimal last = lastMonthlyIncome.isEmpty() ? BigDecimal.ZERO : lastMonthlyIncome.get(0);
            BigDecimal currentIncome = thisMonthlyIncome.isEmpty() ? BigDecimal.ZERO : thisMonthlyIncome.get(0);

            // Hitung persentase perubahan
            BigDecimal changePercent = BigDecimal.ZERO;
            if (last.compareTo(BigDecimal.ZERO) != 0) {
                changePercent = currentIncome.subtract(last)
                        .divide(last, 2, RoundingMode.HALF_UP) // 2 digit desimal
                        .multiply(BigDecimal.valueOf(100));
            }

            // Tentukan naik/turun
            boolean increase = currentIncome.compareTo(last) > 0;

            // Buat response
            Map<String, Object> result = new HashMap<>();
            result.put("income", currentIncome);
            result.put("percent", changePercent);
            result.put("increase", increase);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, result);
        });
    }

    @Override
    public ResponseEntity<?> getTotalExpense(Integer year, Integer month) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();
            YearMonth current = YearMonth.of(year, month);
            YearMonth previous = current.minusMonths(1);

            // Ambil expense bulan lalu
            List<BigDecimal> lastMonthlyExpense = generalRepository
                    .getDashboardRepository()
                    .getMonthlyExpense(currentUser.getId(), previous.getYear(), previous.getMonthValue());

            // Ambil expense bulan ini
            List<BigDecimal> thisMonthlyExpense = generalRepository
                    .getDashboardRepository()
                    .getMonthlyExpense(currentUser.getId(), year, month);

            BigDecimal last = lastMonthlyExpense.isEmpty() ? BigDecimal.ZERO : lastMonthlyExpense.get(0);
            BigDecimal currentExpense = thisMonthlyExpense.isEmpty() ? BigDecimal.ZERO : thisMonthlyExpense.get(0);

            // Hitung persentase perubahan
            BigDecimal changePercent = BigDecimal.ZERO;
            if (last.compareTo(BigDecimal.ZERO) != 0) {
                changePercent = currentExpense.subtract(last)
                        .divide(last, 2, RoundingMode.HALF_UP) // 2 digit desimal
                        .multiply(BigDecimal.valueOf(100));
            }

            // Tentukan naik/turun
            boolean increase = currentExpense.compareTo(last) > 0;

            // Buat response
            Map<String, Object> result = new HashMap<>();
            result.put("expense", currentExpense);
            result.put("percent", changePercent);
            result.put("increase", increase);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, result);
        });
    }

    @Override
    public ResponseEntity<?> getCashFlow(Integer year, Integer month) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            // Ambil income bulan ini (exclude mutation di query repository)
            List<BigDecimal> thisMonthlyIncome = generalRepository
                    .getDashboardRepository()
                    .getMonthlyIncome(currentUser.getId(), year, month);

            // Ambil expense bulan ini
            List<BigDecimal> thisMonthlyExpense = generalRepository
                    .getDashboardRepository()
                    .getMonthlyExpense(currentUser.getId(), year, month);

            // Hitung total income & expense
            BigDecimal totalIncome = thisMonthlyIncome.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpense = thisMonthlyExpense.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Cash flow = income - expense
            BigDecimal cashFlow = totalIncome.subtract(totalExpense);

            // Hitung persentase cash flow terhadap income
            BigDecimal percent = BigDecimal.ZERO;
            if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
                percent = cashFlow
                        .divide(totalIncome, 2, RoundingMode.HALF_UP) // 2 digit decimal
                        .multiply(BigDecimal.valueOf(100)); // dalam persen
            }

            // Buat response
            Map<String, Object> result = new HashMap<>();
            result.put("cash_flow", cashFlow);
            result.put("percent", percent);
            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, result);
        });
    }

}
