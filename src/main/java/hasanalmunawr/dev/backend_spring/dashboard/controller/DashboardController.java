package hasanalmunawr.dev.backend_spring.dashboard.controller;

import hasanalmunawr.dev.backend_spring.base.constants.Endpoint;
import hasanalmunawr.dev.backend_spring.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoint.Base.DASHBOARD)
@Tag(name = "Dashboard Controller", description = "API for managing dashboard related operations.")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary-transaction")
    public ResponseEntity<?> getSummaryTransaction(
    ) {
        return dashboardService.getTransactionSummaryDashboard();
    }

    @GetMapping("summary-category")
    public ResponseEntity<?> getExpenseCategory(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return dashboardService.getExpenseCategoryDashboard(year, month);
    }

    @GetMapping("/avg-spending")
    public ResponseEntity<?> getAvgSpending(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return dashboardService.getAvgSpending(year, month);
    }

    @GetMapping("/total-income")
    public ResponseEntity<?> getTotalIncome(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return dashboardService.getTotalIncome(year, month);
    }

    @GetMapping("/total-expense")
    public ResponseEntity<?> getTotalExpense(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return dashboardService.getTotalExpense(year, month);
    }

@GetMapping("/cash-flow")
    public ResponseEntity<?> getCashFlow(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return dashboardService.getCashFlow(year, month);
    }




}
