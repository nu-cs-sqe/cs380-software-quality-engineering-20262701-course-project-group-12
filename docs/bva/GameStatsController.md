# BVA Analysis for GameStatsController

This is my slice of US-01 (#7). `GameStatsView` and `GameFrame` are views, so they don't get BVA files. I only analyze `GameStatsController`.

The methods come from `docs/design/us-01-game-setup.md`. `Game` is Vanessa's. I will stub it in the tests.

### Method under test: `GameStatsController(Game game)`

Step 1:
- input: the game
- output: a controller, or an exception

Step 2/3:
- game -> Cases: started, not started, null
- output -> Cases: controller created, exception

The design says the constructor requires a started game.

- **TC1: started game** ( :white_check_mark: )
    - **State of the system**: `game.isStarted()` is true. Player 1 is Ada / White. Player 2 is Bo / Black. `playerToMove()` is Player 1.
    - **Expected output**: the controller is created

- **TC2: game has not been started** ( :white_check_mark: )
    - **State of the system**: `game.isStarted()` is false
    - **Expected output**: `IllegalArgumentException`

- **TC3: game is null** ( :white_check_mark: )
    - **State of the system**: `game` is null
    - **Expected output**: `IllegalArgumentException`

### Method under test: `createView()`

Step 1:
- input: the started game (the two names and who is to move)
- output: a `GameStatsView` with the three lines from the design

Step 2/3:
- names -> Cases: one character (shortest name that survived start), a normal name, a name with a space in the middle, the same name twice (US-01 allows that)
- playerToMove -> Cases: Player 1 (what setup always does), Player 2 (the method still has to print that name)
- output is always:
  - `Player 1: <name> (White)`
  - `Player 2: <name> (Black)`
  - `Current player: <name of the player to move>`

- **TC4: normal names, Player 1 to move** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `playerToMove()` is Player 1.
    - **Expected output**: the view shows
        - `Player 1: Ada (White)`
        - `Player 2: Bo (Black)`
        - `Current player: Ada`

- **TC5: one-character names** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 A / White. Player 2 B / Black. `playerToMove()` is Player 1.
    - **Expected output**: the view shows
        - `Player 1: A (White)`
        - `Player 2: B (Black)`
        - `Current player: A`

- **TC6: names with a space in the middle** ( :x: )
    - **State of the system**: started game. Player 1 Ann Marie / White. Player 2 Li Wei / Black. `playerToMove()` is Player 1.
    - **Expected output**: the view shows
        - `Player 1: Ann Marie (White)`
        - `Player 2: Li Wei (Black)`
        - `Current player: Ann Marie`

- **TC7: both players have the same name** ( :x: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Ada / Black. `playerToMove()` is Player 1.
    - **Expected output**: the view shows
        - `Player 1: Ada (White)`
        - `Player 2: Ada (Black)`
        - `Current player: Ada`

- **TC8: Player 2 is to move** ( :x: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. `playerToMove()` is Player 2.
    - **Expected output**: the view shows
        - `Player 1: Ada (White)`
        - `Player 2: Bo (Black)`
        - `Current player: Bo`
