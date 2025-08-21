package hasanalmunawr.dev.backend_spring.base.constants;

public class Endpoint {

    public static class Base {
        public static final String AUTHENTICATION = "/authentication";
        public static final String PUBLIC = "/public";
        public static final String USER = "/user";
        public static final String GENERATOR = "/generator";
        public static final String PERMISSION = "/permission";
        public static final String ROLE = "/role";
        public static final String PUBLIC_API = "/api";
        public static final String CONFIG = "/config";

        public static final String BRANCH = "/branches";
        public static final String REGION = "/regions";
        public static final String COLOR = "/colors";
        public static final String PRODUCT = "/products";
        public static final String SERVICE_ORDER = "/service-orders";

        public static final String BANK_ACCOUNT = "/bank-accounts";
        public static final String TRANSACTION = "/transactions";
        public static final String CATEGORY = "/categories";
        public static final String BUDGET = "/budgets";
        public static final String BUDGET_HEADER = "/budget-headers";
        public static final String DEBT = "/debts";
        public static final String DASHBOARD = "/dashboards";
    }

    public static class Basic {
        public static final String CREATE = "/create";
        public static final String UPDATE = "/update";
        public static final String DELETE = "/delete";
        public static final String SELECT = "/select";
        public static final String ASSIGN = "/assign";

        public static final String VIEW = "/view";
        public static final String SEARCH = "/search";
        public static final String DROPDOWN = "/dropdown";
        public static final String GENERATE = "/generate";
        public static final String DETAIL = "/detail";
        public static final String VALIDATE =  "/validate";
        public static final String ACTIVATE = "/activate";
        public static final String DEACTIVATE = "/deactivate";
        public static final String REVOKE = "/revoke";
    }

    public static class Auth {
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String LOGOUT = "/logout";
        public static final String VERIFY_CODE = "/verify-code";
        public static final String REFRESH_TOKEN = "/refresh-token";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String RESET_PASSWORD = "/reset-password";
    }

    public static class User {
        public static final String HANDLER = "/handler";
        public static final String PROFILE = "/profile";
        public static final String UPDATE_PROFILE = "/update-profile";
        public static final String UPDATE_PASSWORD = "/update-password";

    }

    public static class Config {
        public static final String GET_CONFIG = "/get-config";
    }
}
