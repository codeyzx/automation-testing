Feature: My Course - Empty Progress Tab

  @TC-FR05-04 @MyCourse
  Scenario: Verify Dalam Progres tab for new student with no enrolled courses
    Given a new student account exists with no enrolled courses
    And the student successfully logs in
    When the student navigates to "Kursus Saya"
    Then the "Dalam Progres" tab should be active
    And the system should display message "Kursus belum ada"
    And no course card should be displayed
    And the "Selesai" tab should be available and clickable
