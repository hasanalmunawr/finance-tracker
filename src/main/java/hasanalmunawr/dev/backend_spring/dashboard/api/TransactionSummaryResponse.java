package hasanalmunawr.dev.backend_spring.dashboard.api;

public interface TransactionSummaryResponse {

    Integer getMonth();
    Integer getYear();
    String getType();
    Double getTotalAmount();
    Long getTotalTransactions();

}
