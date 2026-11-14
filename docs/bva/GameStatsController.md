# BVA Analysis for GameStatsController

This is my slice of US-01 (#7). `GameStatsView` and `GameFrame` are views, so they don't get BVA files. I only analyze `GameStatsController`.

The methods come from `docs/design/us-01-game-setup.md`. `Game` is Vanessa's. I will stub it in the tests. The controller asks `Game` for each name (`playerOneName()`, `playerTwoName()`, `playerToMoveName()`) instead of reaching into `Player`.

### Method under test: `GameStatsController(Game game)`

Step 1:
- input: the game
- output: a controller, or an exception

Step 2/3:
- game -> Cases: started, not started (no null: our code never passes one, so the constructor does not check for it)
- output -> Cases: controller created, exception

The design says the constructor requires a started game.

- **TC1: started game** ( :white_check_mark: )
    - **State of the system**: `game.isStarted()` is true. Player 1 is Ada / White. Player 2 is Bo / Black. `playerToMoveName()` is Player 1's name.
    - **Expected output**: the controller is created

- **TC2: game has not been started** ( :white_check_mark: )
    - **State of the system**: `game.isStarted()` is false
    - **Expected output**: `IllegalArgumentException`

- **TC3: removed.** It passed a null game and expected `IllegalArgumentException`. The null check was dropped after the instructor's Week 6 code review: our code never passes null.

### Method under test: `createView()`

Step 1:
- input: the started game (the two names and who is to move)
- output: a `GameStatsView` with the three lines from the design

Step 2/3:
- names -> Cases: one character (shortest name that survived start), a normal name, a name with a space in the middle, two similar but different names (start now rejects equal names)
- the name of the player to move -> Cases: Player 1 (what setup always does), Player 2 (the method still has to print that name)
- output is always:
  - `Player 1: <name> (White)`
  - `Player 2: <name> (Black)`
  - `Current player: <name of the player to move>`

- **TC4: normal names, Player 1 to move** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `playerToMoveName()` is Player 1's name.
    - **Expected output**: the view shows
        - `Player 1: Ada (White)`
        - `Player 2: Bo (Black)`
        - `Current player: Ada`

- **TC5: one-character names** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 A / White. Player 2 B / Black. `playerToMoveName()` is Player 1's name.
    - **Expected output**: the view shows
        - `Player 1: A (White)`
        - `Player 2: B (Black)`
        - `Current player: A`

- **TC6: names with a space in the middle** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ann Marie / White. Player 2 Li Wei / Black. `playerToMoveName()` is Player 1's name.
    - **Expected output**: the view shows
        - `Player 1: Ann Marie (White)`
        - `Player 2: Li Wei (Black)`
        - `Current player: Ann Marie`

- **TC7: similar but different names** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Alex / White. Player 2 Alexa / Black. `playerToMoveName()` is Player 1's name.
    - **Expected output**: the view shows
        - `Player 1: Alex (White)`
        - `Player 2: Alexa (Black)`
        - `Current player: Alex`

- **TC8: Player 2 is to move** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `playerToMoveName()` is Player 2's name.
    - **Expected output**: the view shows
        - `Player 1: Ada (White)`
        - `Player 2: Bo (Black)`
        - `Current player: Bo`

### Method under test: `refreshStatus()`

Issue #41. After a legal move the turn has passed, so the current-player line
must be read from `Game` again. `BoardView` calls this after a click.

Issue #48 (GitHub US-15, docs US-15) renames `refreshCurrentPlayer()` to
`refreshStatus()`, because once the game is over the same line shows the
result instead of the player to move: the winner's name and color, and that
the result is checkmate (US-15 AC 4).

Step 1:
- input: whether the game is over; the player to move; the winner's name and color
- output: the status line on the view

Step 2/3:
- game over -> Boolean: false, true
- winner -> Cases: Player 1 / White, Player 2 / Black
- result of a game that is over -> Cases: checkmate, draw by repetition (issue #59), resignation (issue #60), draw by the fifty-move rule (issue #51), draw by the seventy-five-move rule (issue #51)
- output is:
  - `Current player: <name of the player to move>` while the game is not over
  - `Checkmate: <winner's name> (<winner's color>) wins` once the game is over by checkmate
  - `Draw by repetition` once the game is drawn by repetition
  - `Resignation: <winner's name> (<winner's color>) wins` once the other player has resigned
  - `Draw by the fifty-move rule` once the game is drawn by the fifty-move rule
  - `Draw by the seventy-five-move rule` once the game is drawn by the seventy-five-move rule

- **TC9: refresh after the turn passes** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran while `playerToMoveName()` was Ada. Then `playerToMoveName()` is Bo, the game is not over, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Current player: Bo`

- **TC10: refresh after Player 2 checkmates Player 1** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `winnerName()` is Bo, `winnerColor()` is `BLACK`, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Checkmate: Bo (Black) wins`

- **TC11: refresh after Player 1 checkmates Player 2** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `winnerName()` is Ada, `winnerColor()` is `WHITE`, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Checkmate: Ada (White) wins`

Issue #44 (GitHub US-11 AC 6). While the game is not over and the player to
move is in check, the status line says so next to the player's name. A
checkmated player is also in check, but the checkmate line wins, so the check
is only asked about while the game is not over.

Step 1:
- input (added): whether the player to move is in check (`isPlayerToMoveInCheck()`)

Step 2/3:
- in check -> Boolean: false, true
- output (added): `Current player: <name of the player to move> (in check)`
  while the game is not over and the player to move is in check

TC9 still holds: the player to move is not in check, so the line is unchanged.

- **TC12: refresh while the player to move is in check** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then `playerToMoveName()` is Bo, the game is not over, `isPlayerToMoveInCheck()` is true, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Current player: Bo (in check)`

- **TC13: refresh after checkmate shows the result, not the check** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `isPlayerToMoveInCheck()` is true, `winnerName()` is Bo, `winnerColor()` is `BLACK`, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Checkmate: Bo (Black) wins`

Issue #59 (GitHub US-19, docs US-18). After a draw by repetition, the same line
shows the result instead of the player to move (US-19 AC 8). A draw has no
winner, so the line names no player.

- **TC14: refresh after a draw by repetition** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `isDrawnByRepetition()` is true, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Draw by repetition`

Issue #60 (GitHub US-20, docs US-20). After a resignation, the same line shows
the opponent's name and color as the winner by resignation (US-20 AC 4).

- **TC15: refresh after Player 1 resigns** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `isDrawnByRepetition()` is false, `isWonByResignation()` is true, `winnerName()` is Bo, `winnerColor()` is `BLACK`, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Resignation: Bo (Black) wins`

- **TC16: refresh after Player 2 resigns** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `isDrawnByRepetition()` is false, `isWonByResignation()` is true, `winnerName()` is Ada, `winnerColor()` is `WHITE`, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Resignation: Ada (White) wins`

Issue #61 (GitHub US-21, docs US-21). After the opponent accepts a draw offer,
the same line shows a draw by agreement instead of the player to move (US-21
AC 7). A draw has no winner, so the line names no player.

Step 1:
- input (added): whether the game is drawn by agreement (`isDrawnByAgreement()`)

Step 2/3:
- result of a game that is over -> Cases (added): draw by agreement
- output (added): `Draw by agreement` once the game is drawn by agreement

TC10, TC11, and TC13 still hold: the game answers false for
`isDrawnByAgreement()`, so a checkmate still shows the winner.

- **TC17: refresh after a draw by agreement** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `isDrawnByRepetition()` is false, `isDrawnByAgreement()` is true, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Draw by agreement`

Issue #51 (GitHub US-18, docs US-19). After a fifty-move or seventy-five-move
draw, the same line shows the result. A draw has no winner, so the line names
no player. The queries are asked before `winnerName()`. The case numbers
start at TC18, after agreement's TC17, so they do not collide with stalemate
or dead position.

- **TC18: refresh after a fifty-move draw** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `isDrawnByFiftyMove()` is true, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Draw by the fifty-move rule`

- **TC19: refresh after a seventy-five-move draw** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `isDrawnByFiftyMove()` is false, `isDrawnBySeventyFiveMove()` is true, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Draw by the seventy-five-move rule`
