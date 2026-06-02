Feature: My Course Progress

  Scenario: Check progress percentage displays correctly as 33.333332%
    Given user opens JTKLearn login page
    And user logs in with valid Pelajar credentials
    And user is enrolled in "Kursus Ahli Pedang"
    When user opens course "Kursus Ahli Pedang"
    And user completes instruction "Dasar-Dasar Menggunakan Pedang"
    And user navigates to "Kursus Saya"
    And user opens "Dalam Progres" tab
    Then progress percentage should display "33.333332%"
    And course should not appear in "Selesai" tab
