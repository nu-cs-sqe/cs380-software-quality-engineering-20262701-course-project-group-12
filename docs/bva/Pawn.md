# BVA Analysis for Pawn

`Pawn` is the piece that advances straight toward the opponent and
captures only one square diagonally forward (issue #16 / GitHub US-06,
docs US-04). It extends `Piece`. This analysis covers `canMove`. The
constructor only stores `PieceType.PAWN` and the color through `Piece`,
so it has no cases of its own.

`canMove` is a query. It does not move the pawn. `Board.move` still
places the piece. Occupancy comes from a real `Board` (this team owns
`Board`, and `Location` has no `equals`, so EasyMock cannot match the
squares `Pawn` creates). Tests rearrange the starting position with
`Board.move` when a square has to be empty or occupied.

`canMove(Location from, Location to, Board board)` returns `true` when
one of these holds, and `false` for any other destination:

- `to` is one square straight forward and empty.
- `from` is on the pawn's starting rank, `to` is two squares straight
  forward, and both of those squares are empty.
- `to` is one square diagonally forward and holds an opponent's piece.
- `to` is one square diagonally forward and empty, and the square beside
  `from` on the file of `to` holds an opponent's pawn that
  `Board.canBeCapturedEnPassant` reports (issue #47 / GitHub US-14, docs
  US-12).

Forward is toward rank 8 for a white pawn and toward rank 1 for a black
pawn. The starting rank is rank 2 for White and rank 7 for Black.

Squares use file `0` = a-file through file `7` = h-file, and rank `1`
through `8`.

Out of scope: promotion on the last rank (US-13). A move onto the last
rank is an ordinary forward move here. A diagonal move onto an empty
square is rejected unless it captures en passant.

## Week 6 instructor review

- **No null checks.** This is a standalone Java application, so the code
  makes sure `null` is never passed around instead of checking for it.
  `canMove` has no null cases and does not throw for a null argument.
- **No magic numbers.** The starting ranks, the forward direction of each
  color, and the one- and two-square distances become named constants in
  `Pawn` and in the tests.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: `from`**
  - e2 `(4, 2)`, a white pawn on its starting rank
  - e3 `(4, 3)`, a white pawn one rank past its starting rank
  - e4 `(4, 4)`, a white pawn further up the board
  - a2 `(0, 2)` and h2 `(7, 2)`, white pawns on the edge files
  - a7 `(0, 7)`, a white pawn one rank before the last rank
  - e7 `(4, 7)`, a black pawn on its starting rank
  - e6 `(4, 6)`, a black pawn one rank past its starting rank
  - e5 `(4, 5)`, a black pawn further down the board
  - d7 `(3, 7)`, a black pawn that captures
  - e5 `(4, 5)` and f4 `(5, 4)`, white pawns beside a pawn that just
    moved two ranks
  - d4 `(3, 4)`, a black pawn beside a pawn that just moved two ranks
- **Input: `to`**
  - the same square as `from`
  - one square straight forward
  - two squares straight forward
  - three squares straight forward
  - one square straight backward
  - one square sideways
  - one square diagonally forward, toward a higher file and toward a
    lower file
  - one square diagonally backward
  - two squares diagonally forward
  - one rank forward and two files over
- **Input: `board`**
  - a board whose occupancy matches the case
- **Input: occupant of `to`**
  - empty
  - an opponent's piece
  - a piece of the pawn's color
- **Input: occupant of the square passed over in a two-square advance**
  - empty
  - a piece
- **Input: the square beside `from`, on the file of `to`, for a diagonal
  move onto an empty square**
  - an opponent's pawn that just moved two ranks
  - an opponent's pawn that reached the square with two one-rank moves
  - a pawn of the pawn's color that just moved two ranks
- **Input: moves after the two-rank move**
  - none
  - one other move
- **Input: pawn color**
  - white
  - black
- **Output: allowed move**
  - `true`
- **Output: rejected destination**
  - `false`

### Step 2: Categories

- The direction of a move is a case: straight forward, straight
  backward, sideways, diagonally forward, diagonally backward, or none
  (the same square). Forward depends on the pawn's color, so forward
  and backward are checked for both colors.
