@Rafli
Feature: Login Functionality

  Scenario: Confirm successful login using instructor account
    Given user is on JTKLearn login page
    When user enters email "ahmadjoni@gmail.com"
    And user enters password "ahmadjoni"
    And user clicks Masuk button
    Then login should be successful
    And instructor dashboard should be displayed
    And navigation menu should contain "Beranda"
    And navigation menu should contain "Pemantauan"
    And account menu should be displayed
    And logout submenu should be accessible
