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
