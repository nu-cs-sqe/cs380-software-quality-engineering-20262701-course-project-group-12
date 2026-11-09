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

Out of scope: whether the destination of a one-square move is attacked
(US-11). A two-square destination is rejected as a normal king move,
unless it is a castle.

## Castling (issue #45 / GitHub US-12)

`canMove` also returns `true` for a castle: `to` is two squares along
the rank from `from`, and all of these hold, where the rook square is
the corner of that rank on the side of `to` (h-file for g1 or g8, a-file
for c1 or c8):

- `Board.holdsUnmovedPiece(from)`: the king has never moved. Only the
  king placed on e1 or e8 at the start can be unmoved on its square.
- `Board.holdsUnmovedPiece(rook square)`: that rook has never moved and
  has not been captured.
- Every square between the king and that rook is empty.
- `Board.isAttackedByOpponentOf` is false for the square the king stands
  on, the square it crosses, and `to`. b1, b8, and the rook square may be
  attacked.

`attacks(Location from, Location target, Board board)` overrides
`Piece.attacks`: a king attacks only the eight adjoining squares. A
castle never captures, so it is not an attack.

`move(Location from, Location to, Board board)` overrides `Piece.move`:
the board moves the king, and when `to` is two squares along the rank it
also moves that rook to the square the king crossed. `BoardController`
calls it only after `canMove` allowed the move.

Tests clear the squares between the king and a rook by moving those
pieces straight up the board to rank 4 (White) or down to rank 5
(Black). Opponent attackers are moved onto the square they attack from.
Fifty-move counting (AC 8) is US-18; a castle is a king move and
`Board` keeps no move count yet.

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

## Boundaries for castling (issue #45)

- **Input: `from`, `to`**: each of the four castles is a case: white
  kingside e1 to g1, white queenside e1 to c1, black kingside e8 to g8,
  black queenside e8 to c8 (all-combination of 2 colors and 2 sides).
  Not a castle: e1 to g3 `(6, 3)` (two files, not along the rank) and
  e1 to b1 `(1, 1)` (three files along the rank).
- **Input: history of the king**: never moved; moved to f1 and back.
- **Input: history of the chosen rook**: never moved; moved and back;
  gone (moved off its square). The other rook moved and back.
