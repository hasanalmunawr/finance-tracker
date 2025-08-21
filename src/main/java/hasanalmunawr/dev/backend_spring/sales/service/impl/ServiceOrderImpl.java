package hasanalmunawr.dev.backend_spring.sales.service.impl;

import hasanalmunawr.dev.backend_spring.base.api.PaginationResponse;
import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.sales.api.request.ServiceOrderRequest;
import hasanalmunawr.dev.backend_spring.sales.api.response.ServiceOrderResponse;
import hasanalmunawr.dev.backend_spring.sales.model.ServiceOrder;
import hasanalmunawr.dev.backend_spring.sales.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceOrderImpl implements ServiceOrderService {

    private final GeneralRepository generalRepository;
    private final TaskProcessor taskProcessor;

    @Override
    public ResponseEntity<?> createServiceOrder(ServiceOrderRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            ServiceOrder serviceOrder = new ServiceOrder()
                    .setServiceId(generateServiceOrderId())
                    .setServiceDate(request.getServiceDate())
                    .setViaComplaint(request.getViaComplaint())
                    .setServiceBranch(request.getServiceBranch())
                    .setServiceCategory(request.getServiceCategory())

                    .setCustomerType(request.getCustomerType())
                    .setCustomerName(request.getCustomerName())
                    .setCustomerAddress(request.getCustomerAddress())
                    .setCustomerPhone(request.getCustomerPhone())
                    .setCustomerEmail(request.getCustomerEmail())
                    .setStoreName(request.getStoreName())

                    .setProductCategory(request.getProductCategory())
                    .setProductType(request.getProductType())
                    .setProductColor(request.getProductColor())
                    .setSerialNumber(request.getSerialNumber())
                    .setWarrantyCard(request.getWarrantyCard())
                    .setPurchaseOrder(request.getPurchaseOrder())
                    .setPurchaseDate(request.getPurchaseDate())
                    .setWarrantyStatus(request.getWarrantyStatus())
                    .setComplaint(request.getComplaint())

                    .setSymptom(request.getSymptom())
                    .setProductionPrefix(request.getProductionPrefix())
                    .setFailureCause(request.getFailureCause());

            generalRepository.getServiceOrderRepository().save(serviceOrder);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, ServiceOrderResponse.fromModel(serviceOrder));
        });
    }

    @Override
    public ResponseEntity<?> getAllServiceOrders(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            Page<ServiceOrder> serviceOrderData = getServiceOrders(page, size, sort, filter);

            List<ServiceOrderResponse> serviceOrderContent = serviceOrderData.getContent()
                    .stream()
                    .map(ServiceOrderResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(serviceOrderData.getTotalPages())
                    .setTotalData(serviceOrderData.getTotalElements())
                    .setCurrentPage(serviceOrderData.getNumber() + 1)
                    .setData(serviceOrderContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getServiceOrderById(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            ServiceOrder serviceOrder = generalRepository.getServiceOrderRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, ServiceOrderResponse.fromModel(serviceOrder));
        });
    }

    @Override
    public ResponseEntity<?> updateServiceOrder(Integer id, ServiceOrderRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            ServiceOrder serviceOrder = generalRepository.getServiceOrderRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            updateServiceOrderFromRequest(serviceOrder, request);
            generalRepository.getServiceOrderRepository().save(serviceOrder);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_UPDATED, null);
        });
    }

    @Override
    public ResponseEntity<?> deleteServiceOrder(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            Optional<ServiceOrder> serviceOrder = generalRepository.getServiceOrderRepository().findById(id);

            if (!serviceOrder.isPresent()) throw new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND);

            generalRepository.getServiceOrderRepository().deleteById(id);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_DELETED, null);
        });
    }

    private Page<ServiceOrder> getServiceOrders(int page, int size, String sort, String filter) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getServiceOrderRepository().searchServiceOrders(filter, pageable);
    }

    private void updateServiceOrderFromRequest(ServiceOrder serviceOrder, ServiceOrderRequest request) {
        serviceOrder.setServiceDate(request.getServiceDate());
        serviceOrder.setViaComplaint(request.getViaComplaint());
        serviceOrder.setServiceBranch(request.getServiceBranch());
        serviceOrder.setServiceCategory(request.getServiceCategory());

        serviceOrder.setCustomerType(request.getCustomerType());
        serviceOrder.setCustomerName(request.getCustomerName());
        serviceOrder.setCustomerAddress(request.getCustomerAddress());
        serviceOrder.setCustomerPhone(request.getCustomerPhone());
        serviceOrder.setCustomerEmail(request.getCustomerEmail());
        serviceOrder.setStoreName(request.getStoreName());

        serviceOrder.setProductCategory(request.getProductCategory());
        serviceOrder.setProductType(request.getProductType());
        serviceOrder.setProductColor(request.getProductColor());
        serviceOrder.setSerialNumber(request.getSerialNumber());
        serviceOrder.setWarrantyCard(request.getWarrantyCard());
        serviceOrder.setPurchaseOrder(request.getPurchaseOrder());
        serviceOrder.setPurchaseDate(request.getPurchaseDate());
        serviceOrder.setWarrantyStatus(request.getWarrantyStatus());
        serviceOrder.setComplaint(request.getComplaint());

        serviceOrder.setSymptom(request.getSymptom());
        serviceOrder.setProductionPrefix(request.getProductionPrefix());
        serviceOrder.setFailureCause(request.getFailureCause());
    }

    /*
     * Generate a unique service order number.
     *
     * Format:
     *   SO-YYYYMMDD-XXXX
     *
     * Breakdown:
     *   - SO: Prefix indicating "Service Order"
     *   - YYYYMMDD: Current date in year-month-day format (e.g., 20250513)
     *   - XXXX: A four-digit sequence number based on the number of service orders created today
     * Example Output:
     *   SO-20250513-0001
     *
     */
    private String generateServiceOrderId() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        long countToday = generalRepository.getServiceOrderRepository().countByCreatedAt(LocalDate.now()) + 1;

        // Format sequence number be 4 digit (0001, 0002, dst.)
        String sequence = String.format("%04d", countToday);

        // Merge Become SO Number
        return  "SO-" + today + "-" + sequence;
    }
}
