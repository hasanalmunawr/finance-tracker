package hasanalmunawr.dev.backend_spring.sales.controller;

import hasanalmunawr.dev.backend_spring.base.constants.Endpoint;
import hasanalmunawr.dev.backend_spring.sales.api.request.ServiceOrderRequest;
import hasanalmunawr.dev.backend_spring.sales.service.ServiceOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hasanalmunawr.dev.backend_spring.sales.api.swagger.ServiceOrderExample.*;

@RestController
@RequestMapping(Endpoint.Base.SERVICE_ORDER)
@Tag(name = "Service Order Controller", description = "API for managing service-order-related operations.")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @GetMapping()
    public ResponseEntity<?> getAllServiceOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return serviceOrderService.getAllServiceOrders(page, size, sort, filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceOrderById(@PathVariable Integer id) {
        return serviceOrderService.getServiceOrderById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service Order Response Success",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = SERVICE_ORDER_CREATE_RESPONSE_SUCCESS))),
    })
    @PostMapping(Endpoint.Basic.CREATE)
    public ResponseEntity<?> createServiceOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Service Order Request",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ServiceOrderRequest.class),
                            examples = @ExampleObject(value = SERVICE_ORDER_REQUEST_JSON)
                    )
            )
            @Valid @RequestBody ServiceOrderRequest request
    ) {
        return serviceOrderService.createServiceOrder(request);
    }

    @PutMapping(Endpoint.Basic.UPDATE + "/{id}")
    public ResponseEntity<?> updateServiceOrder(@PathVariable Integer id, @Valid @RequestBody ServiceOrderRequest request) {
        return serviceOrderService.updateServiceOrder(id, request);
    }

    @DeleteMapping(Endpoint.Basic.DELETE + "/{id}")
    public ResponseEntity<?> deleteServiceOrder(@PathVariable Integer id) {
        return serviceOrderService.deleteServiceOrder(id);
    }
}
