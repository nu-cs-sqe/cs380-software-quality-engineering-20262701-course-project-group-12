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
- output is:
  - `Current player: <name of the player to move>` while the game is not over
  - `Checkmate: <winner's name> (<winner's color>) wins` once the game is over

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

- **TC13: refresh after checkmate shows the result, not the check** ( :x: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `createView()` ran. Then the game is over, `isPlayerToMoveInCheck()` is true, `winnerName()` is Bo, `winnerColor()` is `BLACK`, and `refreshStatus()` runs.
    - **Expected output**: the view shows `Checkmate: Bo (Black) wins`
