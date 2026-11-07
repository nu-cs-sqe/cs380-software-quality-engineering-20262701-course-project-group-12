# BVA Analysis for King

`King` is the piece that steps one square along a rank, file, or
diagonal (issue #17 / GitHub US-07). It extends `Piece`. This analysis
covers `canMove`. The constructor only stores `PieceType.KING` and the
color through `Piece`, so it has no cases of its own.

`canMove` is a query. It does not move the king. `Board.move` still
places the piece. Occupancy comes from a real `Board` (this team owns
`Board`, and `Location` has no `equals`, so EasyMock cannot match the
squares `King` creates). Tests rearrange the starting position with
`Board.move` when a square has to be empty or occupied.

`canMove(Location from, Location to, Board board)` returns `true` when
`to` is one adjoining square (along the rank, the file, or a diagonal)
and is empty or holds an opponent's piece. It returns `false` for any
other destination.

Squares use file `0` = a-file through file `7` = h-file, and rank `1`
through `8`.

Out of scope: whether the destination is attacked (US-11) and castling
(US-12). A two-square destination is rejected as a normal king move.
US-12 will decide when two squares along the rank is a castle.

## Week 6 instructor review

- **No null checks.** This is a standalone Java application, so the code
  makes sure `null` is never passed around instead of checking for it.
  `canMove` has no null cases and does not throw for a null argument.
- **No magic numbers.** The one-square king step and the two-square
  distance that is not a normal king move become named constants in
  `King` and in the tests.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: `from`**
  - e1 `(4, 1)`, the white king on its starting square
  - e4 `(4, 4)`, the white king moved to the center, where every
    adjoining square is on the board
  - e8 `(4, 8)`, the black king on its starting square
- **Input: `to`**
  - the same square as `from`
  - one square along the file, toward rank 8 and toward rank 1
  - one square along the rank, toward file 7 and toward file 0
  - one square along each of the four diagonals
  - two squares along the file
  - two squares along the rank (the castling distance from e1 toward
    the h-file)
  - two squares along a diagonal
  - two squares in one direction and one in the other
  - four squares along the rank, the far edge from e4
- **Input: `board`**
  - a board whose occupancy matches the case (no null: callers never
    pass one)
- **Input: occupant of `to`**
  - empty
  - an opponent's piece
  - a piece of the king's color
- **Input: king color**
  - white
  - black
- **Output: allowed move**
  - `true`
- **Output: rejected destination**
  - `false`

### Step 2: Categories

- An adjoining square is a case: one step along the file, one step
  along the rank, or one step along a diagonal. Each of those has two
  directions, and the diagonal has four, so each of the eight adjoining
  squares appears once.
- The Chebyshev distance `max(|file2 - file1|, |rank2 - rank1|)` is an
  interval `[1, 1]`. `0` is the same square and is not a move. `2` is
  just above the interval, including the two-square castling distance.
  From e4 the far edge on the board is `4`.
- How a destination sits at distance `2` is a case: along the file,
  along the rank, along a diagonal, or two in one direction and one in
  the other.
- The destination occupant is a case: empty, opponent, same color.
- King color does not change the geometry, so each color appears in at
  least one case.
- `Location` already forbids squares off the board, so there is no case
  for a step off the edge.
- Two invalid inputs are never paired. A rejected distance has an empty
  destination, and an occupied destination is one square away.

### Step 3: Boundary choices

- One square onto an empty square in each adjoining direction. The
  white king is on e4 after `move` from e1, so the eight neighbors are
  empty except that the black king's one-square case uses down the
  file:
  - up the file, white e4 to e5 `(4, 5)`
  - down the file, black e8 to e7 `(4, 7)` after `move` of the e7 pawn
  - toward the h-file, white e4 to f4 `(5, 4)`
  - toward the a-file, white e4 to d4 `(3, 4)`
  - up and right, white e4 to f5 `(5, 5)`
  - up and left, white e4 to d5 `(3, 5)`
  - down and right, white e4 to f3 `(5, 3)`
  - down and left, white e4 to d3 `(3, 3)`
- The same square: e1 to e1.
- Two squares along the file onto an empty square: e4 to e6 `(4, 6)`.
- Two squares along the rank onto an empty square (castling distance):
  e1 to g1 `(6, 1)` after `move` of the g1 bishop.
- Two squares along a diagonal onto an empty square: e4 to g6 `(6, 6)`.
- Two squares in one direction and one in the other onto an empty
  square: e4 to f6 `(5, 6)`.
- Four squares along the rank onto an empty square: e4 to a4 `(0, 4)`.
- Adjacent own piece: e1 to e2 `(4, 2)`, the white e-pawn on the
  starting board.
- Adjacent opponent: e1 to e2 holding a black pawn.

## Step 4: Test Cases

### Method under test: `canMove(Location from, Location to, Board board)`

- **TC-KING-01 — Move one square up the file onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king. `from` is e4 `(4, 4)`. `to` is
    e5 `(4, 5)`. The board is a new `Board` after `move` of the king
    from e1 `(4, 1)` to e4, so e5 is empty.
  - **Expected output**: `true`.

- **TC-KING-02 — Move a black king one square down the file onto an
  empty square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black king on e8 `(4, 8)`. `to` is e7
    `(4, 7)`. The board is a new `Board` after `move` of the e7 pawn to
    e5 `(4, 5)`, so e7 is empty.
  - **Expected output**: `true`.

- **TC-KING-03 — Move one square toward the h-file onto an empty
  square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is f4
    `(5, 4)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so f4 is empty.
  - **Expected output**: `true`.

- **TC-KING-04 — Move one square toward the a-file onto an empty
  square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is d4
    `(3, 4)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so d4 is empty.
  - **Expected output**: `true`.

- **TC-KING-05 — Move one square up and right onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is f5
    `(5, 5)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so f5 is empty.
  - **Expected output**: `true`.

- **TC-KING-06 — Move one square up and left onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is d5
    `(3, 5)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so d5 is empty.
  - **Expected output**: `true`.

- **TC-KING-07 — Move one square down and right onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is f3
    `(5, 3)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so f3 is empty.
  - **Expected output**: `true`.

- **TC-KING-08 — Move one square down and left onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is d3
    `(3, 3)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so d3 is empty.
  - **Expected output**: `true`.

- **TC-KING-09 — Reject the square the king already occupies** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king. `from` and `to` are both e1
    `(4, 1)`. New `Board`.
  - **Expected output**: `false`.

- **TC-KING-10 — Reject two squares along the file** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is e6
    `(4, 6)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so e6 is empty.
  - **Expected output**: `false`.

- **TC-KING-11 — Reject two squares along the rank** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e1 `(4, 1)`. `to` is g1
    `(6, 1)`, two squares toward the h-file (castling distance). The
    board is a new `Board` after `move` of the g1 bishop to g3 `(6, 3)`,
    so g1 is empty.
  - **Expected output**: `false`.

- **TC-KING-12 — Reject two squares along a diagonal** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is g6
    `(6, 6)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so g6 is empty.
  - **Expected output**: `false`.

- **TC-KING-13 — Reject two squares in one direction and one in the
  other** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is f6
    `(5, 6)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so f6 is empty.
  - **Expected output**: `false`.

- **TC-KING-14 — Reject a far square along the rank** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is a4
    `(0, 4)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so a4 is empty.
  - **Expected output**: `false`.

- **TC-KING-15 — Reject an adjacent piece of the king's color** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king. `from` is e1 `(4, 1)`. `to` is
    e2 `(4, 2)`. New `Board`; e2 holds the white e-pawn.
  - **Expected output**: `false`.

- **TC-KING-16 — Capture an adjacent opponent** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king. `from` is e1 `(4, 1)`. `to` is
    e2 `(4, 2)`. The board is a new `Board` after `move` of the e2 pawn
    to e4 `(4, 4)` and `move` of the e7 pawn to e2, so e2 holds a black
    pawn.
  - **Expected output**: `true`.
