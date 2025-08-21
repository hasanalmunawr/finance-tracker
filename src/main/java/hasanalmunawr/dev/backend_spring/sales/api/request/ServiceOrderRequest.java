package hasanalmunawr.dev.backend_spring.sales.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ServiceOrderRequest {

    @Schema(
            description = "Service Date",
            example = "2025-04-25",
            required = true
    )
    @NotNull(message = "Service date must not be null")
    private LocalDate serviceDate;

    @Schema(
            description = "Via Complaint",
            example = "Call Center",
            required = true
    )
    @NotBlank(message = "Via complaint must not be blank")
    private String viaComplaint;

    @NotBlank(message = "Service branch must not be blank")
    private String serviceBranch;

    @NotBlank(message = "Service category must not be blank")
    private String serviceCategory;

    @NotBlank(message = "Customer type must not be blank")
    private String customerType;

    @NotBlank(message = "Customer name must not be blank")
    private String customerName;

    @NotBlank(message = "Customer address must not be blank")
    private String customerAddress;

    @NotBlank(message = "Customer phone must not be blank")
    private String customerPhone;

    @Email(message = "Customer email must be a valid email format")
    private String customerEmail;

    private String storeName; // Optional

    @NotBlank(message = "Product category must not be blank")
    private String productCategory;

    @NotBlank(message = "Product type must not be blank")
    private String productType;

    private String productColor; // Optional

    @NotBlank(message = "Serial number must not be blank")
    private String serialNumber;

    private String warrantyCard; // Optional

    private String purchaseOrder; // Optional

    private String purchaseDate; // Optional, use @Pattern if format is strict

    @NotBlank(message = "Warranty status must not be blank")
    private String warrantyStatus;

    @NotBlank(message = "Complaint must not be blank")
    private String complaint;

    private String symptom; // Optional

    private String productionPrefix; // Optional

    private String failureCause; // Optional

}
