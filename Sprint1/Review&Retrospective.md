# Sprint 1 Review & Retrospective for ByteMechanics

**Date:** 9/25
**Dev Team Members Present:** Abby Rictor, Aislinn Jeske, Farzaneh Kadkhodaie, Marylou Nash, and Zach Klausner

## Before We Begin:
- We made mistakes this sprint by not including Professor Moreno
- We create user stories and ask her for priority 
- We include her in our sprint review
- For the last bit of the sprint, we need to stop coding and focus on the “Acceptance Criteria” for the single moves of a player
- Our code doesn’t need to be changed, just put on hold until it’s determined what is the highest priority for the next sprint

## Review
- What work did we commit to be delivered? (What was in our Sprint Backlog?)
  - We prioritized correct functionality of the game and a way to see and interact with the game. With this, came a way for the client and server to interact and ha format so they could decipher these messages. We were also committed to creating JUnit tests for our implementation of the game.
- What work did we complete?
  - We created a Maven project with a React client with a MySql database. In this system, implemented move validation for all pieces, client/server communication and message protocol, and a semi-functional client interface with continuous integration.
- What were key decisions we made regarding:
  - Server: We chose to have Maven to run tests and compile, it would connect to client with a websocket, we established 3 interchange protocols to create Java objects 
  - Game: We store the game as a 2 dimensional array, we can access a piece two ways (both through the board and through the player), we have a player object that contains all of their pieces, and we have a board that stores all the pieces
  - Client: We chose to have a React client and use SASS for styling
  - Database: We were given a MySql database, but we chose to have a table for users and a table for matches

- Did we accomplish our project metric goals? (Is our code coverage up to standards?)
  - We created JUnit tests for the main subsystems of our server (database, game, and client/server messaging
- Demo of the work
- Priority review of tasks for next sprint
- Goals for next sprint
  - Code Climate score of ‘B’ or higher for all classes
  - Keep better track of story points vs time
  - 50%+ test coverage for system
  - Collaboration on user stories
  - Meeting time: Wednesday 5:15 - 7:00


## Retrospective
- What are we doing well and should keep doing?
  - Code going in everyday, incremental development, everyone is working
  - Although we’re working on separate things, we’re collaborating well
  - Good interaction and collaboration
  - Good communication
  - Good time between opening PR and closing
- What should we start doing in order to improve?
  - Create standards for PR’s and who needs to review/merge
  - Have mini demos in our meetings to keep others updated on our work
  - Spread the work throughout the sprint
  - At the beginning, be more specific about what we’re going to accomplish during the sprint
  - Have more meetings during the week, even if everyone can’t make it
  - Keep track of what stories we finish
- What should we stop doing in order to improve?
  - Stop using a single branch
  - Stop merging code without at least one unit test
