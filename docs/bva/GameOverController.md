# BVA Analysis for GameOverController

Issue #75. When a game ends, a dialog opens over the game window with the result
and two buttons, New Game and Close. `GameOverController` decides whether the
dialog opens, what it says, and what happens after the players choose.
`GameOverView` is the dialog, so it has no BVA file.

The result text comes from `GameStatsController.resultLine()`, the same method
that builds the status line, so the dialog and the status line never disagree.
The tests use a real `GameStatsController` over a mocked `Game` for that reason,
and mock only `GameOverView`.

A claimed draw ends the game as a draw by repetition or by the fifty-move rule,
so the repetition and fifty-move cases cover it.

### Method under test: `showIfOver()`

Step 1:
- input: whether the game is over
- input: whether the dialog already opened for this game
- input: how the game ended (the result line)
- input: what the players choose in the dialog
- output: whether the dialog opens, and its text
- output: what happens next (back to the welcome window with both names, exit,
  or nothing)

Step 2/3:
- game over -> Boolean: false, true
- dialog already opened -> Boolean: false, true
- how the game ended -> Cases: checkmate by White, checkmate by Black,
  resignation won by White, resignation won by Black, stalemate, dead position,
  repetition, the fifty-move rule, the seventy-five-move rule, agreement
- choice -> Cases: New Game, Close, closed with the window's close button
- output:
  - no dialog while the game is in progress, or when the dialog already opened
  - the dialog shows the result line
  - New Game returns to the welcome window with Player 1's and Player 2's
    names, in that order
  - Close exits the application
  - closing the dialog with the window's close button does nothing else, so the
    game window stays open on the final position

In TC2 to TC12 the players close the dialog with the window's close button, so
only the dialog's text is checked.

- **TC1: game in progress** ( :white_check_mark: )
    - **State of the system**: started game. Player 1 Ada / White. Player 2 Bo / Black. The game is not over.
    - **Expected output**: no dialog opens

- **TC2: White wins by checkmate** ( :x: )
    - **State of the system**: the game is over by checkmate. `winnerName()` is Ada, `winnerColor()` is `WHITE`.
    - **Expected output**: the dialog shows `Checkmate: Ada (White) wins`

- **TC3: Black wins by checkmate** ( :x: )
    - **State of the system**: the game is over by checkmate. `winnerName()` is Bo, `winnerColor()` is `BLACK`.
    - **Expected output**: the dialog shows `Checkmate: Bo (Black) wins`

- **TC4: White wins by resignation** ( :x: )
    - **State of the system**: the game is over, `isWonByResignation()` is true, `winnerName()` is Ada, `winnerColor()` is `WHITE`.
    - **Expected output**: the dialog shows `Resignation: Ada (White) wins`

- **TC5: Black wins by resignation** ( :x: )
    - **State of the system**: the game is over, `isWonByResignation()` is true, `winnerName()` is Bo, `winnerColor()` is `BLACK`.
    - **Expected output**: the dialog shows `Resignation: Bo (Black) wins`

- **TC6: draw by stalemate** ( :x: )
    - **State of the system**: the game is over, `isStalemate()` is true.
    - **Expected output**: the dialog shows `Draw by stalemate`

- **TC7: draw by dead position** ( :x: )
    - **State of the system**: the game is over, `isDrawnByDeadPosition()` is true.
    - **Expected output**: the dialog shows `Draw by dead position`

- **TC8: draw by repetition** ( :x: )
    - **State of the system**: the game is over, `isDrawnByRepetition()` is true.
    - **Expected output**: the dialog shows `Draw by repetition`

- **TC9: draw by the fifty-move rule** ( :x: )
    - **State of the system**: the game is over, `isDrawnByFiftyMove()` is true.
    - **Expected output**: the dialog shows `Draw by the fifty-move rule`

- **TC10: draw by the seventy-five-move rule** ( :x: )
    - **State of the system**: the game is over, `isDrawnBySeventyFiveMove()` is true.
    - **Expected output**: the dialog shows `Draw by the seventy-five-move rule`

- **TC11: draw by agreement** ( :x: )
    - **State of the system**: the game is over, `isDrawnByAgreement()` is true.
    - **Expected output**: the dialog shows `Draw by agreement`

- **TC12: the dialog already opened for this game** ( :x: )
    - **State of the system**: the game is over by stalemate. `showIfOver()` already ran once and the players closed the dialog with the window's close button. `showIfOver()` runs again.
    - **Expected output**: the dialog opened only the first time

- **TC13: the players choose New Game** ( :x: )
    - **State of the system**: the game is over by stalemate. Player 1 Ada, Player 2 Bo. The players choose New Game.
    - **Expected output**: the welcome window opens with Ada as Player 1 and Bo as Player 2

- **TC14: the players choose Close** ( :x: )
    - **State of the system**: the game is over by stalemate. The players choose Close.
    - **Expected output**: the application exits
