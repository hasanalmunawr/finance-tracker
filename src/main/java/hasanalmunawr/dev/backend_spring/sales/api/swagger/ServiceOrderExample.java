package hasanalmunawr.dev.backend_spring.sales.api.swagger;


public class ServiceOrderExample {

    public static final String SERVICE_ORDER_REQUEST_JSON = """
            {
              "service_date": "2025-04-25",
              "via_complaint": "Call Center",
              "service_branch": "Semarang",
              "service_category": "Walk-in",
             \s
              "customer_type": "End User",
              "customer_name": "Dina Ayu",
              "customer_address": "Jl. Pandanaran No. 3, Semarang",
              "customer_phone": "087812345678",
              "customer_email": "dina.ayu@domain.com",
              "store_name": null,
                        
              "product_category": "Elektronik",
              "product_type": "AC Split 1 PK",
              "product_color": "Putih",
              "serial_number": "AC001PK1234",
              "warranty_card": "WRTY2025AC",
              "purchase_order": "PO554433",
              "purchase_date": "2024-12-15",
              "warranty_status": "Masih Berlaku",
              "complaint": "AC tidak dingin dan mengeluarkan suara",
             \s
              "symptom": "Angin keluar tapi tidak dingin",
              "production_prefix": "ACPR2025",
              "failure_cause": "Kondensor kotor"
            }
                        
            """;
    public static final String SERVICE_ORDER_CREATE_RESPONSE_SUCCESS = """
            {
                "message": "Resource created",
                "result": {
                    "id": 6,
                    "service_id": "SO-20250528-0006",
                    "service_date": "2025-04-25",
                    "via_complaint": "Call Center",
                    "service_branch": "Semarang",
                    "service_category": "Walk-in",
                    "customer_type": "End User",
                    "customer_name": "Dina Ayu",
                    "customer_address": "Jl. Pandanaran No. 3, Semarang",
                    "customer_phone": "087812345678",
                    "customer_email": "dina.ayu@domain.com",
                    "store_name": null,
                    "product_category": "Elektronik",
                    "product_type": "AC Split 1 PK",
                    "product_color": "Putih",
                    "serial_number": "AC001PK1234",
                    "warranty_card": "WRTY2025AC",
                    "purchase_order": "PO554433",
                    "purchase_date": "2024-12-15",
                    "warranty_status": "Masih Berlaku",
                    "complaint": "AC tidak dingin dan mengeluarkan suara",
                    "symptom": "Angin keluar tapi tidak dingin",
                    "production_prefix": "ACPR2025",
                    "failure_cause": "Kondensor kotor",
                    "created_at": "2025-05-28T10:07:31.0424055",
                    "updated_at": "2025-05-28T10:07:31.0424055"
                },
                "status": true
            }
            """;
}
