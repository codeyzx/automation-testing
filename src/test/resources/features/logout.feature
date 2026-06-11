Feature: Logout Functionality

  @LOGOUT-2.2.1 @Logout
  Scenario: Check logout is successful for role Pelajar
    Given user opens JTKLearn login page
    And user logs in with valid Pelajar credentials
    And user is on dashboard page
    When user clicks account dropdown
    And user clicks logout button
    Then user should be redirected to login page
    And page title should contain "Selamat datang,"
