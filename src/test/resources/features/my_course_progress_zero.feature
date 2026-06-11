@TC-FR05-02 @MyCourse
Feature: My Course Progress

  Scenario: Verify progress tab after enrolling in a course without starting any instruction
    Given student account is active
    And student is enrolled in "Odoo Course"
    And no instruction has been completed
    When student opens menu "Kursus Saya"
    And student opens tab "Dalam Progres"
    Then course "Odoo Course" should be displayed
    And progress percentage should be "0%"
    And progress bar should be empty
    And course should not appear in tab "Selesai"

