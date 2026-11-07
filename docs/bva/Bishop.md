# BVA Analysis for Bishop

`Bishop` is the piece that moves any number of squares along a diagonal
and does not jump (issue #14 / GitHub US-04, docs US-07). It extends
`Piece`. This analysis covers `canMove`. The constructor only stores
`PieceType.BISHOP` and the color through `Piece`, so it has no cases of
its own.

`canMove` is a query. It does not move the bishop. `Board.move` still
places the piece. Occupancy comes from a real `Board` (this team owns
`Board`, and `Location` has no `equals`, so EasyMock cannot match the
in-between squares `Bishop` creates). Tests rearrange the starting
position with `Board.move` when a path has to be empty.

`canMove(Location from, Location to, Board board)` returns `true` when
the destination is on a diagonal through `from`, `from` and `to` are
different squares, every square between them is empty, and `to` is empty
or holds an opponent's piece. It returns `false` for any other
destination.

Squares use file `0` = a-file through file `7` = h-file, and rank `1`
through `8`. A square is dark when file + rank is odd (a1 is dark) and
light when it is even (h1 is light).

## Week 6 instructor review

- **No null checks.** This is a standalone Java application, so the code
  makes sure `null` is never passed around instead of checking for it.
  `canMove` has no null cases and does not throw for a null argument.
- **No magic numbers.** Values with a meaning, such as the longest
  diagonal of seven squares, become named constants in the tests and in
  `Bishop`.

## Boundaries Considered

### Step 1: Inputs and outputs

- **Input: `from`**
  - c1 `(2, 1)`, the white dark-square bishop
  - f1 `(5, 1)` moved to h1 `(7, 1)`, the white light-square bishop
  - c8 `(2, 8)`, the black light-square bishop
  - f8 `(5, 8)`, the black dark-square bishop
  - a1 `(0, 1)`, a corner
- **Input: `to`**
  - the same square as `from`
  - one square along each of the four diagonal directions
  - seven squares along a diagonal, corner to corner
  - one square along the file
  - one square along the rank
  - two squares along the file, a square of the same color
  - one more rank than file away
  - one more file than rank away
- **Input: `board`**
  - a board whose occupancy matches the case
- **Input: squares between `from` and `to`**
  - none (adjacent squares)
  - many; the first between-square and the last between-square stand
    for that block
- **Input: occupant of `to`**
  - empty
  - an opponent's piece
  - a piece of the bishop's color
- **Input: bishop color**
  - white
  - black
- **Input: color of the bishop's square**
  - dark
  - light
- **Output: allowed move**
  - `true`
- **Output: rejected destination**
  - `false`

### Step 2: Categories

- A bishop move is a case: along a diagonal, or not. The diagonal case
  has four directions, so each direction appears once.
- Distance on a clear diagonal is an interval `[1, 7]`. `0` is the same
  square and is not a move. `Location` already forbids squares off the
  board, so there is no `8`.
- The file distance compared with the rank distance is a boundary. Equal
  is a diagonal. One apart on either side is just off the diagonal. A
  file distance of `0` is along the file, and a rank distance of `0` is
  along the rank.
- Square color is a case. A diagonal move never changes the color, so
  every allowed destination is the color of `from`. A destination of the
  same color that is not on a diagonal is still rejected.
- Between-squares are a collection. Adjacent moves have none. A long
  move has many; the first and last squares stand for that block. An
  interior blocker is not its own case. Distance `2` is inside `[1, 7]`,
  so it is not a separate case.
- The destination occupant is a case: empty, opponent, same color.
- Bishop color does not change the geometry, so each color appears in at
  least one case.
- Two invalid inputs are never paired. A blocked path has an opponent on
  the destination, and an off-diagonal destination is empty.

### Step 3: Boundary choices

- One square in each diagonal direction onto an empty square:
  - up and right, white c1 to d2 `(3, 2)`
  - up and left, white c1 to b2 `(1, 2)`
  - down and right, black c8 to d7 `(3, 7)`
  - down and left, black f8 to e7 `(4, 7)`
- Seven squares onto an empty square on each long diagonal:
  - the dark diagonal, a1 `(0, 1)` to h8 `(7, 8)`
  - the light diagonal, h1 `(7, 1)` to a8 `(0, 8)`
- The same square: c1 to c1.
- One square along the file: c1 to c2 `(2, 2)`, a light square.
- One square along the rank: c1 to d1 `(3, 1)`, a light square.
- Two squares along the file: c1 to c3 `(2, 3)`, a dark square like c1.
- One more rank than file: c1 to e4 `(4, 4)`.
- One more file than rank: c1 to f3 `(5, 3)`.
- Adjacent own piece: c1 to d2, the white d-pawn on the starting board.
- Adjacent opponent: c1 to d2 holding a black pawn.
- Capture at the far end of the dark diagonal: a1 to h8, the black rook.
- First between-square occupied: a1 to h8 with the white pawn on b2
  `(1, 2)`.
- Last between-square occupied: a1 to h8 with the black pawn on g7
  `(6, 7)`.

## Step 4: Test Cases

### Method under test: `canMove(Location from, Location to, Board board)`

- **TC-BISHOP-01 — Move one square up and right onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop on the dark square c1
    `(2, 1)`. `to` is d2 `(3, 2)`. The board is a new `Board` after
    `move` of the d2 pawn to d4 `(3, 4)`, so d2 is empty.
  - **Expected output**: `true`.

- **TC-BISHOP-02 — Move one square up and left onto an empty square**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop on c1 `(2, 1)`. `to` is b2
    `(1, 2)`. The board is a new `Board` after `move` of the b2 pawn to
    b4 `(1, 4)`, so b2 is empty.
  - **Expected output**: `true`.

- **TC-BISHOP-03 — Move a black bishop one square down and right onto an
  empty square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black bishop on the light square c8
    `(2, 8)`. `to` is d7 `(3, 7)`. The board is a new `Board` after
    `move` of the d7 pawn to d5 `(3, 5)`, so d7 is empty.
  - **Expected output**: `true`.

- **TC-BISHOP-04 — Move a black bishop one square down and left onto an
  empty square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black bishop on the dark square f8
    `(5, 8)`. `to` is e7 `(4, 7)`. The board is a new `Board` after
    `move` of the e7 pawn to e5 `(4, 5)`, so e7 is empty.
  - **Expected output**: `true`.

- **TC-BISHOP-05 — Move seven squares along the dark diagonal onto an
  empty square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is a1 `(0, 1)`. `to`
    is h8 `(7, 8)`. The board is a new `Board` after `move` of the c1
    bishop to a1, the b2 pawn to b4 `(1, 4)`, the g7 pawn to g5
    `(6, 5)`, and the h8 rook to h6 `(7, 6)`. Every square from b2
    through h8 is empty.
  - **Expected output**: `true`.

- **TC-BISHOP-06 — Move seven squares along the light diagonal onto an
  empty square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop on the light square h1
    `(7, 1)`. `to` is a8 `(0, 8)`. The board is a new `Board` after
    `move` of the f1 bishop to h1, the g2 pawn to g4 `(6, 4)`, the b7
    pawn to b5 `(1, 5)`, and the a8 rook to a6 `(0, 6)`. Every square
    from g2 through a8 is empty.
  - **Expected output**: `true`.

- **TC-BISHOP-07 — Reject the square the bishop already occupies**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` and `to` are both c1
    `(2, 1)`. New `Board`.
  - **Expected output**: `false`.

- **TC-BISHOP-08 — Reject one square along the file** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is c1 `(2, 1)`. `to`
    is c2 `(2, 2)`. The board is a new `Board` after `move` of the c2
    pawn to c4 `(2, 4)`, so c2 is empty.
  - **Expected output**: `false`.

- **TC-BISHOP-09 — Reject one square along the rank** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is c1 `(2, 1)`. `to`
    is d1 `(3, 1)`. The board is a new `Board` after `move` of the d1
    queen to d3 `(3, 3)`, so d1 is empty.
  - **Expected output**: `false`.

- **TC-BISHOP-10 — Reject a square of the same color off the diagonal**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop on the dark square c1
    `(2, 1)`. `to` is the dark square c3 `(2, 3)`. The board is a new
    `Board` after `move` of the c2 pawn to c4 `(2, 4)`, so c2 and c3 are
    empty.
  - **Expected output**: `false`.

- **TC-BISHOP-11 — Reject a destination one more rank than file away**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is c1 `(2, 1)`. `to`
    is e4 `(4, 4)`. New `Board`; e4 is empty.
  - **Expected output**: `false`.

- **TC-BISHOP-12 — Reject a destination one more file than rank away**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is c1 `(2, 1)`. `to`
    is f3 `(5, 3)`. New `Board`; f3 is empty.
  - **Expected output**: `false`.

- **TC-BISHOP-13 — Reject an adjacent piece of the bishop's color**
  ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is c1 `(2, 1)`. `to`
    is d2 `(3, 2)`. New `Board`; d2 holds the white d-pawn.
  - **Expected output**: `false`.

- **TC-BISHOP-14 — Capture an adjacent opponent** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is c1 `(2, 1)`. `to`
    is d2 `(3, 2)`. New `Board` after `move` of the d2 pawn to d4
    `(3, 4)` and `move` of the d7 pawn to d2, so d2 holds a black pawn.
  - **Expected output**: `true`.

- **TC-BISHOP-15 — Capture an opponent at the far end of a clear
  diagonal** ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is a1 `(0, 1)`. `to`
    is h8 `(7, 8)`. New `Board` after `move` of the c1 bishop to a1, the
    b2 pawn to b4 `(1, 4)`, and the g7 pawn to g5 `(6, 5)`, so b2
    through g7 are empty and h8 holds the black rook.
  - **Expected output**: `true`.

- **TC-BISHOP-16 — Reject a jump over the first square of the path**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is a1 `(0, 1)`. `to`
    is h8 `(7, 8)`. New `Board` after `move` of the c1 bishop to a1 and
    the g7 pawn to g5 `(6, 5)`, so b2 still holds the white b-pawn, c3
    through g7 are empty, and h8 holds the black rook.
  - **Expected output**: `false`.

- **TC-BISHOP-17 — Reject a jump over the last square of the path**
  ( :x: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White bishop. `from` is a1 `(0, 1)`. `to`
    is h8 `(7, 8)`. New `Board` after `move` of the c1 bishop to a1 and
    the b2 pawn to b4 `(1, 4)`, so b2 through f6 are empty, g7 still
    holds the black g-pawn, and h8 holds the black rook.
  - **Expected output**: `false`.