- The forward distance of a straight move is an interval. It is `[1, 1]`
  off the starting rank and `[1, 2]` on it. The values are `0` (the
  same square), `1`, `2`, and `3`, just above the starting-rank interval.
- The rank of `from` for a two-square advance is a boundary: the
  starting rank allows it, and one rank past the starting rank does
  not. Both colors are checked, because the starting rank differs.
- The rank of `to` has a high edge. A white pawn may move one square
  onto rank 8. The promotion that follows is US-13.
- The file of `from` has two edges. A pawn on the a-file can capture only
  toward a higher file, and a pawn on the h-file only toward a lower
  file, so those two captures cover both diagonal directions and both
  file edges.
- The file distance of a capture is an interval `[1, 1]`. The values are
  `1`, a diagonal square, and `2`, just above it on the next rank.
- The occupant of `to` is a case: empty, opponent, same color. A straight
  move needs empty, and a diagonal move needs an opponent.
- The square passed over in a two-square advance is a collection of one
  square. It is either empty or holds a piece.
- Two invalid inputs are never paired. A blocked two-square advance has
  an empty destination, and a rejected direction has an empty
  destination unless the case is about the occupant.
- The pawn beside `from` in an en passant capture is a case: an
  opponent's pawn that can be captured en passant, an opponent's pawn
  that cannot because it moved one rank at a time, and a pawn of the
  pawn's own color.
- The chance to capture en passant lasts one move, so the moves after
  the two-rank move have two values: none, and one other move.
- Color sets the direction, so an en passant capture is checked for
  both colors: toward a lower file for White and toward a higher file
  for Black.
- `Board.canBeCapturedEnPassant` answers for the last move made with
  `Board.move`, so each en passant case sets up the board with `move` in
  the order the game would.

### Step 3: Boundary choices

- One square forward onto an empty square: white e2 to e3 `(4, 3)`,
  black e7 to e6 `(4, 6)`.
- Two squares forward from the starting rank onto an empty square: white
  e2 to e4 `(4, 4)`, black e7 to e5 `(4, 5)`.
- Capture toward a higher file from the a-file: white a2 to b3 `(1, 3)`.
- Capture toward a lower file from the h-file: white h2 to g3 `(6, 3)`.
- Capture by a black pawn: d7 to e6 `(4, 6)`.
- One square forward onto the last rank: white a7 to a8 `(0, 8)`.
- The same square: e2 to e2.
- One square backward: white e4 to e3; black e5 to e6.
- One square sideways: white e4 to f4 `(5, 4)`.
- Two squares forward one rank past the starting rank: white e3 to e5
  `(4, 5)`; black e6 to e4 `(4, 4)`.
- Three squares forward from the starting rank: white e2 to e5.
- One square forward onto an opponent: white e2 to e3 holding a black
  pawn.
- One square forward onto an own piece: white e2 to e3 holding the white
  g1 knight.
- Two squares forward with the passed-over square occupied: white e2 to
  e4 with the white g1 knight on e3.
- Two squares forward onto an opponent: white e2 to e4 holding a black
  pawn, with e3 empty.
- One square diagonally forward onto an empty square: white e2 to f3
  `(5, 3)`.
- One square diagonally forward onto an own piece: white e2 to f3 holding
  the white g1 knight.
- One square diagonally backward onto an opponent: white e4 to d3
  `(3, 3)` holding a black pawn.
- Two squares diagonally forward onto an opponent: white e2 to g4
  `(6, 4)` holding a black pawn.
- One rank forward and two files over onto an opponent: white e2 to g3
  `(6, 3)` holding a black pawn.
- En passant by a white pawn: e5 to d6 `(3, 6)` right after the black d7
  pawn moves to d5 `(3, 5)`.
- En passant by a black pawn: d4 to e3 `(4, 3)` right after the white e2
  pawn moves to e4.
- En passant one move too late: the white case, with the g1 knight moving
  to f3 after the black pawn.
- Beside an opponent's pawn that moved one rank at a time: white e5 to d6
  after the black d7 pawn moves to d6 and then d5.
