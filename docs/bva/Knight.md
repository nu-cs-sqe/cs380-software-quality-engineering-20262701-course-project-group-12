# BVA Analysis for Knight

`Knight` is the piece that moves in an L shape and jumps over any pieces
in between (issue #12 / GitHub US-02, docs US-06). It extends `Piece`.
This analysis covers `canMove`. The constructor only stores
`PieceType.KNIGHT` and the color through `Piece`, so it has no cases of
its own.

`canMove` is a query. It does not move the knight. `Board.move` still
places the piece. Occupancy comes from a real `Board`, as in the rook,
bishop and pawn analyses, so the jump in AC 2 is real pieces standing
between the two squares. Tests rearrange the starting position with
`Board.move`.

`canMove(Location from, Location to, Board board)` returns `true` when
`to` is two squares from `from` in one direction and one square
perpendicular, and `to` is empty or holds an opponent's piece. It never
looks at the squares in between. It returns `false` for any other
destination.

Squares use file `0` = a-file through file `7` = h-file, and rank `1`
through `8`.

## Week 6 instructor review

- **No null checks.** This is a standalone Java application, so the code
  makes sure `null` is never passed around instead of checking for it.
  `canMove` has no null cases and does not throw for a null argument.
- **No magic numbers.** The two leg lengths of the L become named
  constants in `Knight`.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: `from`**
  - e4 `(4, 4)`, a central square
  - a1 `(0, 1)`, the lowest corner
  - h8 `(7, 8)`, the highest corner
  - b1 `(1, 1)` and g1 `(6, 1)`, the white knights' starting squares
- **Input: `to`**, as the file distance and the rank distance from `from`
  - `(1, 2)` and `(2, 1)`, the L shape, in each of the eight directions
  - `(0, 0)`, the same square
  - `(1, 1)`, one square along a diagonal
  - `(2, 2)`, two squares along a diagonal
  - `(0, 2)`, two squares along the file
  - `(2, 0)`, two squares along the rank
  - `(1, 3)`, one rank too far
  - `(3, 1)`, one file too far
- **Input: `board`**
  - a board whose occupancy matches the case
- **Input: squares between `from` and `to`**
  - empty
  - occupied
- **Input: occupant of `to`**
  - empty
  - an opponent's piece
  - a piece of the knight's color
- **Input: knight color**
  - white
  - black
- **Output: allowed move**
  - `true`
- **Output: rejected destination**
  - `false`

### Step 2: Categories

- The file distance and the rank distance are a pair of intervals. The
  two allowed points are `(1, 2)` and `(2, 1)`. Each one gets its four
  neighbors one step away: `(0, 2)`, `(2, 2)`, `(1, 1)`, `(1, 3)` and
  `(2, 0)`, `(3, 1)`, with `(1, 1)` and `(2, 2)` shared. `(0, 0)`, the
  same square, is the lowest corner of both intervals.
- The direction is a case. An L has eight directions (AC 4: eight
  destinations from a central square), so each direction appears once.
- The board edge is a case. From a corner only two of the eight
  directions stay on the board (AC 4). `Location` already forbids the
  other six, so the two remaining destinations are the cases, at both
  the lowest corner and the highest corner.
- The squares in between are a Boolean: empty or occupied. A knight
  ignores them (AC 2), so the occupied case is the one that matters.
- The destination occupant is a case: empty, opponent, same color.
- Knight color does not change the geometry, so each color appears in
  at least one case.
- Two invalid inputs are never paired. The off-L destinations are empty,
  except `(1, 3)`, which holds an opponent, so only the geometry is
  wrong. The same square is the one exception: the knight always stands
  on its own square. The geometry check rejects it first, before the
  own-piece check exists.

### Step 3: Boundary choices

- The central square: a new `Board` after `move` of the g1 knight to e4
  `(4, 4)`, the d2 pawn to d3 `(3, 3)` and the f2 pawn to f3 `(5, 3)`.
  All eight destinations of e4 are then empty.
- The L in each direction from e4 onto an empty square:
  - two up, one right: f6 `(5, 6)`
  - two up, one left: d6 `(3, 6)`
  - one up, two right: g5 `(6, 5)`
  - one up, two left: c5 `(2, 5)`
  - one down, two right: g3 `(6, 3)`
  - one down, two left: c3 `(2, 3)`
  - two down, one right: f2 `(5, 2)`
  - two down, one left: d2 `(3, 2)`
- The lowest corner: the b1 knight on a1 in place of the rook, to b3
  `(1, 3)` and to c2 `(2, 2)`.
- The highest corner: the black g8 knight on h8 in place of the rook, to
  g6 `(6, 6)` and to f7 `(5, 7)`.
- Pieces in between: b1 to c3 on the starting board, over the b2 and c2
  pawns.
- Off the L from e4:
  - the same square: e4 `(4, 4)`
  - `(1, 1)`: f5 `(5, 5)`
  - `(2, 2)`: g6 `(6, 6)`
  - `(0, 2)`: e6 `(4, 6)`
  - `(2, 0)`: g4 `(6, 4)`
  - `(1, 3)`: f7 `(5, 7)`, the black f-pawn
  - `(3, 1)`: h5 `(7, 5)`
- Own piece: g1 to e2 `(4, 2)`, the white e-pawn on the starting board.
- Opponent: b1 to c3 holding a black pawn.

## Step 4: Test Cases

### Method under test: `canMove(Location from, Location to, Board board)`

- **TC-KNIGHT-01 — Move two up and one right onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is f6 `(5, 6)`, empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-02 — Move two up and one left onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is d6 `(3, 6)`, empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-03 — Move one up and two right onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is g5 `(6, 5)`, empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-04 — Move one up and two left onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is c5 `(2, 5)`, empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-05 — Move one down and two right onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is g3 `(6, 3)`, empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-06 — Move one down and two left onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is c3 `(2, 3)`, empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-07 — Move two down and one right onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is f2 `(5, 2)`, empty after the f-pawn moved.
  - **Expected output**: `true`.

- **TC-KNIGHT-08 — Move two down and one left onto an empty square**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is d2 `(3, 2)`, empty after the d-pawn moved.
  - **Expected output**: `true`.

- **TC-KNIGHT-09 — Move from the lowest corner two up and one right**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight. `from` is a1 `(0, 1)`. `to`
    is b3 `(1, 3)`. New `Board` after `move` of the b1 knight to a1, in
    place of the rook. b3 is empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-10 — Move from the lowest corner one up and two right**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight. `from` is a1 `(0, 1)`. `to`
    is c2 `(2, 2)`. New `Board` after `move` of the b1 knight to a1, in
    place of the rook, and of the c2 pawn to c4 `(2, 4)`. c2 is empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-11 — Move a black knight from the highest corner two down
  and one left** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black knight. `from` is h8 `(7, 8)`. `to`
    is g6 `(6, 6)`. New `Board` after `move` of the g8 knight to h8, in
    place of the rook. g6 is empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-12 — Move a black knight from the highest corner one down
  and two left** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black knight. `from` is h8 `(7, 8)`. `to`
    is f7 `(5, 7)`. New `Board` after `move` of the g8 knight to h8, in
    place of the rook, and of the f7 pawn to f5 `(5, 5)`. f7 is empty.
  - **Expected output**: `true`.

- **TC-KNIGHT-13 — Jump over the pieces in between** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight. `from` is b1 `(1, 1)`. `to`
    is c3 `(2, 3)`, empty. New `Board`; the white pawns on b2 `(1, 2)`
    and c2 `(2, 2)` stand between the two squares.
  - **Expected output**: `true`.

- **TC-KNIGHT-14 — Reject the square the knight already occupies**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is e4.
  - **Expected output**: `false`.

- **TC-KNIGHT-15 — Reject one square along a diagonal** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is f5 `(5, 5)`, empty.
  - **Expected output**: `false`.

- **TC-KNIGHT-16 — Reject two squares along a diagonal** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is g6 `(6, 6)`, empty.
  - **Expected output**: `false`.

- **TC-KNIGHT-17 — Reject two squares along the file** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is e6 `(4, 6)`, empty.
  - **Expected output**: `false`.

- **TC-KNIGHT-18 — Reject two squares along the rank** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is g4 `(6, 4)`, empty.
  - **Expected output**: `false`.

- **TC-KNIGHT-19 — Reject an opponent one rank beyond the L** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is f7 `(5, 7)`, which holds the black f-pawn.
  - **Expected output**: `false`.

- **TC-KNIGHT-20 — Reject a square one file beyond the L** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight on e4 `(4, 4)` (the central
    square setup). `to` is h5 `(7, 5)`, empty.
  - **Expected output**: `false`.

- **TC-KNIGHT-21 — Reject a piece of the knight's color** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight. `from` is g1 `(6, 1)`. `to`
    is e2 `(4, 2)`. New `Board`; e2 holds the white e-pawn.
  - **Expected output**: `false`.

- **TC-KNIGHT-22 — Capture an opponent** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White knight. `from` is b1 `(1, 1)`. `to`
    is c3 `(2, 3)`. New `Board` after `move` of the c7 pawn to c3, so c3
    holds a black pawn.
  - **Expected output**: `true`.
