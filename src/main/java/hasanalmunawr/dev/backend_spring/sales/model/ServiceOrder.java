package hasanalmunawr.dev.backend_spring.sales.model;

import hasanalmunawr.dev.backend_spring.base.model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Table
@Entity
public class ServiceOrder extends BaseModel {

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



}
