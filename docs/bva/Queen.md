# BVA Analysis for Queen

`Queen` is the piece that moves any number of squares along a rank, a
file, or a diagonal and does not jump (issue #15 / GitHub US-05, docs
US-08). It extends `Piece`. This analysis covers `canMove`. The
constructor only stores `PieceType.QUEEN` and the color through `Piece`,
so it has no cases of its own.

`canMove` is a query. It does not move the queen. `Board.move` still
places the piece. Occupancy comes from a real `Board` (this team owns
`Board`, and `Location` has no `equals`, so EasyMock cannot match the
in-between squares `Queen` creates). Tests rearrange the starting
position with `Board.move` when a path has to be empty.

`canMove(Location from, Location to, Board board)` returns `true` when
the destination shares a rank, a file, or a diagonal with `from`, `from`
and `to` are different squares, every square between them is empty, and
`to` is empty or holds an opponent's piece. It returns `false` for any
other destination.

Squares use file `0` = a-file through file `7` = h-file, and rank `1`
through `8`.

## Week 6 instructor review

- **No null checks.** This is a standalone Java application, so the code
  makes sure `null` is never passed around instead of checking for it.
  `canMove` has no null cases and does not throw for a null argument.
- **No magic numbers.** Squares in the tests are named constants (`D1`,
  `D4`, ...).

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: `from`**
  - d4 `(3, 4)`, a white queen in the middle of the board, so all eight
    neighbors are empty
  - d5 `(3, 5)`, a black queen in the middle of the board
  - d1 `(3, 1)`, the white queen's starting square
  - a3 `(0, 3)` and h3 `(7, 3)`, the ends of an empty rank
  - h8 `(7, 8)`, a corner, for a black queen
- **Input: `to`**
  - the same square as `from`
  - one square in each of the eight directions
  - seven squares along a rank, a file, and a diagonal
  - a knight's L: one file and two ranks away, or two files and one rank
    away
- **Input: `board`**
  - a board whose occupancy matches the case
- **Input: squares between `from` and `to`**
  - none (adjacent squares)
  - many; the first between-square and the last between-square stand
    for that block
- **Input: occupant of `to`**
  - empty
  - an opponent's piece
  - a piece of the queen's color
- **Input: queen color**
  - white
  - black
- **Output: allowed move**
  - `true`
- **Output: rejected destination**
  - `false`

### Step 2: Categories

- A queen move is a case: along a line (rank, file, or diagonal), or
  not. A line has eight directions, so each direction appears once.
- Distance along a clear line is an interval `[1, 7]`. `0` is the same
  square and is not a move. `Location` already forbids squares off the
  board, so there is no `8`. Each kind of line (rank, file, diagonal)
  reaches the high edge `7` once.
- A knight's L is the boundary just off every line: one file and two
  ranks away is one rank off the diagonal and one file off the file,
  and two files and one rank away is one file off the diagonal and one
  rank off the rank. Both orientations appear.
- Between-squares are a collection. Adjacent moves have none. A long
  move has many; the first and last squares stand for that block. An
  interior blocker is not its own case. Distance `2` is inside `[1, 7]`,
  so it is not a separate case.
- A rook-like path (file or rank) and a bishop-like path (diagonal) are
  walked by the same check, so each kind of line has a blocked case,
  and a long move walks toward a lower file and a lower rank as well as
  toward higher ones.
- The destination occupant is a case: empty, opponent, same color.
- Queen color does not change the geometry, so each color appears in at
  least one case.
- Two invalid inputs are never paired. A blocked path ends on an empty
  square or an opponent, and an off-line destination is empty.

### Step 3: Boundary choices

- One square in each direction onto an empty square:
  - up the file, white d4 to d5 `(3, 5)`
  - right along the rank, white d4 to e4 `(4, 4)`
  - left along the rank, white d4 to c4 `(2, 4)`
  - up and right, white d4 to e5 `(4, 5)`
  - up and left, white d4 to c5 `(2, 5)`
  - down the file, black d5 to d4 `(3, 4)`
  - down and right, black d5 to e4 `(4, 4)`
  - down and left, black d5 to c4 `(2, 4)`
- Seven squares along a rank onto an empty square: a3 `(0, 3)` to h3
  `(7, 3)`.
- Seven squares along a diagonal toward rank 1 onto an empty square:
  black h8 `(7, 8)` to a1 `(0, 1)`.
- Seven squares along a file, capturing at the far end: d1 `(3, 1)` to
  d8 `(3, 8)`, the black queen.
- The same square: d1 to d1.
- A knight's L, one file and two ranks: d4 to e6 `(4, 6)`.
- A knight's L, two files and one rank: d4 to f5 `(5, 5)`.
- Adjacent own piece: d1 to d2 `(3, 2)`, the white d-pawn on the
  starting board.
- Adjacent opponent: d4 to d5 holding a black pawn.
- First between-square occupied along a file: d1 to d8 with the white
  pawn on d2.
- Last between-square occupied along a file: d1 to d8 with the black
  pawn on d7 `(3, 7)`.
- First between-square occupied along a diagonal: d1 to h5 `(7, 5)`
  with the white pawn on e2 `(4, 2)`, the starting board.
- Last between-square occupied along a rank toward the a-file: h3 to a3
  with a white pawn on b3 `(1, 3)`.

## Step 4: Test Cases

### Method under test: `canMove(Location from, Location to, Board board)`

- **TC-QUEEN-01 — Move one square up the file onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d4 `(3, 4)`. `to` is
    d5 `(3, 5)`. The board is a new `Board` after `move` of the d1 queen
    to d4, so every neighbor of d4 is empty.
  - **Expected output**: `true`.

