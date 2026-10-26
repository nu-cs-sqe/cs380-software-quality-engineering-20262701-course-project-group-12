# Chess User Stories

Each story below is one task for the project management board. The rules are the [FIDE Laws of Chess](https://handbook.fide.com/chapter/e012023) (in force from 1 January 2023). The game is a two-player Java Swing application on one screen.

A move is made by clicking one of your pieces, then clicking the destination square. This replaces the over-the-board touch-move rules in Article 4.

These stories cover the Basic Rules of Play (Articles 1, 2, 3, and 5) and the draw rules in Article 9. They do not include a chess clock, scoresheets, an arbiter, rapid or blitz, or Chess960. An illegal move is refused. The game does not use the competition penalty that forfeits a player who completes a second illegal move.

After every legal move, the system checks the result in this order, before the opponent is given the turn:

1. Checkmate. The mover wins.
2. Stalemate. The game is a draw.
3. Dead position. The game is a draw.
4. Fivefold repetition. The game is a draw.
5. Seventy-five moves by each player with no pawn move and no capture. The game is a draw, unless that move was checkmate.
6. Otherwise the turn passes. The player to move may then claim a threefold repetition or a fifty-move draw when that claim is valid.

## Board tasks

1. [US-01 Start a new game](#us-01-start-a-new-game)
2. [US-02 Move a piece on your turn](#us-02-move-a-piece-on-your-turn)
3. [US-03 Reject an illegal move](#us-03-reject-an-illegal-move)
4. [US-04 Move a pawn](#us-04-move-a-pawn)
5. [US-05 Move a rook](#us-05-move-a-rook)
6. [US-06 Move a knight](#us-06-move-a-knight)
7. [US-07 Move a bishop](#us-07-move-a-bishop)
8. [US-08 Move a queen](#us-08-move-a-queen)
9. [US-09 Move a king one square](#us-09-move-a-king-one-square)
10. [US-10 Capture an opponent's piece](#us-10-capture-an-opponents-piece)
11. [US-11 Castle](#us-11-castle)
12. [US-12 Capture en passant](#us-12-capture-en-passant)
13. [US-13 Promote a pawn](#us-13-promote-a-pawn)
14. [US-14 Keep the king out of check](#us-14-keep-the-king-out-of-check)
15. [US-15 Win by checkmate](#us-15-win-by-checkmate)
16. [US-16 Draw by stalemate](#us-16-draw-by-stalemate)
17. [US-17 Draw by dead position](#us-17-draw-by-dead-position)
18. [US-18 Draw by repetition](#us-18-draw-by-repetition)
19. [US-19 Draw by the fifty-move rule](#us-19-draw-by-the-fifty-move-rule)
20. [US-20 Resign](#us-20-resign)
21. [US-21 Agree to a draw](#us-21-agree-to-a-draw)

---

## US-01: Start a new game

**Board task:** Start a new game

### User Story

As a player of Chess, **I want** the game to be properly set up automatically when a new game starts, **so that** I can begin playing from the standard starting position without placing the pieces myself.

### Acceptance Criteria

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

### Use Case 1: Start New Game

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

---

## US-02: Move a piece on your turn

**Board task:** Move a piece on your turn

### User Story

As a player, **I want** to move one of my pieces on my turn, **so that** my opponent and I can play alternate moves.

### Acceptance Criteria

1. White moves first. After each legal move, the turn passes to the other player.
2. The player to move clicks one of their own pieces, then clicks the destination square.
3. Clicking another of that player's pieces changes the selection.
4. Clicking the selected piece again clears the selection.
5. A click on an empty square or on an opponent's piece does not select that square.
6. When the destination is a legal move for the selected piece, the piece occupies the destination, its previous square becomes empty, and the selection is cleared.
7. The opponent's pieces stay where they are, except a piece removed by a capture in US-10 or US-12.
8. No piece can be moved after the game has ended.

### Use Case 1: Make a legal move

- **Actor:** Player to move
- **Preconditions:**
  - A game is in progress and it is this player's turn.
  - The selected destination is a legal move under US-04 through US-14.
- **Main Flow:**
  1. The player clicks one of their pieces.
  2. The system marks that piece as selected.
  3. The player clicks the destination square.
  4. The system moves the piece to that square.
  5. The system clears the selection and gives the turn to the opponent, unless the move ended the game.
- **Alternate Flows:**
  - 1.a The player clicks an empty square or an opponent's piece. The system leaves the selection unchanged.
  - 2.a The player clicks a different piece of their own. The system selects that piece instead. Resumes at Step 3.
  - 2.b The player clicks the selected piece again. The system clears the selection. Resumes at Step 1.
  - 3.a The destination is illegal. The system handles it as in US-03.
- **Postconditions:**
  - The piece stands on the destination square.
  - It is the opponent's turn, unless the move ended the game.

---

## US-03: Reject an illegal move

**Board task:** Reject an illegal move

### User Story

As a player, **I want** an illegal move to be refused, **so that** the position and the turn stay unchanged until I choose a legal move.

### Acceptance Criteria

1. A move is illegal when it breaks any rule in US-04 through US-14.
2. The system does not move the piece.
3. The turn does not pass.
4. The system tells the player that the move is not allowed.
5. The selected piece stays selected, and the player may choose a different destination or a different piece.
6. The same refusal applies when the game is already over.

### Use Case 1: Attempt an illegal move

- **Actor:** Player to move
- **Preconditions:**
  - A game is in progress and it is this player's turn.
  - The player has selected one of their pieces.
- **Main Flow:**
  1. The player clicks a destination that is not a legal move.
  2. The system tells the player that the move is not allowed.
  3. The system leaves every piece on its current square.
- **Alternate Flows:**
  - 1.a The destination holds another piece of the same color. The system refuses the move.
- **Postconditions:**
  - The board is unchanged.
  - It is still the same player's turn.

---

## US-04: Move a pawn

**Board task:** Move a pawn

### User Story

As a player, **I want** to move a pawn by the pawn rules, **so that** a pawn advances toward the opponent and captures only on a diagonal.

### Acceptance Criteria

1. A white pawn moves toward rank 8. A black pawn moves toward rank 1.
2. A pawn may move one square straight forward onto an empty square.
3. A pawn on its starting rank (rank 2 for White, rank 7 for Black) may instead move two squares straight forward when both of those squares are empty.
4. A pawn may move one square diagonally forward onto a square occupied by an opponent's piece and capture it.
5. A pawn cannot move backward, sideways, or onto a square of its own color's piece.
6. A pawn cannot capture straight forward.
7. A two-square advance cannot capture and cannot jump over a piece.
8. Reaching the furthest rank is a promotion, handled by US-13. Capturing a pawn that has just advanced two squares is handled by US-12.

### Use Case 1: Advance a pawn

- **Actor:** Player to move
- **Preconditions:**
  - It is the player's turn, and the player selects a pawn.
  - The square one step forward is empty. For a two-square advance, the pawn is on its starting rank and both squares in front are empty.
- **Main Flow:**
  1. The player clicks the empty square one or two steps straight ahead.
  2. The system moves the pawn to that square.
  3. The system gives the turn to the opponent, unless the move ends the game.
- **Alternate Flows:**
  - 1.a The square in front is occupied, or the pawn is no longer on its starting rank and the player chooses a two-square advance. The system rejects the move as in US-03.
- **Postconditions:**
  - The pawn is on the chosen square.
  - It is the opponent's turn, unless the move ended the game.

### Use Case 2: Capture with a pawn

- **Actor:** Player to move
- **Preconditions:**
  - An opponent's piece stands one square diagonally forward from the selected pawn.
- **Main Flow:**
  1. The player clicks that occupied square.
  2. The system removes the opponent's piece and places the pawn there.
- **Alternate Flows:**
  - 1.a The diagonal square is empty. The system rejects the move, unless US-12 applies.
- **Postconditions:**
  - The pawn occupies the diagonal square.
  - The captured piece is off the board.

---

## US-05: Move a rook

**Board task:** Move a rook

### User Story

As a player, **I want** to move a rook any number of squares along a rank or file, **so that** the rook travels horizontally or vertically without jumping.

### Acceptance Criteria

1. A rook may move any number of squares along the rank or file it occupies, and no other way.
2. Every square between the rook and the destination is empty.
3. The destination is empty or holds an opponent's piece.
4. A rook cannot move over a piece.

### Use Case 1: Move a rook

- **Actor:** Player to move
- **Preconditions:**
  - The player selects a rook, and the destination is on the same rank or file with an empty path.
- **Main Flow:**
  1. The player clicks the destination square.
  2. The system moves the rook there. If the square holds an opponent's piece, that piece is captured as in US-10.
- **Alternate Flows:**
  - 1.a The destination is not on the same rank or file, or a piece stands between the rook and the destination. The system rejects the move as in US-03.
- **Postconditions:**
  - The rook is on the destination square.

---

## US-06: Move a knight

**Board task:** Move a knight

### User Story

As a player, **I want** to move a knight in an L shape, **so that** the knight can jump to its destination.

### Acceptance Criteria

1. A knight moves to a nearest square that is not on the same rank, file, or diagonal. That destination is two squares in one direction and then one square perpendicular.
2. The knight may jump over any pieces between its square and the destination.
3. The destination is empty or holds an opponent's piece.
4. From a corner the knight has two destinations. From a central square it has eight.

### Use Case 1: Move a knight

- **Actor:** Player to move
- **Preconditions:**
  - The player selects a knight, and the destination is one knight move away.
- **Main Flow:**
  1. The player clicks the destination square.
  2. The system moves the knight there, including when pieces stand between the two squares.
- **Alternate Flows:**
  - 1.a The destination is not a knight move. The system rejects the move as in US-03.
- **Postconditions:**
  - The knight is on the destination square.
  - Pieces it jumped over are unchanged.

---

## US-07: Move a bishop

**Board task:** Move a bishop

### User Story

As a player, **I want** to move a bishop any number of squares on a diagonal, **so that** the bishop stays on squares of one color and does not jump.

### Acceptance Criteria

1. A bishop may move any number of squares along a diagonal on which it stands, and no other way.
2. A bishop that starts on a light square can reach only light squares. A bishop that starts on a dark square can reach only dark squares.
3. Every square between the bishop and the destination is empty.
4. The destination is empty or holds an opponent's piece.

### Use Case 1: Move a bishop

- **Actor:** Player to move
- **Preconditions:**
  - The player selects a bishop, and the destination is on the same diagonal with an empty path.
- **Main Flow:**
  1. The player clicks the destination square.
  2. The system moves the bishop there.
- **Alternate Flows:**
  - 1.a The destination is off the bishop's diagonal, or a piece blocks the path. The system rejects the move as in US-03.
- **Postconditions:**
  - The bishop is on the destination square and still on its original color.

---

## US-08: Move a queen

**Board task:** Move a queen

### User Story

As a player, **I want** to move a queen any number of squares along a rank, file, or diagonal, **so that** the queen combines the rook's and the bishop's moves.

### Acceptance Criteria

1. A queen may move any number of squares along the rank, file, or diagonal it occupies.
2. Every square between the queen and the destination is empty.
3. The destination is empty or holds an opponent's piece.
4. A queen cannot jump, and it cannot move in a knight's L shape.

### Use Case 1: Move a queen

- **Actor:** Player to move
- **Preconditions:**
  - The player selects a queen, and the destination shares a rank, file, or diagonal with an empty path.
- **Main Flow:**
  1. The player clicks the destination square.
  2. The system moves the queen there.
- **Alternate Flows:**
  - 1.a The destination is not on the same rank, file, or diagonal, or a piece blocks the path. The system rejects the move as in US-03.
- **Postconditions:**
  - The queen is on the destination square.

---

## US-09: Move a king one square

**Board task:** Move a king one square

### User Story

As a player, **I want** to move my king to an adjoining square, **so that** the king can step one square in any direction.

### Acceptance Criteria

1. A king may move one square along a rank, file, or diagonal.
2. The destination is empty or holds an opponent's piece.
3. The destination is not attacked by an opponent's piece. US-14 decides whether the king would be in check.
4. Moving the king two squares is castling, handled by US-11, and is not a normal king move.

### Use Case 1: Move the king one square

- **Actor:** Player to move
- **Preconditions:**
  - The player selects their king, and the destination is one adjoining square where the king would not be in check.
- **Main Flow:**
  1. The player clicks the adjoining square.
  2. The system moves the king there.
- **Alternate Flows:**
  - 1.a The destination is more than one square away, other than a legal castle. The system rejects the move as in US-03.
- **Postconditions:**
  - The king is on the adjoining square.
  - The king has moved, so both castling rights for that player are lost.

---

## US-10: Capture an opponent's piece

**Board task:** Capture an opponent's piece

### User Story

As a player, **I want** to capture an opponent's piece by moving onto its square, **so that** the captured piece leaves the board as part of my move.

### Acceptance Criteria

1. A piece may move to a square occupied by an opponent's piece when that move is legal for the moving piece.
2. The opponent's piece is removed from the board as part of the same move.
3. The moving piece occupies that square.
4. A piece cannot move to a square occupied by a piece of its own color.
5. The king is never captured. The game ends by checkmate, US-15, before a king can be taken.
6. A capture resets the fifty-move count in US-19.

### Use Case 1: Capture a piece

- **Actor:** Player to move
- **Preconditions:**
  - An opponent's piece, other than the king, stands on a square the selected piece can legally reach.
- **Main Flow:**
  1. The player clicks the occupied square.
  2. The system removes the opponent's piece.
  3. The system places the moving piece on that square and passes the turn, unless the move ends the game.
- **Alternate Flows:**
  - 1.a The square holds a piece of the same color. The system rejects the move as in US-03.
- **Postconditions:**
  - The captured piece is off the board.
  - The mover occupies that square.

---

## US-11: Castle

**Board task:** Castle

### User Story

As a player, **I want** to castle, **so that** I can move my king and one rook in a single king move.

### Acceptance Criteria

1. Castling is one move. The player moves the king two squares toward a rook. The system then places that rook on the square the king crossed.
2. White kingside: king from e1 to g1, rook from h1 to f1. White queenside: king from e1 to c1, rook from a1 to d1.
3. Black kingside: king from e8 to g8, rook from h8 to f8. Black queenside: king from e8 to c8, rook from a8 to d8.
4. The king and the rook involved are both on their original squares and neither has moved. A king or rook that has moved cannot castle, even after returning to its original square. Losing one rook's right does not remove the other rook's right. Once the king has moved, both rights are lost.
5. Every square between the king and that rook is empty.
6. The square the king stands on, the square it crosses, and the square it lands on are not attacked. The king cannot castle out of check, through check, or into check.
7. An attack on the rook, or on b1 or b8, does not by itself prevent castling. The king does not cross or land on b1 or b8.
8. The move is recorded as a king move. It is not a capture, so it counts toward the fifty-move rule.

### Use Case 1: Castle

- **Actor:** Player to move
- **Preconditions:**
  - The player's king and the chosen rook have never moved.
  - The squares between them are empty.
  - The king is not in check, and the king would not cross or land on an attacked square.
- **Main Flow:**
  1. The player selects the king.
  2. The player clicks the square two steps toward the chosen rook.
  3. The system moves the king to that square and moves the rook to the square the king crossed.
  4. The system passes the turn, unless the move ends the game.
- **Alternate Flows:**
  - 2.a The king has moved, the chosen rook has moved or is gone, a piece stands between them, or the king's path is attacked. The system rejects the move as in US-03.
- **Postconditions:**
  - The king and that rook are on their castled squares.
  - That player can no longer castle on either side.

---

## US-12: Capture en passant

**Board task:** Capture en passant

### User Story

As a player, **I want** to capture en passant, **so that** my pawn can take an opponent's pawn that has just advanced two squares and landed beside it.

### Acceptance Criteria

1. An en passant capture is legal only on the move immediately after an opponent's pawn advances two squares from its starting square.
2. The capturing pawn stands on the same rank as, and on a file adjacent to, the pawn that just advanced.
3. The capturing pawn moves diagonally forward to the square the opponent's pawn passed over. That square is empty.
4. The opponent's pawn is removed from the board.
5. Any other move removes the chance to capture that pawn en passant.
6. The capture counts as a capture for the fifty-move rule.

### Use Case 1: Capture en passant

- **Actor:** Player to move
- **Preconditions:**
  - The opponent's previous move was a two-square pawn advance from that pawn's starting square.
  - The player has a pawn on an adjacent file of the same rank.
- **Main Flow:**
  1. The player selects that pawn.
  2. The player clicks the empty square directly behind the opponent's pawn.
  3. The system moves the player's pawn to that square and removes the opponent's pawn.
- **Alternate Flows:**
  - 2.a The opponent's last move was not that two-square advance, or another move has already been played. The system rejects the move as in US-03.
- **Postconditions:**
  - The capturing pawn is on the square the opponent's pawn passed through.
  - The captured pawn is off the board.

---

## US-13: Promote a pawn

**Board task:** Promote a pawn

### User Story

As a player, **I want** to promote a pawn that reaches the last rank, **so that** the pawn becomes a queen, rook, bishop, or knight of my color as part of the same move.

### Acceptance Criteria

1. A white pawn that moves to rank 8, or a black pawn that moves to rank 1, is promoted as part of that move.
2. The player must choose a queen, rook, bishop, or knight of the same color. A king and a pawn are not choices.
3. The choice is not limited to pieces that have already been captured.
4. The system asks for the choice before the turn passes. The move is unfinished until the player chooses.
5. The pawn is removed and the chosen piece stands on the arrival square. Its effect is immediate, so it can give check on that move.
6. Promotion can be a one-square advance, a capture on the last rank, or an en passant capture that lands on the last rank.
7. A promotion is a pawn move, so it resets the fifty-move count.

### Use Case 1: Promote a pawn

- **Actor:** Player to move
- **Preconditions:**
  - The selected pawn has a legal move onto the furthest rank.
- **Main Flow:**
  1. The player clicks the arrival square on the furthest rank.
  2. The system asks the player to choose a queen, rook, bishop, or knight.
  3. The player chooses one of those pieces.
  4. The system removes the pawn, places the chosen piece on the arrival square, and passes the turn, unless the move ends the game.
- **Alternate Flows:**
  - 3.a The player has not chosen yet. The system keeps the choice open and does not pass the turn.
- **Postconditions:**
  - The pawn is off the board.
  - The chosen piece occupies the arrival square.

---

## US-14: Keep the king out of check

**Board task:** Keep the king out of check

### User Story

As a player, **I want** moves that leave my king in check to be refused, **so that** my king is never left under attack.

### Acceptance Criteria

1. A king is in check when one or more opponent pieces attack its square. A piece still gives check when that piece itself could not move because doing so would expose its own king.
2. A move that leaves or places the mover's own king in check is illegal. This includes moving a pinned piece off the line between the attacker and the king.
3. When the king is in check, the only legal moves are those that end the check: move the king to a safe square, capture the checking piece, or block a check from a bishop, rook, or queen.
4. When two or more pieces give check, the only legal reply is a king move that escapes all of them.
5. While the king is in check and the game has not ended, the system shows that the king is in check.
6. A legal move may give check to the opponent. The opponent is then the player in check.

### Use Case 1: Get out of check

- **Actor:** Player to move
- **Preconditions:**
  - The player's king is in check, and the player has at least one legal move.
- **Main Flow:**
  1. The player selects a piece and a destination that leaves their king unattacked.
  2. The system makes the move and clears the check indication, unless the move also checks the opponent.
  3. The system gives the turn to the opponent.
- **Alternate Flows:**
  - 1.a The chosen move would still leave the king in check. The system rejects it as in US-03.
  - 1.b Two pieces give check, and the player tries to block one check or capture only one checker. The system rejects the move unless the king itself moves to a safe square.
- **Postconditions:**
  - The player's king is not in check.
  - It is the opponent's turn.

### Use Case 2: Give check

- **Actor:** Player to move
- **Preconditions:**
  - The player has a legal move that attacks the opponent's king and does not leave their own king in check.
- **Main Flow:**
  1. The player makes that move.
  2. The system marks the opponent's king as in check and gives the opponent the turn, unless the move is checkmate.
- **Postconditions:**
  - The opponent's king is in check.
  - It is the opponent's turn, unless the game has ended.

---

## US-15: Win by checkmate

**Board task:** Win by checkmate

### User Story

As a player, **I want** the game to end when I checkmate the opponent's king, **so that** I win when the opponent is in check and has no legal move.

### Acceptance Criteria

1. Checkmate occurs when the player to move is in check and has no legal move.
2. The player who made the mating move wins. The checkmated player loses.
3. The game ends immediately. Further moves, resignation, and draw offers are refused.
4. The main window shows the winner's name and color, and that the result is checkmate.
5. Checkmate takes priority over a draw by repetition or by the seventy-five-move rule.
6. The king is not removed from the board.

### Use Case 1: Deliver checkmate

- **Actor:** Player to move
- **Preconditions:**
  - The player has a legal move that leaves the opponent in check with no legal reply.
- **Main Flow:**
  1. The player makes that move.
  2. The system recognizes that the opponent is in check and has no legal move.
  3. The system ends the game and shows that this player wins by checkmate.
- **Postconditions:**
  - The game is over.
  - The mating player has won.

---

## US-16: Draw by stalemate

**Board task:** Draw by stalemate

### User Story

As a player, **I want** the game to be drawn when the player to move is not in check and has no legal move, **so that** a stuck player is not checkmated.

### Acceptance Criteria

1. Stalemate occurs when the player to move is not in check and has no legal move.
2. The game ends immediately as a draw.
3. A position in which the player to move is in check is checkmate, US-15, and is not stalemate.
4. The main window shows that the game is a draw by stalemate.
5. Further moves are refused.

### Use Case 1: Leave the opponent in stalemate

- **Actor:** Player to move
- **Preconditions:**
  - The player has a legal move that leaves the opponent with no legal move and does not check the opponent's king.
- **Main Flow:**
  1. The player makes that move.
  2. The system recognizes that the opponent is not in check and has no legal move.
  3. The system ends the game and shows a draw by stalemate.
- **Postconditions:**
  - The game is over.
  - The result is a draw.

---

## US-17: Draw by dead position

**Board task:** Draw by dead position

### User Story

As a player, **I want** the game to be drawn when neither king can possibly be checkmated, **so that** the game ends as soon as checkmate is impossible.

### Acceptance Criteria

1. The game ends in a draw immediately when neither player can checkmate the other by any series of legal moves.
2. The system treats these positions as dead, whatever the squares of the pieces:
   - King against king.
   - King and bishop against king.
   - King and knight against king.
   - King and bishop against king and bishop, with both bishops on squares of the same color.
3. King and two knights against a king is not a dead position. Checkmate is possible, so the game continues.
4. King and queen, king and rook, or king and pawn against a lone king is not a dead position.
5. The main window shows a draw by dead position, and further moves are refused.

This task covers the material positions above. Other dead positions, such as a blocked pawn wall, are outside this task.

### Use Case 1: Reach a dead position

- **Actor:** Player to move
- **Preconditions:**
  - The player has a legal move that leaves only one of the material balances in criterion 2.
- **Main Flow:**
  1. The player makes that move.
  2. The system recognizes the dead position.
  3. The system ends the game and shows a draw.
- **Postconditions:**
  - The game is over.
  - The result is a draw.

---

## US-18: Draw by repetition

**Board task:** Draw by repetition

### User Story

As a player, **I want** a repeated position to be drawn under the repetition rules, **so that** the same position cannot continue forever.

### Acceptance Criteria

1. Two positions are the same only when the same player is to move, the same pieces occupy the same squares, both players have the same castling rights, and the same en passant capture, if any, is available.
2. Castling rights in that comparison are the rights that still exist. A right disappears only after that king or that rook moves.
3. When it is a player's turn and the current position has already occurred at least twice before, that player may claim a draw. The game is drawn immediately.
4. A player who has selected a legal move that would produce the position for the third time may claim a draw before confirming the move. The game is drawn immediately.
5. The claim is offered only when it is correct.
6. When the same position has occurred five times, the game is drawn immediately, with no claim.
7. Checkmate still wins if the move that reached the position checkmated the king.
8. The main window shows a draw by repetition, and further moves are refused.

### Use Case 1: Claim a threefold repetition

- **Actor:** Player to move
- **Preconditions:**
  - The current position has occurred at least three times under criterion 1, counting the current occurrence.
- **Main Flow:**
  1. The player chooses Claim Draw.
  2. The system confirms that the claim is correct.
  3. The system ends the game and shows a draw by repetition.
- **Alternate Flows:**
  - 1.a The player first selects a move that would make the position occur for the third time, then chooses Claim Draw. The system draws the game.
- **Postconditions:**
  - The game is over.
  - The result is a draw.

### Use Case 2: Reach the same position five times

- **Actor:** Player to move
- **Preconditions:**
  - The player's legal move will make this the fifth occurrence of the resulting position, and the move is not checkmate.
- **Main Flow:**
  1. The player makes the move.
  2. The system ends the game as a draw without waiting for a claim.
- **Postconditions:**
  - The game is over.
  - The result is a draw.

---

## US-19: Draw by the fifty-move rule

**Board task:** Draw by the fifty-move rule

### User Story

As a player, **I want** the fifty-move and seventy-five-move rules enforced, **so that** a game with no pawn move and no capture eventually ends as a draw.

### Acceptance Criteria

1. The system counts half-moves since the last pawn move or capture. The count starts at 0.
2. A move that is not a pawn move and not a capture adds one. Castling adds one.
3. A pawn move, a normal capture, an en passant capture, or a promotion resets the count to 0.
4. When both players have completed 50 such moves (the count is 100) and it is a player's turn, that player may claim a draw. The game is drawn immediately.
5. A player who has selected the move that would bring the count to 100 may claim a draw before confirming it. The game is drawn immediately.
6. The claim is offered only when it is correct.
7. When both players have completed 75 such moves (the count reaches 150), the game is drawn immediately, with no claim.
8. If the move that reaches 150 checkmates the opponent, the mover wins by checkmate instead.
9. The main window shows a draw by the fifty-move rule or by the seventy-five-move rule, and further moves are refused.

### Use Case 1: Claim a draw after 50 moves each

- **Actor:** Player to move
- **Preconditions:**
  - The half-move count is 100, or the selected move would make it 100.
  - That move is not a pawn move or a capture.
- **Main Flow:**
  1. The player chooses Claim Draw.
  2. The system confirms that the claim is correct.
  3. The system ends the game and shows a draw.
- **Postconditions:**
  - The game is over.
  - The result is a draw.

### Use Case 2: Reach 75 moves each

- **Actor:** Player to move
- **Preconditions:**
  - The selected move brings the half-move count to 150 and does not checkmate the opponent.
- **Main Flow:**
  1. The player makes the move.
  2. The system ends the game as a draw without waiting for a claim.
- **Alternate Flows:**
  - 1.a The move checkmates the opponent. The system ends the game as a win, as in US-15.
- **Postconditions:**
  - The game is over.
  - The result is a draw, unless the move was checkmate.

---

## US-20: Resign

**Board task:** Resign

### User Story

As a player, **I want** to resign, **so that** I can end a lost game and give the win to my opponent.

### Acceptance Criteria

1. While the game is in progress, either player may resign.
2. The resigning player loses, the opponent wins, and the game ends immediately.
3. If the position is already a dead position under US-17, the game has already ended as a draw, so resignation is refused.
4. The main window shows the opponent's name and color as the winner by resignation.
5. Further moves are refused.

### Use Case 1: Resign

- **Actor:** Either player
- **Preconditions:**
  - The game is in progress and the position is not a dead position.
- **Main Flow:**
  1. The player chooses Resign.
  2. The system ends the game.
  3. The system shows that the opponent wins.
- **Alternate Flows:**
  - 1.a The game is already over. The system ignores the resignation.
- **Postconditions:**
  - The game is over.
  - The resigning player has lost.

---

## US-21: Agree to a draw

**Board task:** Agree to a draw

### User Story

As a player, **I want** to offer a draw and have my opponent accept or decline it, **so that** we can agree to end the game as a draw.

### Acceptance Criteria

1. A draw can be agreed only after both players have made at least one move, and only while the game is in progress.
2. Either player may offer a draw. No conditions can be attached to the offer.
3. The offer cannot be withdrawn. It stays until the opponent accepts it, declines it, makes a move, or the game ends for another reason.
4. If the opponent accepts, the game ends immediately as a draw.
5. If the opponent declines or makes a move, the offer ends and the game continues.
6. Only one offer is pending at a time.
7. The main window shows a draw by agreement when the offer is accepted, and further moves are refused.

### Use Case 1: Agree to a draw

- **Actor:** Either player
- **Preconditions:**
  - Both players have completed at least one move.
  - The game is in progress.
- **Main Flow:**
  1. A player chooses Offer Draw.
  2. The system shows the offer to the opponent.
  3. The opponent chooses Accept.
  4. The system ends the game and shows a draw by agreement.
- **Alternate Flows:**
  - 1.a A player offers a draw before both players have moved. The system refuses the offer.
  - 3.a The opponent chooses Decline, or makes a move. The system removes the offer and the game continues.
- **Postconditions:**
  - If the offer was accepted, the game is over and the result is a draw.
  - If the offer was declined, the board is unchanged by the offer itself.
