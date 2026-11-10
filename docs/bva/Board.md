# BVA Analysis for Board

`Board` owns the 64 squares. The squares are private; an empty square holds no piece rather than null.

- `Board()`: places the starting position. Rank 8, a through h: black rook, knight, bishop, queen, king, bishop, knight, rook. Rank 7: eight black pawns. Ranks 6 through 3: empty. Rank 2: eight white pawns. Rank 1, a through h: white rook, knight, bishop, queen, king, bishop, knight, rook. Each piece is an instance of its own class (`Rook`, `Knight`, `Bishop`, `Queen`, `King`, `Pawn`), so it moves by that class's `canMove` (issue #53). A plain `Piece` cannot move.
- `boolean isOccupied(Location location)`: returns true when a piece stands on that square and false when it is empty. Does not change the board.
- `Piece pieceAt(Location location)`: returns the piece on that square. Requires an occupied square and throws `IllegalArgumentException` otherwise. Does not change the board.
- `void move(Location from, Location to)` (issue #19): puts the piece from `from` on `to`, and `from` becomes empty. A piece already on `to` is replaced; this is the board side of a capture. Moving a piece to its own square leaves the board unchanged. Throws `IllegalArgumentException` when `from` is empty, and then leaves the board unchanged. It does not check whether the move is legal, does not change whose turn it is, and does not touch the UI.
- `void promote(Location square, PieceType type)` (issue #46): replaces the pawn on `square` with a new piece of the chosen type and the pawn's color: a `Queen`, `Rook`, `Bishop`, or `Knight`, so the promoted piece moves by its own rules. The choice does not depend on which pieces have been captured. Throws `IllegalArgumentException` when `type` is `KING` or `PAWN`, and then leaves the board unchanged. It does not check that the pawn is on its last rank; `BoardController` calls it only after a pawn arrives there.
- `void move(Location from, Location to)`, en passant (issue #47 / GitHub US-14): after each move the board remembers whether it was a pawn moving two ranks, the only move that can be captured en passant. When a pawn moves diagonally onto an empty square, and the square beside `from` on the file of `to` holds the pawn that can be captured en passant, that pawn is removed. This is the board side of an en passant capture, the way replacing a piece is the board side of a normal capture.
- `boolean canBeCapturedEnPassant(Location square)` (issue #47): returns true when `square` holds the pawn that moved two ranks on the last move, and false otherwise. The chance lasts one move: any later move ends it.
- `boolean isInCheck(PieceColor color)` (issue #48 / GitHub US-15, docs US-15): returns true when a piece of the other color `canMove` onto the square of the king of `color`. Does not change the board.
- `boolean isCheckmated(PieceColor color)` (issue #48): returns true when the king of `color` is in check and no move by a piece of `color` leaves that king out of check. A move counts only when the piece `canMove` there, and it is tried on a copy, so the board itself does not change. The king is never removed.
- `Position position(PieceColor colorToMove)` (issue #59 / GitHub US-19, docs US-18): returns the position on the board, with `colorToMove` to move, for the repetition rules. Its cases are in `Position.md`, which checks the positions it builds.
- `Position positionAfterMove(Location from, Location to, PieceColor colorToMove)` (issue #59): returns the position the board would have after `move(from, to)`, with `colorToMove` to move. The move is made on a copy, so the board does not change.

No method returns null. The project rules forbid it, so an empty square is reported by `isOccupied` instead of a null from `pieceAt`. Callers ask `isOccupied` before `pieceAt`. TC9 through TC12 used to expect null from `pieceAt` on the empty ranks; they now check those squares with `isOccupied`.

Issue #19 says that after a move `pieceAt(from)` returns null. Under the no-null rule, the emptied square is checked with `isOccupied(from)`, which returns false.

No method checks its arguments for null, and no case passes null. Our own code is the only caller and never passes null, so a null check would guard against nothing (instructor code review, Week 6). TC21, TC30, TC42, and TC43 used to pass a null location and expect `IllegalArgumentException`; they were removed with the checks. TC44 used to reject a move to null; it now rejects a move from an empty square.

File 0 is the a-file and file 7 is the h-file. The squares are private, so the constructor and `move` are checked through `pieceAt` and `isOccupied`.

`BoardView` is a view, and `PieceType` and `PieceColor` are enums, so they have no BVA file.

The `isInCheck` and `isCheckmated` cases use the real pieces that issue #53 puts on the starting board, so each piece's own `canMove` decides what it attacks. Out of scope: refusing a move that leaves the mover's own king in check and showing that a king is in check (issue #44), and stalemate (issue #49).

## Steps 1-3: Characteristics and partitions

### `pieceAt(Location location)`

The ranks fall into five blocks. The test values are the edges of those blocks, not a square from the middle of an empty rank. Ranks 4 and 5 are inside the empty block, so rank 3 and rank 6 stand for that block. On a rank where every square is the same kind of piece, file 0 and file 7 stand for that rank. Queen and king are different cases, so d1, e1, d8, and e8 are included even though those files are not 0 or 7. The empty block is checked with `isOccupied`; for `pieceAt` one empty square, a3, stands for the rejected case.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the board | Collection of 64 squares, the maximum size | a new `Board` in the starting position |
| Input: `location` | Pointer | a location (no null: callers never pass one) |
| Input: file of the location | Interval `[0, 7]` | `0`, `7` |
| Input: rank of the location | Edges of the rank blocks | `1` white back rank; `2` white pawns; `3` first empty rank; `7` black pawns; `8` black back rank |
| Input: the pair `(file, rank)` | Pair of intervals | both lows, a1 `(0, 1)`; both highs, h8 `(7, 8)`; a8 `(0, 8)`; h1 `(7, 1)` |
| Output: the occupant | Cases for type and color | white and black pawn, rook, knight, bishop, queen, and king |
| Output: signal for an empty square | Cases | `IllegalArgumentException`, on the empty square a3 |
| Output: the board after the call | Collection | unchanged |

### `isOccupied(Location location)`

The same rank blocks apply. Ranks 3 and 6 stand for the empty block, at file 0 and file 7. The occupied ranks are checked at their edges: the four corners for the pair of intervals, plus h2 and a7 for the pawn ranks.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the board | Collection of 64 squares | a new `Board` in the starting position |
| Input: `location` | Pointer | a location (no null: callers never pass one) |
| Input: file of the location | Interval `[0, 7]` | `0`, `7` |
| Input: rank of the location | Edges of the rank blocks | `1` white back rank; `2` white pawns; `3` first empty rank; `6` last empty rank; `7` black pawns; `8` black back rank |
| Input: the pair `(file, rank)` | Pair of intervals | both lows, a1 `(0, 1)`; both highs, h8 `(7, 8)`; a8 `(0, 8)`; h1 `(7, 1)` |
| Output: the answer | Boolean | true; false |

### `Board()`: the class of each starting piece (issue #53)

Type and color are small sets of cases, and every pair is on the starting board, so Step 4 uses all-combination: one case for each of the 6 types in each of the 2 colors. The white pieces are checked on the a-side of rank 1 and on a2. The black pieces are checked on the h-side of rank 8 and on h7. Together the two sides use every entry of the back rank: a1 through e1 for the first five files, f8 through h8 for the last three. d8 and e8 are the only black queen and king.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the board | Collection of 64 squares | a new `Board` in the starting position |
| Input: the square read with `pieceAt` | Cases, one per type and color | white: rook a1, knight b1, bishop c1, queen d1, king e1, pawn a2; black: rook h8, knight g8, bishop f8, queen d8, king e8, pawn h7 |
| Output: the class of the piece on that square | Cases | `Rook`, `Knight`, `Bishop`, `Queen`, `King`, `Pawn` |

### `move(Location from, Location to)`

The starting position has a rook on every corner, so each corner can be both a `from` and a `to` square. Each square gets all four corners of its pair of intervals: a1 and h8 move to empty squares and capture; a8 and h1 are captured and then move themselves. a4 and h5 are the empty `to` squares on the same files. The moved piece is checked with `assertSame`, so the test knows it is the same object and not another rook of that color.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the board | Collection of 64 squares | a new `Board` in the starting position |
| Input: `from` | Pointer | a location (no null: callers never pass one) |
| Input: `to` | Pointer | a location (no null: callers never pass one) |
| Input: the pair `(file, rank)` of `from` | Pair of intervals | both lows, a1 `(0, 1)`; both highs, h8 `(7, 8)`; low file and high rank, a8 `(0, 8)`; high file and low rank, h1 `(7, 1)` |
| Input: the pair `(file, rank)` of `to` | Pair of intervals | both lows, a1 `(0, 1)`; both highs, h8 `(7, 8)`; low file and high rank, a8 `(0, 8)`; high file and low rank, h1 `(7, 1)` |
| Input: occupant of `from` | Cases | a piece; empty, a4 `(0, 4)` |
| Input: occupant of `to` | Cases | empty, a4 `(0, 4)` and h5 `(7, 5)`; a piece of the other color |
| Input: `from` compared with `to` | Cases | different squares; the same square, a1 to a1 |
| Output: occupant of `to` after the call | Pointer | the same piece that was on `from` |
| Output: `from` after the call | Boolean, through `isOccupied` | false; still the same piece, when `to` is the same square |
| Output: the board after a rejected move | Collection | unchanged |
| Output: signal for a rejected move | Cases | none; `IllegalArgumentException` |
| Output: the number of pieces after a capture (issue #33) | Collection, counted with `isOccupied` over the 64 squares | 31, one fewer than the starting 32 |

### `promote(Location square, PieceType type)`

A pawn is put on its last rank with `move`: the a2 pawn to a8 for White, and the h7 pawn to h1 for Black. The four allowed types each appear once for White, and the queen is repeated for Black, so each color appears. The promoted piece is checked with `assertInstanceOf`, because the piece must be the class that moves by that type's rules, not a plain `Piece`.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Input: occupant of `square` | Cases | a white pawn on a8 `(0, 8)`; a black pawn on h1 `(7, 1)` |
| Input: `type` | Cases | the choices `QUEEN`, `ROOK`, `BISHOP`, `KNIGHT`; not choices, `KING` and `PAWN` |
| Output: occupant of `square` after the call | Cases | a `Queen`, `Rook`, `Bishop`, or `Knight` of the pawn's color; the same pawn, after a rejected choice |
| Output: signal for a rejected promotion | Cases | none; `IllegalArgumentException` |

### `canBeCapturedEnPassant(Location square)`

The board is set up with `move`, which does not check whether a move is legal. A two-rank pawn move is the only move that can be captured en passant, so the cases change one thing at a time: the distance, the type of the piece, the color, the square asked about, and whether another move came after it. The case numbers start at TC66, after the piece-class cases (TC46 to TC57, issue #53) and the promotion cases (TC58 to TC65, issue #46).

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the last move | Cases | a white pawn two ranks, e2 `(4, 2)` to e4 `(4, 4)`; a black pawn two ranks, d7 `(3, 7)` to d5 `(3, 5)`; a pawn one rank, e2 to e3 `(4, 3)`; a rook two ranks, a1 `(0, 1)` to a3 `(0, 3)` |
| Input: a move after the two-rank pawn move | Cases | none; the black knight b8 `(1, 8)` to c6 `(2, 6)` |
| Input: `square` | Cases | the square the pawn moved to; another square, d4 `(3, 4)` |
| Output: the answer | Boolean | true; false |

### `move(Location from, Location to)`, en passant

A white pawn is put on e5 `(4, 5)` with two moves, e2 to e4 and e4 to e5. The black pawn beside it then moves d7 to d5, or reaches d5 with two one-rank moves, which cannot be captured en passant. The capturing move is e5 to d6 `(3, 6)`, the square the black pawn passed over.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the piece moving diagonally onto the empty d6 | Cases | the white pawn from e5; a white bishop on e5 (moved there from c1 `(2, 1)`) |
| Input: the black pawn on d5 | Cases | can be captured en passant (moved d7 to d5); cannot (moved d7 to d6 `(3, 6)` to d5) |
| Output: d5 after the move | Boolean, through `isOccupied` | false, the captured pawn is removed; true, nothing is removed |
| Output: d6 after the move | Pointer | the same white pawn that moved from e5 |

### `isInCheck(PieceColor color)` and `isCheckmated(PieceColor color)`

Each position is set up with `move` from a new `Board`, using real games, so every position can happen in play. `move` does not check legality, so the moves are written as squares.

- Fool's mate: f2 `(5, 2)` to f3 `(5, 3)`, e7 `(4, 7)` to e5 `(4, 5)`, g2 `(6, 2)` to g4 `(6, 4)`, d8 `(3, 8)` to h4 `(7, 4)`. The black queen checks the white king on e1, and White has no reply.
- Queen check on Black: e2 `(4, 2)` to e4 `(4, 4)`, f7 `(5, 7)` to f6 `(5, 6)`, d1 `(3, 1)` to h5 `(7, 5)`. The white queen checks the black king on e8, and Black can block with g7 `(6, 7)` to g6 `(6, 6)`.
- Sam Loyd's stalemate: e2 to e3, a7 to a5, d1 to h5, a8 to a6, h5 to a5, h7 to h5, h2 to h4, a6 to h6, a5 to c7, f7 to f6, c7 to d7, e8 to f7, d7 to b7, d8 to d3, b7 to b8, d3 to h7, b8 to c8, f7 to g6, c8 to e6. The black king on g6 is not in check, and Black has no legal move: the f6 pawn is pinned by the white queen on e6, and every other black piece is blocked.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `color` | Cases | White; Black |
| Input: whether a piece of the other color reaches the king of `color` | Boolean | no (a new `Board`; Fool's mate for Black); yes (Fool's mate for White; the queen check on Black) |
| Input: whether a move of `color` leaves its king out of check | Boolean | yes (a new `Board`; the queen check on Black); no (Fool's mate for White; Sam Loyd's stalemate) |
| Input: a move that `canMove` allows but that leaves the king in check | Cases | a reply that does not answer the check (Fool's mate); a pinned piece (the f6 pawn in Sam Loyd's stalemate) |
| Output: `isInCheck` | Boolean | true; false |
| Output: `isCheckmated` | Boolean | true only when the king is in check and no move leaves it out of check; false otherwise |
| Output: the board after `isCheckmated` | Collection | unchanged; the king stays on its square |

### `positionAfterMove(Location from, Location to, PieceColor colorToMove)`

The move is the white knight from g1 `(6, 1)` to f3 `(5, 3)`, with Black to move after it. The expected position is built by making the same move on a second new `Board` and asking it for `position(BLACK)`.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `from` and `to` | Pointer | locations (no null: callers never pass one) |
| Input: `colorToMove` | Cases | Black, the player to move after a White move |
| Output: the position | Cases | the same as the board's position after `move(g1, f3)` |
| Output: the board after the call | Collection | unchanged: g1 still holds the knight, and f3 is empty |

## Step 4: Test cases (each-choice strategy)

Every value from Step 3 is used in at least one test case. A square is written as file, then rank.

### Method under test: `pieceAt(Location location)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: a1, both lows | a new `Board`; location `(0, 1)` | white rook | :white_check_mark: |
| TC2: h1, high file and low rank | a new `Board`; location `(7, 1)` | white rook | :white_check_mark: |
| TC3: b1, white knight | a new `Board`; location `(1, 1)` | white knight | :white_check_mark: |
| TC4: c1, white bishop | a new `Board`; location `(2, 1)` | white bishop | :white_check_mark: |
| TC5: d1, white queen | a new `Board`; location `(3, 1)` | white queen | :white_check_mark: |
| TC6: e1, white king | a new `Board`; location `(4, 1)` | white king | :white_check_mark: |
| TC7: a2, first file of the white pawns | a new `Board`; location `(0, 2)` | white pawn | :white_check_mark: |
| TC8: h2, last file of the white pawns | a new `Board`; location `(7, 2)` | white pawn | :white_check_mark: |
| TC13: a7, first file of the black pawns | a new `Board`; location `(0, 7)` | black pawn | :white_check_mark: |
| TC14: h7, last file of the black pawns | a new `Board`; location `(7, 7)` | black pawn | :white_check_mark: |
| TC15: a8, low file and high rank | a new `Board`; location `(0, 8)` | black rook | :white_check_mark: |
| TC16: b8, black knight | a new `Board`; location `(1, 8)` | black knight | :white_check_mark: |
| TC17: c8, black bishop | a new `Board`; location `(2, 8)` | black bishop | :white_check_mark: |
| TC18: d8, black queen | a new `Board`; location `(3, 8)` | black queen | :white_check_mark: |
| TC19: e8, black king | a new `Board`; location `(4, 8)` | black king | :white_check_mark: |
| TC20: h8, both highs | a new `Board`; location `(7, 8)` | black rook | :white_check_mark: |
| TC22: the board is unchanged | a new `Board`; call `pieceAt` on `(0, 1)` twice | both calls return a white rook | :white_check_mark: |
| TC31: a3, an empty square | a new `Board`; location `(0, 3)` | throws `IllegalArgumentException` | :white_check_mark: |

### Method under test: `isOccupied(Location location)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC9: a3, first empty rank at file 0 | a new `Board`; location `(0, 3)` | false | :white_check_mark: |
| TC10: h3, first empty rank at file 7 | a new `Board`; location `(7, 3)` | false | :white_check_mark: |
| TC11: a6, last empty rank at file 0 | a new `Board`; location `(0, 6)` | false | :white_check_mark: |
| TC12: h6, last empty rank at file 7 | a new `Board`; location `(7, 6)` | false | :white_check_mark: |
| TC24: a1, both lows | a new `Board`; location `(0, 1)` | true | :white_check_mark: |
| TC25: h1, high file and low rank | a new `Board`; location `(7, 1)` | true | :white_check_mark: |
| TC26: h2, last file of the white pawns | a new `Board`; location `(7, 2)` | true | :white_check_mark: |
| TC27: a7, first file of the black pawns | a new `Board`; location `(0, 7)` | true | :white_check_mark: |
| TC28: a8, low file and high rank | a new `Board`; location `(0, 8)` | true | :white_check_mark: |
| TC29: h8, both highs | a new `Board`; location `(7, 8)` | true | :white_check_mark: |

### Method under test: `Board()`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC23: starting position at the corners and the rank edges | implemented in TC1 through TC20 | the occupants listed in those cases | implemented in TC1 through TC20 |
| TC46: white rook class | a new `Board`; read a1 `(0, 1)` | the piece is a `Rook` | :white_check_mark: |
| TC47: black rook class | a new `Board`; read h8 `(7, 8)` | the piece is a `Rook` | :white_check_mark: |
| TC48: white knight class | a new `Board`; read b1 `(1, 1)` | the piece is a `Knight` | :white_check_mark: |
| TC49: black knight class | a new `Board`; read g8 `(6, 8)` | the piece is a `Knight` | :white_check_mark: |
| TC50: white bishop class | a new `Board`; read c1 `(2, 1)` | the piece is a `Bishop` | :white_check_mark: |
| TC51: black bishop class | a new `Board`; read f8 `(5, 8)` | the piece is a `Bishop` | :white_check_mark: |
| TC52: white queen class | a new `Board`; read d1 `(3, 1)` | the piece is a `Queen` | :white_check_mark: |
| TC53: black queen class | a new `Board`; read d8 `(3, 8)` | the piece is a `Queen` | :white_check_mark: |
| TC54: white king class | a new `Board`; read e1 `(4, 1)` | the piece is a `King` | :white_check_mark: |
| TC55: black king class | a new `Board`; read e8 `(4, 8)` | the piece is a `King` | :white_check_mark: |
| TC56: white pawn class | a new `Board`; read a2 `(0, 2)` | the piece is a `Pawn` | :white_check_mark: |
| TC57: black pawn class | a new `Board`; read h7 `(7, 7)` | the piece is a `Pawn` | :white_check_mark: |

### Method under test: `move(Location from, Location to)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC32: from both lows to an empty square | a new `Board`; move a1 `(0, 1)` to a4 `(0, 4)` | `pieceAt(a4)` is the white rook that was on a1 | :white_check_mark: |
| TC33: from both lows leaves that square empty | a new `Board`; move a1 `(0, 1)` to a4 `(0, 4)` | `isOccupied(a1)` is false | :white_check_mark: |
| TC34: from both highs to an empty square | a new `Board`; move h8 `(7, 8)` to h5 `(7, 5)` | `pieceAt(h5)` is the black rook that was on h8 | :white_check_mark: |
| TC35: from both highs leaves that square empty | a new `Board`; move h8 `(7, 8)` to h5 `(7, 5)` | `isOccupied(h8)` is false | :white_check_mark: |
| TC36: to low file and high rank, replacing a black piece | a new `Board`; move a1 `(0, 1)` to a8 `(0, 8)` | `pieceAt(a8)` is the white rook that was on a1 | :white_check_mark: |
| TC37: to high file and low rank, replacing a white piece | a new `Board`; move h8 `(7, 8)` to h1 `(7, 1)` | `pieceAt(h1)` is the black rook that was on h8 | :white_check_mark: |
| TC38: from low file and high rank, to both lows | a new `Board`; move a8 `(0, 8)` to a1 `(0, 1)` | `pieceAt(a1)` is the black rook that was on a8 | :white_check_mark: |
| TC39: from high file and low rank, to both highs | a new `Board`; move h1 `(7, 1)` to h8 `(7, 8)` | `pieceAt(h8)` is the white rook that was on h1 | :white_check_mark: |
| TC40: to the same square | a new `Board`; move a1 `(0, 1)` to a1 `(0, 1)` | `pieceAt(a1)` is still the white rook | :white_check_mark: |
| TC41: from an empty square | a new `Board`; move a4 `(0, 4)` to a5 `(0, 5)` | throws `IllegalArgumentException` | :white_check_mark: |
| TC44: a rejected move leaves the board unchanged | a new `Board`; move a4 `(0, 4)` to a1 `(0, 1)` | `pieceAt(a1)` is still the white rook | :white_check_mark: |
| TC45: a capture takes the captured piece off the board (issue #33) | a new `Board`; move a1 `(0, 1)` to a8 `(0, 8)`, so the white rook captures the black rook | 31 of the 64 squares are occupied | :white_check_mark: |

### Method under test: `promote(Location square, PieceType type)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC58: promote a white pawn to a queen | a new `Board`; move a2 `(0, 2)` to a8 `(0, 8)`; promote a8 to `QUEEN` | `pieceAt(a8)` is a `Queen`; its color is `WHITE` | :white_check_mark: |
| TC59: promote a white pawn to a rook | as TC58; promote a8 to `ROOK` | `pieceAt(a8)` is a `Rook`; its color is `WHITE` | :white_check_mark: |
| TC60: promote a white pawn to a bishop | as TC58; promote a8 to `BISHOP` | `pieceAt(a8)` is a `Bishop`; its color is `WHITE` | :white_check_mark: |
| TC61: promote a white pawn to a knight | as TC58; promote a8 to `KNIGHT` | `pieceAt(a8)` is a `Knight`; its color is `WHITE` | :white_check_mark: |
| TC62: promote a black pawn to a queen | a new `Board`; move h7 `(7, 7)` to h1 `(7, 1)`; promote h1 to `QUEEN` | `pieceAt(h1)` is a `Queen`; its color is `BLACK` | :white_check_mark: |
| TC63: a king is not a choice | as TC58; promote a8 to `KING` | throws `IllegalArgumentException` | :white_check_mark: |
| TC64: a pawn is not a choice | as TC58; promote a8 to `PAWN` | throws `IllegalArgumentException` | :white_check_mark: |
| TC65: a rejected choice leaves the pawn | as TC63 | `pieceAt(a8)` is still the white pawn that moved from a2 | :white_check_mark: |

### Method under test: `canBeCapturedEnPassant(Location square)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC66: a white pawn that just moved two ranks | a new `Board`; move e2 `(4, 2)` to e4 `(4, 4)`; ask about e4 | true | :white_check_mark: |
| TC67: a pawn that just moved one rank | a new `Board`; move e2 to e3 `(4, 3)`; ask about e3 | false | :white_check_mark: |
| TC68: a piece that is not a pawn moving two ranks | a new `Board`; move a1 `(0, 1)` to a3 `(0, 3)`; ask about a3 | false | :white_check_mark: |
| TC69: another move ends the chance | a new `Board`; move e2 to e4, then b8 `(1, 8)` to c6 `(2, 6)`; ask about e4 | false | :white_check_mark: |
| TC70: a black pawn that just moved two ranks | a new `Board`; move d7 `(3, 7)` to d5 `(3, 5)`; ask about d5 | true | :white_check_mark: |
| TC71: another square than the pawn that moved | a new `Board`; move e2 to e4; ask about d4 `(3, 4)` | false | :white_check_mark: |

### Method under test: `move(Location from, Location to)`, en passant

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC72: an en passant capture removes the captured pawn | a new `Board`; move e2 `(4, 2)` to e4, e4 to e5 `(4, 5)`, and d7 `(3, 7)` to d5 `(3, 5)`; move e5 to d6 `(3, 6)` | `isOccupied(d5)` is false | :white_check_mark: |
| TC73: the capturing pawn lands on the square passed over | as TC72 | `pieceAt(d6)` is the white pawn that moved from e2 | :white_check_mark: |
| TC74: a pawn that moved one rank at a time is not removed | a new `Board`; move e2 to e4, e4 to e5, d7 to d6, and d6 to d5; move e5 to d6 | `isOccupied(d5)` is true | :white_check_mark: |
| TC75: a piece that is not a pawn removes nothing | a new `Board`; move c1 `(2, 1)` to e5, and d7 to d5; move e5 to d6 | `isOccupied(d5)` is true | :white_check_mark: |

### Method under test: `isInCheck(PieceColor color)`

The case numbers start at TC76, after the piece-class cases (TC46 to TC57, issue #53), the promotion cases (TC58 to TC65, issue #46), and the en passant cases (TC66 to TC75, issue #47).

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC76: no piece reaches the king | a new `Board`; ask about White | false | :white_check_mark: |
| TC77: a piece of the other color reaches the white king | the Fool's mate position; ask about White | true | :white_check_mark: |
| TC78: the other king is not in check | the Fool's mate position; ask about Black | false | :white_check_mark: |
| TC79: a piece of the other color reaches the black king | the queen check on Black; ask about Black | true | :white_check_mark: |

### Method under test: `isCheckmated(PieceColor color)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC80: not in check, with legal moves | a new `Board`; ask about White | false | :white_check_mark: |
| TC81: in check with no reply | the Fool's mate position; ask about White | true | :white_check_mark: |
| TC82: in check with a reply that blocks the check | the queen check on Black; ask about Black | false | :white_check_mark: |
| TC83: not in check and no legal move | Sam Loyd's stalemate; ask about Black | false | :white_check_mark: |
| TC84: the query leaves the board unchanged | the Fool's mate position; ask about White | `pieceAt(h4)` is still the black queen that moved from d8, `pieceAt(e1)` is still the white king, and 32 squares are occupied | :white_check_mark: |

### Method under test: `position(PieceColor colorToMove)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC85: the position on the board | implemented in Position TC1 to TC9 | the positions those cases compare | implemented in Position TC1 to TC9 |

### Method under test: `positionAfterMove(Location from, Location to, PieceColor colorToMove)`

The case numbers continue after the checkmate cases (TC76 to TC84, issue #48).

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC86: the position after a move | a new `Board`; `positionAfterMove(g1, f3, BLACK)`, compared with a second new `Board` after `move(g1, f3)` and `position(BLACK)` | `isSameAs` returns true | :x: |
| TC87: the board does not change | a new `Board`; `positionAfterMove(g1, f3, BLACK)` | `pieceAt(g1)` is still the white knight; `isOccupied(f3)` is false | :x: |
