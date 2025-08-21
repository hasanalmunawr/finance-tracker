package hasanalmunawr.dev.backend_spring.sales.api.response;

import hasanalmunawr.dev.backend_spring.sales.model.ServiceOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ServiceOrderResponse {

    private Integer id;

    private String serviceId;
    private LocalDate serviceDate;
    private String viaComplaint;
    private String serviceBranch;
    private String serviceCategory;

    private String customerType;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerEmail;
    private String storeName;

    private String productCategory;
    private String productType;
    private String productColor;
    private String serialNumber;
    private String warrantyCard;
    private String purchaseOrder;
    private String purchaseDate;
    private String warrantyStatus;
    private String complaint;

    private String symptom;
    private String productionPrefix;
    private String failureCause;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ServiceOrderResponse fromModel(ServiceOrder order) {
        return new ServiceOrderResponse()
                .setId(order.getId())
                .setServiceId(order.getServiceId())
                .setServiceDate(order.getServiceDate())
                .setViaComplaint(order.getViaComplaint())
                .setServiceBranch(order.getServiceBranch())
                .setServiceCategory(order.getServiceCategory())

                .setCustomerType(order.getCustomerType())
                .setCustomerName(order.getCustomerName())
                .setCustomerAddress(order.getCustomerAddress())
                .setCustomerPhone(order.getCustomerPhone())
                .setCustomerEmail(order.getCustomerEmail())
                .setStoreName(order.getStoreName())

                .setProductCategory(order.getProductCategory())
                .setProductType(order.getProductType())
                .setProductColor(order.getProductColor())
                .setSerialNumber(order.getSerialNumber())
                .setWarrantyCard(order.getWarrantyCard())
                .setPurchaseOrder(order.getPurchaseOrder())
                .setPurchaseDate(order.getPurchaseDate())
                .setWarrantyStatus(order.getWarrantyStatus())
                .setComplaint(order.getComplaint())

                .setSymptom(order.getSymptom())
                .setProductionPrefix(order.getProductionPrefix())
                .setFailureCause(order.getFailureCause())

                .setCreatedAt(order.getCreatedAt())
                .setUpdatedAt(order.getUpdatedAt());
    }

}
