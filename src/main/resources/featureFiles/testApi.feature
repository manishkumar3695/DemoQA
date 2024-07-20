Feature: Api testing

  @k
  Scenario: creates new issue
    Given user stores the project id
    And user stores issuetype id
    And user stores the user id
    And user creates the issue