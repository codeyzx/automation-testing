# JTKLearn Web Automation Testing - Test Case

## Environment

| Field | Value |
| --- | --- |
| Application | JTKLearn |
| Default URL | `https://polban-space.cloudias79.com/jtk-learn` |
| Browser | Google Chrome |
| Framework | Cucumber JVM + Selenium Java |
| Build tool | Maven |
| Pattern | Page Object Model + PageFactory |
| Report | Cucumber HTML and JSON |

## Test Case Traceability

| Test Case | Area | Scenario | Feature file |
| --- | --- | --- | --- |
| `TC01` | Login | Instructor can login with valid credentials | `login.feature` |
| `LOGIN-1.0.2` | Login | Teacher can login and see dashboard navigation | `login.feature` |
| `LOGOUT-2.2.1` | Logout | Student can logout and return to login page | `logout.feature` |
| `TC-FR05-01` | My Course | Course progress updates after completing instruction | `my_course.feature` |
| `TC-FR05-02` | My Course | Enrolled course appears with 0% progress before instruction completion | `my_course_progress_zero.feature` |
| `TC-FR05-04` | My Course | New student sees empty state in Dalam Progres tab | `my_course_empty_progress.feature` |

## TC01 - Login Instructor

**Preconditions**

- Browser opens the JTKLearn login page.
- Instructor account exists.

**Test Data**

| Field | Value |
| --- | --- |
| Email | `ahmadjoni@gmail.com` |
| Password | `ahmadjoni` |
| Role | Pengajar |

**Steps**

1. Enter instructor email.
2. Enter instructor password.
3. Click `Masuk`.

**Expected Result**

- Login succeeds.
- Instructor dashboard is displayed.
- Navigation menu and account menu are visible.

## LOGIN-1.0.2 - Login Teacher Dashboard Navigation

**Preconditions**

- Browser opens the JTKLearn login page.
- Teacher account exists.

**Test Data**

| Field | Value |
| --- | --- |
| Email | `ahmadjoni@example.com` |
| Password | `ahmadjoni` |
| Role | Pengajar |

**Steps**

1. Enter teacher email.
2. Enter teacher password.
3. Click login button.

**Expected Result**

- User is redirected to teacher dashboard.
- Menu `Beranda` and `Rekap Hasil Kuis` are displayed.
- Account name is displayed.

## LOGOUT-2.2.1 - Logout Student

**Preconditions**

- Student account exists.
- Student is logged in.

**Test Data**

| Field | Value |
| --- | --- |
| Email | `ahmadjonii@gmail.com` |
| Password | `ahmadjoni` |
| Role | Pelajar |

**Steps**

1. Open account dropdown.
2. Click logout button.

**Expected Result**

- User returns to login page.
- Login page title contains `Selamat datang,`.

## TC-FR05-01 - My Course Progress After Completion

**Preconditions**

- Student account exists.
- Student is enrolled in `Kursus Ahli Pedang`.

**Steps**

1. Login as student.
2. Open `Kursus Ahli Pedang`.
3. Complete instruction `Dasar-Dasar Menggunakan Pedang`.
4. Open `Kursus Saya`.
5. Open `Dalam Progres` tab.

**Expected Result**

- Progress percentage displays `33.333332%`.
- Course does not appear in `Selesai` tab.

## TC-FR05-02 - My Course Zero Progress

**Preconditions**

- Student account exists.
- Student is enrolled in `Odoo Course`.
- No instruction has been completed.

**Steps**

1. Login as student.
2. Open `Kursus Saya`.
3. Open `Dalam Progres` tab.

**Expected Result**

- `Odoo Course` is displayed.
- Progress percentage is `0%`.
- Progress bar is empty.
- Course does not appear in `Selesai` tab.

## TC-FR05-04 - My Course Empty State

**Preconditions**

- New student account exists.
- Student has no enrolled courses.

**Test Data**

| Field | Value |
| --- | --- |
| Email | `pendaftaran_baru@example.com` |
| Password | `admin123` |
| Role | Pelajar |

**Steps**

1. Create new student account.
2. Login as new student.
3. Open `Kursus Saya`.

**Expected Result**

- `Dalam Progres` tab is active.
- System displays `Kursus belum ada`.
- No course card is displayed.
- `Selesai` tab is available and clickable.

## Automation Notes

- Feature files contain tags matching each test case ID.
- Page classes hold locators and page actions.
- Step Definition classes stay focused on Cucumber flow.
- Test setup helpers live in `utils.UserTestDataManager`.
- Runtime constants live in `utils.TestConfig`.
