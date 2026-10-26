# Game Setup Phase

This is board task US-01. The other tasks for the full game are in [user-stories.md](user-stories.md).

The game is a two-player Chess GUI built with Java Swing. This feature ends when the system is fully initialized and ready for the first turn. Moving a piece, capturing, castling, en passant, promotion, check, checkmate, and stalemate are later features.

## User Story

As a player of Chess, **I want** the game to be properly set up automatically when a new game starts, **so that** I can begin playing from the standard starting position without placing the pieces myself.

## Acceptance Criteria

1. When the application launches, it shows a welcome screen with a name field for Player 1, a name field for Player 2, and a Start Game button.
2. The game must not start unless both players have entered a name.
3. A name that is empty or only whitespace is rejected. The system stays on the welcome screen and tells the player that both names are required.
4. Player 1 plays White. Player 2 plays Black.
5. The board is shown with White along the bottom. The corner square to White's right (h1) is light, and the square colors alternate.
6. The board is an 8×8 grid with all 32 pieces in the standard starting position:
   - Rank 1, from the a-file to the h-file: white rook, knight, bishop, queen, king, bishop, knight, rook.
   - Rank 2: eight white pawns.
   - Ranks 3, 4, 5, and 6: empty.
   - Rank 7: eight black pawns.
   - Rank 8, from the a-file to the h-file: black rook, knight, bishop, queen, king, bishop, knight, rook.
7. White (Player 1) moves first.
8. After a successful start, the main window shows both players' names, their colors, whose turn it is, and the board.

## Use Case 1: Start New Game

- **Actor:** Player
- **Preconditions:**
  - The game application is launched.
  - The welcome screen is displayed.
- **Main Flow:**
  1. The player enters Player 1's name.
  2. The player enters Player 2's name.
  3. The player clicks "Start Game".
  4. The system checks that both names contain at least one character after trimming whitespace.
  5. The system assigns Player 1 to White and Player 2 to Black.
  6. The system creates an 8×8 board, with White at the bottom and h1 light, and places all 32 pieces in the standard starting position.
  7. The system sets the current turn to White (Player 1).
  8. The system closes the welcome screen and opens the main game window.
  9. The system displays both players, their colors, the current player, and the board.
- **Alternate Flows:**
  - 4.a One or both names are missing or only whitespace. The system indicates that both player names are required and stays on the welcome screen.
  - 4.b Resumes at Step 1.
- **Postconditions:**
  - All 32 pieces are on their starting squares.
  - It is White's turn.
  - The game is ready for the first move.
