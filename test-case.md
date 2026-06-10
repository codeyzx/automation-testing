
# JTKLearn Web Automation Testing - Test Case

## Environment

* Application: JTKLearn
* URL: [http://124.158.117.196:5000/](http://124.158.117.196:5000/)
* Browser: Google Chrome
* Automation Framework: Cucumber + Selenium Java
* Testing Type: Functional Testing

---

# Test Case 1 - Login Functionality

## Test Case Information

| Field        | Value                                                                                             |
| ------------ | ------------------------------------------------------------------------------------------------- |
| Test Case ID | TC01                                                                                              |
| Module       | Authentication                                                                                    |
| Type         | Positive                                                                                          |
| Scenario     | Verifikasi User dengan hak akses Pengajar dapat masuk ke sistem menggunakan kredensial yang valid |

---

## Preconditions (GIVEN)

* Browser web sudah dibuka.
* User berada pada antarmuka login aplikasi JTK Learn.
* Data akun pengajar telah tersimpan dan valid di dalam sistem.

### URL

```text
http://124.158.117.196:5000/
```

---

## Test Data

| Field    | Value                                              |
| -------- | -------------------------------------------------- |
| Email    | [ahmadjoni@example.com](mailto:ahmadjoni@example.com) |
| Password | ahmadjoni                                          |
| Role     | Pengajar                                           |

---

## Steps to Execute (WHEN)

1. User mengetik alamat email pengajar yang terdaftar pada kolom  **Email** .
2. User memasukkan kata sandi yang sesuai pada kolom  **Password** .
3. User mengeklik tombol aksi  **"Masuk"** .

---

## Expected Result (THEN)

1. Proses autentikasi disetujui oleh sistem.
2. User langsung dialihkan ke laman  **Dashboard Pengajar** .
3. Pada bagian header navigasi muncul menu:
   * Beranda
   * Rekap Hasil Kuis
   * Nama Akun User

---

## Acceptance Criteria

* Login berhasil menggunakan kredensial valid.
* Sistem mengarahkan user ke Dashboard Pengajar.
* Menu navigasi Pengajar tampil dengan lengkap.
* Nama akun user tampil pada header aplikasi.

---

# Test Case 2 - My Course "Dalam Progres" Tab Verification

## Test Case Information

| Field        | Value                                                                                                     |
| ------------ | --------------------------------------------------------------------------------------------------------- |
| Test Case ID | TC-FR05-01                                                                                                |
| Module       | FR05 (My Course)                                                                                          |
| Type         | Negative                                                                                                  |
| Scenario     | Verifikasi tampilan tab "Dalam Progres" pada akun pelajar baru yang belum pernah mendaftar kursus manapun |

---

## Preconditions (GIVEN)

1. Akun pelajar BARU dibuat (bukan akun lama) dan belum pernah mendaftar kursus manapun.
2. Sistem memiliki kursus yang tersedia.
3. Pelajar baru berhasil login.

### URL

```text
http://124.158.117.196:5000/
```

---

## Test Data

| Field              | Value                                                            |
| ------------------ | ---------------------------------------------------------------- |
| Username           | [pendaftaran_baru@example.com](mailto:pendaftaran_baru@example.com) |
| Password           | admin123                                                         |
| Enrollment History | 0 kursus                                                         |
| User Type          | Pelajar Baru                                                     |

---

## Steps to Execute (WHEN)

1. Buat akun pelajar baru dengan email `pendaftaran_baru@example.com`.
2. Login ke aplikasi JTK Learn.
3. Klik menu **"Kursus Saya"** pada navigasi utama.
4. Pastikan tab **"Dalam Progres"** aktif secara default.
5. Amati seluruh tampilan halaman.

---

## Expected Result (THEN)

1. Halaman **Kursus Saya** berhasil dimuat.
2. Tab **"Dalam Progres"** aktif (terpilih).
3. Sistem menampilkan pesan  **"Kursus belum ada"** .
4. Tidak ada card kursus yang ditampilkan.
5. Tab **"Selesai"** tersedia dan dapat diklik.

---

## Acceptance Criteria

* Halaman Kursus Saya berhasil diakses tanpa error.
* Tab Dalam Progres menjadi tab aktif saat pertama kali dibuka.
* Pesan "Kursus belum ada" ditampilkan dengan benar.
* Tidak terdapat course card pada daftar kursus.
* Tab Selesai tersedia dan dapat digunakan untuk navigasi.

---

# Notes for Automation

## Recommended Automation Approach

* Use Page Object Model (POM)
* Use Page Factory for element initialization
* Use Explicit Wait (`WebDriverWait`)
* Avoid hardcoded `Thread.sleep()`
* Generate HTML and JSON report
* Capture screenshot automatically when scenario fails

---

# Recommended Feature Files

## login.feature

```gherkin
Feature: Authentication Login

  Scenario: Verify teacher can login using valid credentials
    Given user is on login page
    When user enters email "ahmadjoni@example.com"
    And user enters password "ahmadjoni"
    And user clicks login button
    Then user should be redirected to teacher dashboard
    And navigation menu should display:
      | Beranda |
      | Rekap Hasil Kuis |
    And account name should be displayed
```

---

## my_course_empty_progress.feature

```gherkin
Feature: My Course - Empty Progress Tab

  Scenario: Verify Dalam Progres tab for new student with no enrolled courses
    Given a new student account exists with no enrolled courses
    And the student successfully logs in
    When the student navigates to "Kursus Saya"
    Then the "Dalam Progres" tab should be active
    And the system should display message "Kursus belum ada"
    And no course card should be displayed
    And the "Selesai" tab should be available and clickable
```

---

# Traceability

| Requirement          | Test Case  |
| -------------------- | ---------- |
| Authentication Login | TC01       |
| Valid Teacher Login  | TC01       |
| FR05 - My Course     | TC-FR05-01 |
| Tab Dalam Progres    | TC-FR05-01 |
| Empty Course State   | TC-FR05-01 |
| New Student Behavior | TC-FR05-01 |

---

# Test Objective

1. Memastikan user dengan role **Pengajar** dapat melakukan login menggunakan kredensial yang valid dan diarahkan ke Dashboard Pengajar sesuai hak aksesnya.
2. Memastikan akun **Pelajar Baru** yang belum pernah mendaftar kursus mendapatkan tampilan yang benar pada menu  **Kursus Saya** , khususnya pada tab  **Dalam Progres** , dengan menampilkan kondisi kosong (empty state) yang sesuai dan tidak menampilkan data kursus yang tidak relevan.
