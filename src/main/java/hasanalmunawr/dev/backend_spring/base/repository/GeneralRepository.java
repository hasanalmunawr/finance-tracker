package hasanalmunawr.dev.backend_spring.base.repository;


import hasanalmunawr.dev.backend_spring.bankAccount.repository.BankAccountRepository;
import hasanalmunawr.dev.backend_spring.budget.repository.BudgetHeaderRepository;
import hasanalmunawr.dev.backend_spring.budget.repository.BudgetRepository;
import hasanalmunawr.dev.backend_spring.category.repository.CategoryRepository;
import hasanalmunawr.dev.backend_spring.dashboard.repository.DashboardRepository;
import hasanalmunawr.dev.backend_spring.debts.repository.DebtRepository;
import hasanalmunawr.dev.backend_spring.transaction.repository.TransactionRepository;
import hasanalmunawr.dev.backend_spring.user.repository.CodeVerifyRepository;
import hasanalmunawr.dev.backend_spring.user.repository.PasswordResetTokenRepository;
import hasanalmunawr.dev.backend_spring.user.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GeneralRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetHeaderRepository budgetHeaderRepository;

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private CodeVerifyRepository codeVerifyRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

}
