# JTKLearn Web Automation Testing

Project ini berisi automation test untuk JTKLearn menggunakan Cucumber JVM, Selenium Java, JUnit 4, Maven, Page Object Model, PageFactory, dan WebDriverManager.

## Scope Test Case

| Area | Test Case | Feature file |
| --- | --- | --- |
| Login | `@TC01` | `src/test/resources/features/login.feature` |
| Login | `@LOGIN-1.0.2` | `src/test/resources/features/login.feature` |
| Logout | `@LOGOUT-2.2.1` | `src/test/resources/features/logout.feature` |
| My Course | `@TC-FR05-01` | `src/test/resources/features/my_course.feature` |
| My Course | `@TC-FR05-02` | `src/test/resources/features/my_course_progress_zero.feature` |
| My Course | `@TC-FR05-04` | `src/test/resources/features/my_course_empty_progress.feature` |

## Konsep yang Digunakan

- Cucumber feature files untuk skenario Gherkin.
- Step Definition untuk menghubungkan Gherkin dengan kode Java.
- Selenium WebDriver untuk interaksi browser.
- Page Object Model untuk memisahkan aksi halaman dari step.
- PageFactory melalui `@FindBy` dan `PageFactory.initElements(...)`.
- Web locator menggunakan `id`, `css`, dan `xpath`.
- WebDriverManager untuk setup ChromeDriver otomatis.
- Maven Surefire untuk menjalankan test runner.
- Cucumber report HTML dan JSON di folder `target/`.

## Struktur Project

```text
src/test/java
  pages/           Page Object classes
  runners/         Cucumber test runner
  stepdefinitions/ Cucumber step bindings
  utils/           Driver, config, and test data helpers

src/test/resources/features
  login.feature
  logout.feature
  my_course.feature
  my_course_progress_zero.feature
  my_course_empty_progress.feature
```

## Prasyarat

- Java JDK 11 atau lebih baru.
- Apache Maven 3.8 atau lebih baru.
- Google Chrome.

## Cara Menjalankan

Default headless:

```bash
mvn clean test
```

Browser terlihat:

```bash
mvn test -Dheadless=false
```

Override URL aplikasi:

```bash
mvn test -Dapp.url=https://polban-space.cloudias79.com/jtk-learn
```

## Report

Setelah test berjalan, Cucumber membuat report:

- `target/cucumber-report.html`
- `target/cucumber-report.json`

Screenshot kegagalan disimpan lewat hook Cucumber di `target/failure-screenshot.png`.