- Beside a pawn of the pawn's color: white f4 to e5 right after the white
  e2 pawn moves to e4.

## Step 4: Test Cases

### Method under test: `canMove(Location from, Location to, Board board)`

- **TC-PAWN-01 — Move a white pawn one square forward onto an empty
  square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    e3 `(4, 3)`. New `Board`; e3 is empty.
  - **Expected output**: `true`.

- **TC-PAWN-02 — Move a white pawn two squares forward from its starting
  rank** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    e4 `(4, 4)`. New `Board`; e3 and e4 are empty.
  - **Expected output**: `true`.

- **TC-PAWN-03 — Move a black pawn one square forward onto an empty
  square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black pawn. `from` is e7 `(4, 7)`. `to` is
    e6 `(4, 6)`. New `Board`; e6 is empty.
  - **Expected output**: `true`.

- **TC-PAWN-04 — Move a black pawn two squares forward from its starting
  rank** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black pawn. `from` is e7 `(4, 7)`. `to` is
    e5 `(4, 5)`. New `Board`; e6 and e5 are empty.
  - **Expected output**: `true`.

- **TC-PAWN-05 — Capture toward a higher file from the a-file** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is a2 `(0, 2)`. `to` is
    b3 `(1, 3)`. New `Board` after `move` of the b7 pawn to b3, so b3
    holds a black pawn.
  - **Expected output**: `true`.

- **TC-PAWN-06 — Capture toward a lower file from the h-file** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is h2 `(7, 2)`. `to` is
    g3 `(6, 3)`. New `Board` after `move` of the g7 pawn to g3, so g3
    holds a black pawn.
  - **Expected output**: `true`.

- **TC-PAWN-07 — Capture with a black pawn** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black pawn. `from` is d7 `(3, 7)`. `to` is
    e6 `(4, 6)`. New `Board` after `move` of the e2 pawn to e6, so e6
    holds a white pawn.
  - **Expected output**: `true`.

- **TC-PAWN-08 — Move one square forward onto the last rank** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is a7 `(0, 7)`. `to` is
    a8 `(0, 8)`. New `Board` after `move` of the a2 pawn to a7 and the
    a8 rook to a5 `(0, 5)`, so a7 holds a white pawn and a8 is empty.
  - **Expected output**: `true`.