- **TC-QUEEN-02 — Move one square right along the rank onto an empty
  square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d4 `(3, 4)`. `to` is
    e4 `(4, 4)`. New `Board` after `move` of the d1 queen to d4.
  - **Expected output**: `true`.

- **TC-QUEEN-03 — Move one square left along the rank onto an empty
  square** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d4 `(3, 4)`. `to` is
    c4 `(2, 4)`. New `Board` after `move` of the d1 queen to d4.
  - **Expected output**: `true`.

- **TC-QUEEN-04 — Move one square up and right onto an empty square**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d4 `(3, 4)`. `to` is
    e5 `(4, 5)`. New `Board` after `move` of the d1 queen to d4.
  - **Expected output**: `true`.

- **TC-QUEEN-05 — Move one square up and left onto an empty square**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d4 `(3, 4)`. `to` is
    c5 `(2, 5)`. New `Board` after `move` of the d1 queen to d4.
  - **Expected output**: `true`.

- **TC-QUEEN-06 — Move a black queen one square down the file onto an
  empty square** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black queen. `from` is d5 `(3, 5)`. `to` is
    d4 `(3, 4)`. New `Board` after `move` of the d8 queen to d5, so every
    neighbor of d5 is empty.
  - **Expected output**: `true`.

- **TC-QUEEN-07 — Move a black queen one square down and right onto an
  empty square** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black queen. `from` is d5 `(3, 5)`. `to` is
    e4 `(4, 4)`. New `Board` after `move` of the d8 queen to d5.
  - **Expected output**: `true`.

- **TC-QUEEN-08 — Move a black queen one square down and left onto an
  empty square** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black queen. `from` is d5 `(3, 5)`. `to` is
    c4 `(2, 4)`. New `Board` after `move` of the d8 queen to d5.
  - **Expected output**: `true`.

- **TC-QUEEN-09 — Move seven squares along a rank onto an empty square**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is a3 `(0, 3)`. `to` is
    h3 `(7, 3)`. New `Board` after `move` of the d1 queen to a3. Every
    square from b3 through h3 is empty.
  - **Expected output**: `true`.

- **TC-QUEEN-10 — Move a black queen seven squares along a diagonal
  toward rank 1 onto an empty square** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black queen. `from` is h8 `(7, 8)`. `to` is
    a1 `(0, 1)`. New `Board` after `move` of the h8 rook to h6 `(7, 6)`,
    the d8 queen to h8, the g7 pawn to g5 `(6, 5)`, the b2 pawn to b4
    `(1, 4)`, and the a1 rook to a3 `(0, 3)`. Every square from g7
    through a1 is empty.
  - **Expected output**: `true`.

- **TC-QUEEN-11 — Capture an opponent seven squares along a clear file**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d1 `(3, 1)`. `to` is
    d8 `(3, 8)`. New `Board` after `move` of the d2 pawn to c3 `(2, 3)`
    and the d7 pawn to e6 `(4, 6)`, so d2 through d7 are empty and d8
    holds the black queen.
  - **Expected output**: `true`.

- **TC-QUEEN-12 — Reject the square the queen already occupies**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` and `to` are both d1
    `(3, 1)`. New `Board`.
  - **Expected output**: `false`.

- **TC-QUEEN-13 — Reject a knight's L one file and two ranks away**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d4 `(3, 4)`. `to` is
    e6 `(4, 6)`. New `Board` after `move` of the d1 queen to d4; e6 is
    empty.
  - **Expected output**: `false`.

- **TC-QUEEN-14 — Reject a knight's L two files and one rank away**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d4 `(3, 4)`. `to` is
    f5 `(5, 5)`. New `Board` after `move` of the d1 queen to d4; f5 is
    empty.
  - **Expected output**: `false`.

- **TC-QUEEN-15 — Reject an adjacent piece of the queen's color**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d1 `(3, 1)`. `to` is
    d2 `(3, 2)`. New `Board`; d2 holds the white d-pawn.
  - **Expected output**: `false`.

- **TC-QUEEN-16 — Capture an adjacent opponent** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d4 `(3, 4)`. `to` is
    d5 `(3, 5)`. New `Board` after `move` of the d1 queen to d4 and the
    d7 pawn to d5, so d5 holds a black pawn.
  - **Expected output**: `true`.

- **TC-QUEEN-17 — Reject a jump over the first square of a file**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d1 `(3, 1)`. `to` is
    d8 `(3, 8)`. New `Board` after `move` of the d7 pawn to e6 `(4, 6)`,
    so d2 still holds the white d-pawn, d3 through d7 are empty, and d8
    holds the black queen.
  - **Expected output**: `false`.

- **TC-QUEEN-18 — Reject a jump over the last square of a file**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d1 `(3, 1)`. `to` is
    d8 `(3, 8)`. New `Board` after `move` of the d2 pawn to c3 `(2, 3)`,
    so d2 through d6 are empty, d7 still holds the black d-pawn, and d8
    holds the black queen.
  - **Expected output**: `false`.

- **TC-QUEEN-19 — Reject a jump over the first square of a diagonal**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is d1 `(3, 1)`. `to` is
    h5 `(7, 5)`. New `Board`; e2 holds the white e-pawn, f3 and g4 are
    empty, and h5 is empty.
  - **Expected output**: `false`.

- **TC-QUEEN-20 — Reject a jump over the last square of a rank toward
  the a-file** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White queen. `from` is h3 `(7, 3)`. `to` is
    a3 `(0, 3)`. New `Board` after `move` of the d1 queen to h3 and the
    b2 pawn to b3 `(1, 3)`, so g3 through c3 are empty, b3 holds a white
    pawn, and a3 is empty.
  - **Expected output**: `false`.
