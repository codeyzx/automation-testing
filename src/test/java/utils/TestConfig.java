package utils;

public final class TestConfig {
    public static final String BASE_URL = System.getProperty(
            "app.url",
            "https://polban-space.cloudias79.com/jtk-learn");

    public static final String LOGIN_URL = BASE_URL + "/";
    public static final String USERS_URL = BASE_URL + "/users";
    public static final String STUDENT_DASHBOARD_URL = BASE_URL + "/dashboard-pelajar";
    public static final String MY_COURSES_URL = BASE_URL + "/my-courses";

    public static final String ADMIN_EMAIL = "admin@example.com";
    public static final String ADMIN_PASSWORD = "admin";

    public static final String DEFAULT_STUDENT_NAME = "Ahmad Joni";
    public static final String DEFAULT_STUDENT_EMAIL = "ahmadjonii@gmail.com";
    public static final String DEFAULT_STUDENT_PASSWORD = "ahmadjoni";

    public static final String NEW_STUDENT_NAME = "Pendaftaran Baru";
    public static final String NEW_STUDENT_EMAIL = "pendaftaran_baru@example.com";
    public static final String NEW_STUDENT_PASSWORD = "admin123";

    public static final String ZERO_PROGRESS_STUDENT_EMAIL = "joni@gmail.com";
    public static final String ZERO_PROGRESS_STUDENT_PASSWORD = "joni";

    private TestConfig() {
    }
}