- **Input: squares between the king and the rook**: a collection of 2
  (kingside) or 3 (queenside) squares. All empty; the square next to the
  rook occupied (b1, not on the king's path). The square next to the king
  occupied is TC-KING-11.
- **Input: attacked squares**: none; the king's square; the crossed
  square; `to`; b1; the rook's square. Each is attacked by one black
  knight or rook, so only that square is attacked.
- **Output of `canMove`**: `true`; `false`.
- **`attacks`**: an adjoining square, `true`; the castle square g1 with
  a clear kingside, `false`.
- **`move`**: the rook square for each of the four castles; a one-square
  king move leaves the rook in place.

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

- **TC-KING-11 — Reject two squares along the rank** ( :white_check_mark: )
  - Since issue #45 this is also a castle refused because the f1 bishop
    stands between the king and the h1 rook.
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e1 `(4, 1)`. `to` is g1
    `(6, 1)`, two squares toward the h-file (castling distance). The
    board is a new `Board` after `move` of the g1 bishop to g3 `(6, 3)`,
    so g1 is empty.
  - **Expected output**: `false`.

- **TC-KING-12 — Reject two squares along a diagonal** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is g6
    `(6, 6)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so g6 is empty.
  - **Expected output**: `false`.

- **TC-KING-13 — Reject two squares in one direction and one in the
  other** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is f6
    `(5, 6)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so f6 is empty.
  - **Expected output**: `false`.

- **TC-KING-14 — Reject a far square along the rank** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king on e4 `(4, 4)`. `to` is a4
    `(0, 4)`. The board is a new `Board` after `move` of the king from
    e1 to e4, so a4 is empty.
  - **Expected output**: `false`.

- **TC-KING-15 — Reject an adjacent piece of the king's color** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king. `from` is e1 `(4, 1)`. `to` is
    e2 `(4, 2)`. New `Board`; e2 holds the white e-pawn.
  - **Expected output**: `false`.

- **TC-KING-16 — Capture an adjacent opponent** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king. `from` is e1 `(4, 1)`. `to` is
    e2 `(4, 2)`. The board is a new `Board` after `move` of the e2 pawn
    to e4 `(4, 4)` and `move` of the e7 pawn to e2, so e2 holds a black
    pawn.
  - **Expected output**: `true`.

### Method under test: `canMove(Location from, Location to, Board board)`, castling (issue #45)

- **TC-KING-17 — Castle white kingside** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king. `from` is e1 `(4, 1)`, `to` is g1 `(6, 1)`. New `Board` after `move` of f1 to f4 and g1 to g4, so f1 and g1 are empty.
  - **Expected output**: `true`.

- **TC-KING-18 — Castle white queenside** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king. `from` is e1, `to` is c1 `(2, 1)`. New `Board` after `move` of b1 to b4, c1 to c4, and d1 to d4, so b1, c1, and d1 are empty.
  - **Expected output**: `true`.

- **TC-KING-19 — Castle black kingside** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black king. `from` is e8 `(4, 8)`, `to` is g8 `(6, 8)`. New `Board` after `move` of f8 to f5 and g8 to g5.
  - **Expected output**: `true`.

- **TC-KING-20 — Castle black queenside** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: Black king. `from` is e8, `to` is c8 `(2, 8)`. New `Board` after `move` of b8 to b5, c8 to c5, and d8 to d5.
  - **Expected output**: `true`.

- **TC-KING-21 — Reject a castle by a king that moved and came back** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared as in TC-KING-17, then `move` of the king e1 to f1 and f1 back to e1.
  - **Expected output**: `false`.

- **TC-KING-22 — Reject a castle with a rook that moved and came back** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared, then `move` of the h1 rook to g1 and back to h1.
  - **Expected output**: `false`.

- **TC-KING-23 — Castle kingside after the queenside rook moved** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared, then `move` of the a1 rook to a3 `(0, 3)` and back to a1.
  - **Expected output**: `true`.

- **TC-KING-24 — Reject a castle toward a rook that is gone** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared, then `move` of the h1 rook to h3 `(7, 3)`, so h1 is empty.
  - **Expected output**: `false`.

- **TC-KING-25 — Reject a castle with a piece next to the rook** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to c1. New `Board` after `move` of c1 to c4 and d1 to d4; the b1 knight stays between the king and the a1 rook.
  - **Expected output**: `false`.

- **TC-KING-26 — Reject a castle out of check** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared, then `move` of the b8 knight to d3 `(3, 3)`, which attacks e1.
  - **Expected output**: `false`.

- **TC-KING-27 — Reject a castle through an attacked square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared, then `move` of the b8 knight to e3 `(4, 3)`, which attacks f1 but not e1 or g1.
  - **Expected output**: `false`.

- **TC-KING-28 — Reject a castle onto an attacked square** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared, then `move` of the g8 knight to h3 `(7, 3)`, which attacks g1 but not e1 or f1.
  - **Expected output**: `false`.

- **TC-KING-29 — Castle queenside while b1 is attacked** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to c1. Queenside cleared as in TC-KING-18, then `move` of the b8 knight to a3 `(0, 3)`, which attacks b1 only.
  - **Expected output**: `true`.

- **TC-KING-30 — Castle kingside while the rook is attacked** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared, then `move` of the h2 pawn to a3 and the h7 pawn to a6 `(0, 6)`, so the h8 rook attacks h1 down the open h-file.
  - **Expected output**: `true`.

- **TC-KING-31 — Reject two files toward the rook that leave the rank** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to g3 `(6, 3)`. Kingside cleared, so g3 is empty.
  - **Expected output**: `false`.

- **TC-KING-32 — Reject three files toward the queenside rook** ( :white_check_mark: )
  - **Method(s) under test**: `canMove(Location, Location, Board)`
  - **State of the system**: White king, e1 to b1 `(1, 1)`. Queenside cleared, so b1 is empty.
  - **Expected output**: `false`.


### Method under test: `attacks(Location from, Location target, Board board)` (issue #45)

- **TC-KING-33 — Attack an adjoining square** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White king. `from` is e4 `(4, 4)`, `target` is e5 `(4, 5)`. New `Board` after `move` of the king from e1 to e4.
  - **Expected output**: `true`.

- **TC-KING-34 — Do not attack the castle square** ( :white_check_mark: )
  - **Method(s) under test**: `attacks(Location, Location, Board)`
  - **State of the system**: White king. `from` is e1, `target` is g1. Kingside cleared, so the king could castle there.
  - **Expected output**: `false`.


### Method under test: `move(Location from, Location to, Board board)` (issue #45)

- **TC-KING-35 — Move the rook with a white kingside castle** ( :white_check_mark: )
  - **Method(s) under test**: `move(Location, Location, Board)`
  - **State of the system**: White king, e1 to g1. Kingside cleared.
  - **Expected output**: the rook that was on h1 is on f1 `(5, 1)`.

- **TC-KING-36 — Move the rook with a white queenside castle** ( :x: )
  - **Method(s) under test**: `move(Location, Location, Board)`
  - **State of the system**: White king, e1 to c1. Queenside cleared.
  - **Expected output**: the rook that was on a1 is on d1 `(3, 1)`.

- **TC-KING-37 — Move the rook with a black kingside castle** ( :x: )
  - **Method(s) under test**: `move(Location, Location, Board)`
  - **State of the system**: Black king, e8 to g8. Black kingside cleared as in TC-KING-19.
  - **Expected output**: the rook that was on h8 is on f8 `(5, 8)`.

- **TC-KING-38 — Move the rook with a black queenside castle** ( :x: )
  - **Method(s) under test**: `move(Location, Location, Board)`
  - **State of the system**: Black king, e8 to c8. Black queenside cleared as in TC-KING-20.
  - **Expected output**: the rook that was on a8 is on d8 `(3, 8)`.

- **TC-KING-39 — Leave the rook in place on a one-square move** ( :x: )
  - **Method(s) under test**: `move(Location, Location, Board)`
  - **State of the system**: White king, e1 to f1 `(5, 1)`. Kingside cleared.
  - **Expected output**: the rook that was on h1 is still on h1.
