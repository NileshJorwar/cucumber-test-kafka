Feature: Cucumber G-Adapter e-2-e Testing
  Scenario Outline: As a tester I want to send/receive/verify message
    Given I am giving Message <message> and <resources>
    Then I should be able to see the message on the queue.
    Examples:
    |message|resources|
    |testMsg|/testResrc|
