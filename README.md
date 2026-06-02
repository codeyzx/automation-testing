# JTKLearn Web Automation Testing Project

Project ini adalah implementasi testing automation untuk platform **JTKLearn** menggunakan **Cucumber JVM, Selenium Java, JUnit, dan WebDriverManager**. Pengujian difokuskan pada fungsionalitas utama bagi peran **Pelajar** (Student), yaitu fitur Logout dan Verifikasi Progres Kursus.

---

## 🚀 Fitur Utama
1. **Logout Functionality**: Memastikan pelajar dapat keluar dengan sukses dari sistem dan sesi dihapus dengan benar.
2. **My Course Progress Validation**: Memverifikasi bahwa persentase kemajuan kursus (progress percentage) diperbarui secara akurat setelah menyelesaikan instruksi tertentu.

---

## 🛠️ Tech Stack & Dependency
Project ini dikembangkan menggunakan teknologi testing terbaru:
* **Java 17** (Temurin JDK)
* **Maven** (Build Tool & Dependency Management)
* **Selenium WebDriver (Java)** (Browser Automation)
* **Cucumber JVM (Java & JUnit)** (Behavior Driven Development)
* **JUnit 4** (Test Runner)
* **WebDriverManager** (Automated Driver Management)

Semua dependensi dikonfigurasi dalam berkas [pom.xml](file:///d:/code/college/smt6/PPL/w14/pom.xml).

---

## 📂 Struktur Folder Project
Berikut adalah struktur folder project yang bersih dan modular sesuai dengan Best Practices:

```text
jtklearn-automation-testing/
│
├── pom.xml                                      # Konfigurasi Maven & Dependensi
├── README.md                                    # Dokumentasi Utama Project
├── TEST_RESULT.md                               # Laporan Hasil Pengujian & Bug
│
└── src/
    └── test/
        ├── java/                                # Kode Sumber Pengujian
        │   ├── pages/                           # Page Object Model (POM)
        │   │   ├── LoginPage.java
        │   │   ├── DashboardPage.java
        │   │   ├── CoursePage.java
        │   │   └── MyCoursePage.java
        │   │
        │   ├── stepdefinitions/                 # Cucumber Step Definitions
        │   │   ├── Hooks.java                   # Setup, Teardown, Screenshot-on-Failure
        │   │   ├── LoginSteps.java
        │   │   ├── LogoutSteps.java
        │   │   └── MyCourseSteps.java
        │   │
        │   ├── runners/                         # Cucumber Test Runner
        │   │   └── TestRunner.java
        │   │
        │   └── utils/                           # WebDriver & Driver Factory
        │       └── DriverFactory.java
        │
        └── resources/                           # Resource Pendukung
            ├── features/                        # Gherkin Feature Files
            │   ├── logout.feature
            │   └── my_course.feature
            │
            └── screenshots/                     # Folder Screenshot Evidence
                └── my_course_failure.png        # Evidence Bug SUT
```

---

## 💻 Cara Menjalankan Test

### 1. Prasyarat
Pastikan Anda memiliki tools berikut terinstall pada komputer Anda:
* Java JDK 17 atau yang lebih baru.
* Apache Maven 3.8+.
* Google Chrome Browser (WebDriverManager akan otomatis mengunduh driver yang sesuai).

### 2. Eksekusi Test Suite (Headless Mode - Default)
Secara default, pengujian akan dijalankan secara **headless** (tanpa memunculkan window browser) agar stabil di lingkungan CI/CD atau CLI:
```bash
mvn clean test
```

### 3. Eksekusi Test Suite (Headed Mode / Visual Browser)
Jika Anda ingin melihat jalannya pengujian di browser secara visual, jalankan perintah berikut:
```bash
mvn test -Dheadless=false
```

---

## 📊 Lokasi Laporan (Test Reports)
Setelah pengujian selesai dijalankan, Cucumber akan otomatis membuat laporan interaktif di dalam folder `target/`:

1. **HTML Report**: Laporan interaktif lengkap dengan penataan CSS premium dan screenshot kegagalan tersemat:
   * Lokasi: `target/cucumber-report.html`
2. **JSON Report**: Laporan dalam format JSON mentah untuk integrasi tools eksternal:
   * Lokasi: `target/cucumber-report.json`
