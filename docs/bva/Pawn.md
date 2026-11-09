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

Forward is toward rank 8 for a white pawn and toward rank 1 for a black
pawn. The starting rank is rank 2 for White and rank 7 for Black.

Squares use file `0` = a-file through file `7` = h-file, and rank `1`
through `8`.

Out of scope: promotion on the last rank (US-13) and en passant (US-12).
A move onto the last rank is an ordinary forward move here. A diagonal
move onto an empty square is rejected, because en passant is a later
story.

`attacks(Location from, Location target, Board board)` (issue #45 /
US-12) overrides `Piece.attacks`. A pawn attacks the two squares one
square diagonally forward, whether they are empty or hold an opponent,
so a pawn can stop the king from castling across an empty square. A
straight move is never an attack. The board is not consulted.

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

## Boundaries for `attacks` (issue #45)

- **Input: `from`**: white e2 `(4, 2)` and e4 `(4, 4)`; black e7
  `(4, 7)`.
- **Input: `target`**: one square diagonally forward toward a higher
  file and toward a lower file; one square straight forward; one square
  diagonally backward; one rank forward and two files over; two squares
  diagonally forward.
- **Input: occupant of `target`**: empty; an opponent's piece. Callers
  never ask about a square that holds the pawn's own color.
- **Input: pawn color**: white; black.
- **Output**: `true`; `false`.

The file distance of an attack is an interval `[1, 1]`: `0` (straight
ahead), `1`, and `2`. The rank change is an interval `[1, 1]` in the
pawn's forward direction: `-1` (backward), `1`, and `2`. The values are
the same squares as the `canMove` capture cases, without the opponent
that a capture needs.

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

### Method under test: `attacks(Location from, Location target, Board board)` (issue #45)

- **TC-PAWN-25 — Attack an empty square diagonally forward toward a
  higher file** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`.
    `target` is f3 `(5, 3)`. New `Board`, so f3 is empty.
  - **Expected output**: `true`.

- **TC-PAWN-26 — Attack an empty square diagonally forward toward a
  lower file** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`.
    `target` is d3 `(3, 3)`. New `Board`, so d3 is empty.
  - **Expected output**: `true`.

- **TC-PAWN-27 — Attack with a black pawn** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: Black pawn. `from` is e7 `(4, 7)`.
    `target` is d6 `(3, 6)`. New `Board`, so d6 is empty.
  - **Expected output**: `true`.

- **TC-PAWN-28 — Attack a square that holds an opponent** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`.
    `target` is d3 `(3, 3)`. New `Board` after `move` of the d7 pawn to
    d3, so d3 holds a black pawn.
  - **Expected output**: `true`.

- **TC-PAWN-29 — Do not attack the square straight ahead** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`.
    `target` is e3 `(4, 3)`. New `Board`, so e3 is empty.
  - **Expected output**: `false`.

- **TC-PAWN-30 — Do not attack diagonally backward** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e4 `(4, 4)`.
    `target` is f3 `(5, 3)`. New `Board` after `move` of the e2 pawn to
    e4, so f3 is empty.
  - **Expected output**: `false`.

- **TC-PAWN-31 — Do not attack one rank forward and two files over**
  ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`.
    `target` is g3 `(6, 3)`. New `Board`, so g3 is empty.
  - **Expected output**: `false`.

- **TC-PAWN-32 — Do not attack two squares diagonally forward** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White pawn. `from` is e2 `(4, 2)`.
    `target` is g4 `(6, 4)`. New `Board`, so g4 is empty.
  - **Expected output**: `false`.
