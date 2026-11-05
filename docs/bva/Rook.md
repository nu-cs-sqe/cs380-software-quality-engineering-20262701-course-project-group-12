# BVA Analysis for Rook

`Rook` is the piece that moves any number of squares along a rank or file
and does not jump (issue #13 / GitHub US-03, docs US-05). It extends `Piece`.
This analysis covers `canMove`. The constructor only stores `PieceType.ROOK`
and the color through `Piece`, so it has no cases of its own.

`canMove` is a query. It does not move the rook. `Board.move` still places
the piece. Occupancy comes from a real `Board` (this team owns `Board`, and
`Location` has no `equals`, so EasyMock cannot match the in-between squares
`Rook` creates). Tests rearrange the starting position with `Board.move`
when a path has to be empty.

`canMove(Location from, Location to, Board board)` returns `true` when the
destination is on the same rank or file, `from` and `to` are different
squares, every square between them is empty, and `to` is empty or holds an
opponent's piece. It returns `false` for any other destination. It throws
`IllegalArgumentException` when `from`, `to`, or `board` is null.

Squares use file `0` = a-file through file `7` = h-file, and rank `1`
through `8`.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: `from`**
  - null
  - a1 `(0, 1)`
  - h8 `(7, 8)`
- **Input: `to`**
  - null
  - the same square as `from`
  - one square along the file
  - one square along the rank
  - the far end of the file
  - the far end of the rank
  - off the rank and file (a diagonal square)
- **Input: `board`**
  - null
  - a board whose occupancy matches the case
- **Input: squares between `from` and `to`**
  - none (adjacent squares)
  - many; the first between-square and the last between-square stand for
    that block
- **Input: occupant of `to`**
  - empty
  - an opponent's piece
  - a piece of the rook's color
- **Input: rook color**
  - white
  - black
- **Output: allowed move**
  - `true`
- **Output: rejected destination**
  - `false`
- **Output: signal for a missing argument**
  - `IllegalArgumentException`

### Step 2: Categories

- `from`, `to`, and `board` are pointers. One null is tested at a time.
- A rook move is a case: along a file, along a rank, or neither.
- Distance on a clear line is an interval `[1, 7]`. `0` is the same square
  and is not a move. Location already forbids squares off the board, so
  there is no `8`.
- Between-squares are a collection. Adjacent moves have none. A long
  move has many; the first and last squares stand for that block. An
  interior blocker is not its own case. Distance `2` is inside `[1, 7]`,
  so it is not a separate case.
- The destination occupant is a case: empty, opponent, same color.
- Rook color does not change the geometry, so each color appears once.
- Two invalid inputs are never paired. A blocked path is not also given a
  same-color destination in the same case.

### Step 3: Boundary choices

- One square along the file onto an empty square (low distance, empty
  between, empty destination).
- One square along the rank onto an empty square.
- Seven squares along the file onto an empty square (high distance, many
  empty between).
- Seven squares along the rank onto an empty square.
- A destination off the rank and file: a1 `(0, 1)` to b3 `(1, 3)`, which
  is empty on the starting board.
- The same square: a1 to a1.
- Adjacent own piece: starting a1 rook to a2 `(0, 2)` white pawn. No
  between-squares.
- Adjacent opponent: white rook on a1, black pawn on a2, a2 otherwise
  empty.
- First between-square occupied: starting a1 to a8 `(0, 8)`, white pawn
  on a2.
- Last between-square occupied: a-file cleared except the black pawn on
  a7 `(0, 7)`, destination a8.
- Capture at the far end of a clear file: a-file cleared through a6,
  destination the black pawn on a7.
- Black rook: h8 `(7, 8)` one square along the file onto empty h7
  `(7, 7)`.
- One null argument at a time.

## Step 4: Test Cases

### Method under test: `canMove(Location from, Location to, Board board)`

- **TC-ROOK-01 — Move one square along the file onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    a2 `(0, 2)`. The board is a new `Board` after `move` of the a2 pawn
    to b3 `(1, 3)`, so a2 is empty.
  - **Expected output**: `true`.

- **TC-ROOK-02 — Move one square along the rank onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    b1 `(1, 1)`. The board is a new `Board` after `move` of the b1 knight
    to c3 `(2, 3)`, so b1 is empty.
  - **Expected output**: `true`.

- **TC-ROOK-03 — Move seven squares along the file onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    a8 `(0, 8)`. The a-file is cleared: a2 pawn to b3 `(1, 3)`, a7 pawn
    to b6 `(1, 6)`, a8 rook to c6 `(2, 6)`. Every square from a2 through
    a8 is empty.
  - **Expected output**: `true`.

- **TC-ROOK-04 — Move seven squares along the rank onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    h1 `(7, 1)`. Rank 1 is cleared: b1 knight to a3 `(0, 3)`, c1 bishop
    to a4 `(0, 4)`, d1 queen to a5 `(0, 5)`, e1 king to a6 `(0, 6)`, f1
    bishop to b3 `(1, 3)`, g1 knight to b4 `(1, 4)`, h1 rook to b5
    `(1, 5)`. Every square from b1 through h1 is empty.
  - **Expected output**: `true`.

- **TC-ROOK-05 — Reject a destination off the rank and file** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    b3 `(1, 3)`. New `Board`; b3 is empty.
  - **Expected output**: `false`.

- **TC-ROOK-06 — Reject the square the rook already occupies** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` and `to` are both a1
    `(0, 1)`. New `Board`.
  - **Expected output**: `false`.

- **TC-ROOK-07 — Reject an adjacent piece of the rook's color** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    a2 `(0, 2)`. New `Board`; a2 holds the white a-pawn.
  - **Expected output**: `false`.

- **TC-ROOK-08 — Capture an adjacent opponent** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    a2 `(0, 2)`. New `Board` after `move` of the a2 pawn to b3 `(1, 3)`
    and `move` of the a7 pawn to a2, so a2 holds a black pawn.
  - **Expected output**: `true`.

- **TC-ROOK-09 — Reject a jump over the first square of the path** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    a8 `(0, 8)`. New `Board`; a2 holds the white a-pawn.
  - **Expected output**: `false`.

- **TC-ROOK-10 — Reject a jump over the last square of the path** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    a8 `(0, 8)`. New `Board` after `move` of the a2 pawn to b3 `(1, 3)`,
    so a2 through a6 are empty and a7 still holds the black a-pawn.
  - **Expected output**: `false`.

- **TC-ROOK-11 — Capture an opponent at the far end of a clear file**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    a7 `(0, 7)`. New `Board` after `move` of the a2 pawn to b3 `(1, 3)`,
    so a2 through a6 are empty and a7 holds the black a-pawn.
  - **Expected output**: `true`.

- **TC-ROOK-12 — Move a black rook one square along the file onto an empty
  square** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black rook. `from` is h8 `(7, 8)`. `to` is
    h7 `(7, 7)`. New `Board` after `move` of the h7 pawn to g6 `(6, 6)`,
    so h7 is empty.
  - **Expected output**: `true`.

- **TC-ROOK-13 — Reject a null from square** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is null. `to` is a2
    `(0, 2)`. New `Board`.
  - **Expected output**: `IllegalArgumentException`.

- **TC-ROOK-14 — Reject a null to square** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    null. New `Board`.
  - **Expected output**: `IllegalArgumentException`.

- **TC-ROOK-15 — Reject a null board** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White rook. `from` is a1 `(0, 1)`. `to` is
    a2 `(0, 2)`. `board` is null.
  - **Expected output**: `IllegalArgumentException`.