- **TC-PAWN-09 — Reject the square the pawn already occupies** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` and `to` are both e2
    `(4, 2)`. New `Board`.
  - **Expected output**: `false`.

- **TC-PAWN-10 — Reject a white pawn moving one square backward** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e4 `(4, 4)`. `to` is
    e3 `(4, 3)`. New `Board` after `move` of the e2 pawn to e4, so e3 is
    empty.
  - **Expected output**: `false`.

- **TC-PAWN-11 — Reject a black pawn moving one square backward**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black pawn. `from` is e5 `(4, 5)`. `to` is
    e6 `(4, 6)`. New `Board` after `move` of the e7 pawn to e5, so e6 is
    empty.
  - **Expected output**: `false`.

- **TC-PAWN-12 — Reject a pawn moving one square sideways** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e4 `(4, 4)`. `to` is
    f4 `(5, 4)`. New `Board` after `move` of the e2 pawn to e4; f4 is
    empty.
  - **Expected output**: `false`.

- **TC-PAWN-13 — Reject a white two-square advance off the starting
  rank** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e3 `(4, 3)`. `to` is
    e5 `(4, 5)`. New `Board` after `move` of the e2 pawn to e3, so e4 and
    e5 are empty.
  - **Expected output**: `false`.

- **TC-PAWN-14 — Reject a black two-square advance off the starting
  rank** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black pawn. `from` is e6 `(4, 6)`. `to` is
    e4 `(4, 4)`. New `Board` after `move` of the e7 pawn to e6, so e5 and
    e4 are empty.
  - **Expected output**: `false`.

- **TC-PAWN-15 — Reject a three-square advance from the starting rank**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    e5 `(4, 5)`. New `Board`; e3, e4, and e5 are empty.
  - **Expected output**: `false`.

- **TC-PAWN-16 — Reject a straight capture one square forward** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    e3 `(4, 3)`. New `Board` after `move` of the e7 pawn to e3, so e3
    holds a black pawn.
  - **Expected output**: `false`.

- **TC-PAWN-17 — Reject one square forward onto a piece of the pawn's
  color** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    e3 `(4, 3)`. New `Board` after `move` of the g1 knight to e3, so e3
    holds a white knight.
  - **Expected output**: `false`.

- **TC-PAWN-18 — Reject a two-square advance that jumps a piece**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    e4 `(4, 4)`. New `Board` after `move` of the g1 knight to e3, so e3
    holds a white knight and e4 is empty.
  - **Expected output**: `false`.

- **TC-PAWN-19 — Reject a two-square advance that captures** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    e4 `(4, 4)`. New `Board` after `move` of the e7 pawn to e4, so e3 is
    empty and e4 holds a black pawn.
  - **Expected output**: `false`.

- **TC-PAWN-20 — Reject a diagonal move onto an empty square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    f3 `(5, 3)`. New `Board`; f3 is empty.
  - **Expected output**: `false`.

- **TC-PAWN-21 — Reject a diagonal move onto a piece of the pawn's
  color** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    f3 `(5, 3)`. New `Board` after `move` of the g1 knight to f3, so f3
    holds a white knight.
  - **Expected output**: `false`.

- **TC-PAWN-22 — Reject a diagonal capture backward** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e4 `(4, 4)`. `to` is
    d3 `(3, 3)`. New `Board` after `move` of the e2 pawn to e4 and the d7
    pawn to d3, so d3 holds a black pawn.
  - **Expected output**: `false`.

- **TC-PAWN-23 — Reject a capture two squares diagonally forward**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    g4 `(6, 4)`. New `Board` after `move` of the g7 pawn to g4, so g4
    holds a black pawn and f3 is empty.
  - **Expected output**: `false`.

- **TC-PAWN-24 — Reject a capture one rank forward and two files over**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`. `to` is
    g3 `(6, 3)`. New `Board` after `move` of the g7 pawn to g3, so g3
    holds a black pawn.
  - **Expected output**: `false`.

The en passant cases start at TC-PAWN-29, after the promotion cases
(TC-PAWN-25 to TC-PAWN-28, issue #46).

- **TC-PAWN-29 — Capture en passant with a white pawn** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e5 `(4, 5)`. `to` is
    d6 `(3, 6)`. New `Board` after `move` of the e2 pawn to e4, e4 to e5,
    and the d7 pawn to d5 `(3, 5)`, so d6 is empty and d5 holds a black
    pawn that just moved two ranks.
  - **Expected output**: `true`.

- **TC-PAWN-30 — Capture en passant with a black pawn** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black pawn. `from` is d4 `(3, 4)`. `to` is
    e3 `(4, 3)`. New `Board` after `move` of the d7 pawn to d5, d5 to d4,
    and the e2 pawn to e4 `(4, 4)`, so e3 is empty and e4 holds a white
    pawn that just moved two ranks.
  - **Expected output**: `true`.

- **TC-PAWN-31 — Reject en passant one move too late** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e5 `(4, 5)`. `to` is
    d6 `(3, 6)`. The board of TC-PAWN-29, then `move` of the g1 knight
    to f3 `(5, 3)`.
  - **Expected output**: `false`.

- **TC-PAWN-32 — Reject en passant beside a pawn that moved one rank at
  a time** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e5 `(4, 5)`. `to` is
    d6 `(3, 6)`. New `Board` after `move` of the e2 pawn to e4, e4 to e5,
    the d7 pawn to d6, and d6 to d5, so d6 is empty and d5 holds a black
    pawn.
  - **Expected output**: `false`.

- **TC-PAWN-33 — Reject en passant beside a pawn of the pawn's color**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is f4 `(5, 4)`. `to` is
    e5 `(4, 5)`. New `Board` after `move` of the f2 pawn to f4 and the e2
    pawn to e4, so e5 is empty and e4 holds a white pawn that just moved
    two ranks.
  - **Expected output**: `false`.
