# BVA Analysis for Board

`Board` owns the 64 squares. The squares are private; an empty square holds no piece rather than null.

- `Board()`: places the starting position. Rank 8, a through h: black rook, knight, bishop, queen, king, bishop, knight, rook. Rank 7: eight black pawns. Ranks 6 through 3: empty. Rank 2: eight white pawns. Rank 1, a through h: white rook, knight, bishop, queen, king, bishop, knight, rook. Each piece is an instance of its own class (`Rook`, `Knight`, `Bishop`, `Queen`, `King`, `Pawn`), so it moves by that class's `canMove` (issue #53). A plain `Piece` cannot move.
- `boolean isOccupied(Location location)`: returns true when a piece stands on that square and false when it is empty. Does not change the board.
- `Piece pieceAt(Location location)`: returns the piece on that square. Requires an occupied square and throws `IllegalArgumentException` otherwise. Does not change the board.
- `void move(Location from, Location to)` (issue #19): puts the piece from `from` on `to`, and `from` becomes empty. A piece already on `to` is replaced; this is the board side of a capture. Moving a piece to its own square leaves the board unchanged. Throws `IllegalArgumentException` when `from` is empty, and then leaves the board unchanged. It does not check whether the move is legal, does not change whose turn it is, and does not touch the UI.
- `boolean holdsUnmovedPiece(Location square)` (issue #45 / US-12): true when a piece stands on `square` and that piece has never been moved by `move`. A piece that moved and came back has moved. Castling uses it to ask whether the king and the rook are on their original squares and have never moved: only the piece placed there at the start can be unmoved on a square.
- `boolean isAttackedByOpponentOf(PieceColor color, Location square)` (issue #45 / US-12): true when at least one piece of the other color `attacks` `square` (see `Piece.md`). Castling uses it for the squares the king stands on, crosses, and lands on. US-11 can use it to find check.

No method returns null. The project rules forbid it, so an empty square is reported by `isOccupied` instead of a null from `pieceAt`. Callers ask `isOccupied` before `pieceAt`. TC9 through TC12 used to expect null from `pieceAt` on the empty ranks; they now check those squares with `isOccupied`.

Issue #19 says that after a move `pieceAt(from)` returns null. Under the no-null rule, the emptied square is checked with `isOccupied(from)`, which returns false.

No method checks its arguments for null, and no case passes null. Our own code is the only caller and never passes null, so a null check would guard against nothing (instructor code review, Week 6). TC21, TC30, TC42, and TC43 used to pass a null location and expect `IllegalArgumentException`; they were removed with the checks. TC44 used to reject a move to null; it now rejects a move from an empty square.

File 0 is the a-file and file 7 is the h-file. The squares are private, so the constructor and `move` are checked through `pieceAt` and `isOccupied`.

`BoardView` is a view, and `PieceType` and `PieceColor` are enums, so they have no BVA file.

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

### `holdsUnmovedPiece(Location square)` (issue #45)

`move` records each piece it moves. The occupant of the square is a set of cases: the piece placed there at the start that has never moved, no piece because it moved away, and the same piece after it moved away and came back. The square comes from the starting position, so the pair of intervals is already covered by `pieceAt`; e1 and g1 are the squares castling cares about.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the board | Collection of 64 squares | a new `Board`, and that board after some `move` calls |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Input: occupant of `square` | Cases | the starting piece, never moved, the white king on e1 `(4, 1)`; empty, after the g1 knight moved to f3 `(5, 3)`; the g1 knight after it moved to f3 and back to g1 `(6, 1)` |
| Output: the answer | Boolean | true; false |

### `isAttackedByOpponentOf(PieceColor color, Location square)` (issue #45)

The pieces are a collection of up to 64 squares. The method looks at every square, so the attackers on the first square, a1, and the last square, h8, are the edges of that collection. The kind of attack is a case: a pawn attacks diagonally even when the square is empty, and a rook attacks along an open file. The attacker's color is a case too: only the other color counts.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the board | Collection of 64 squares | a new `Board`, and that board after some `move` calls |
| Input: `color` | Cases | `WHITE`; `BLACK` |
| Input: `square` | Pointer | a location (no null: callers never pass one) |
| Input: pieces that attack `square` | Collection | none, e4 `(4, 4)` for White on a new board; white pawns only, e3 `(4, 3)` on a new board; one rook on the last square h8 `(7, 8)`; one rook on the first square a1 `(0, 1)` |
| Input: color of the attackers | Cases | the other color; the same color as `color` |
| Output: the answer | Boolean | true; false |

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

### Method under test: `holdsUnmovedPiece(Location square)` (issue #45)

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC58: the starting piece that has never moved | a new `Board`; ask about e1 `(4, 1)` | true | :white_check_mark: |
| TC59: a square the piece moved away from | a new `Board`; move g1 `(6, 1)` to f3 `(5, 3)`; ask about g1 | false | :white_check_mark: |
| TC60: a piece that moved and came back | a new `Board`; move g1 `(6, 1)` to f3 `(5, 3)`, then f3 back to g1; ask about g1 | false | :white_check_mark: |

### Method under test: `isAttackedByOpponentOf(PieceColor color, Location square)` (issue #45)

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC61: no opponent piece attacks the square | a new `Board`; ask whether Black attacks e4 `(4, 4)`, with `color = WHITE` | false | :white_check_mark: |
| TC62: opponent pawns attack an empty square diagonally | a new `Board`; `color = BLACK`, square e3 `(4, 3)`; the white pawns on d2 and f2 attack it | true | :white_check_mark: |
| TC63: only pieces of the same color attack the square | a new `Board`; `color = WHITE`, square e3 `(4, 3)` | false | :white_check_mark: |
| TC64: an opponent rook on the last square, h8 | a new `Board`; move the h7 pawn `(7, 7)` to a6 `(0, 6)`; `color = WHITE`, square h3 `(7, 3)`; the h-file from h7 to h4 is empty | true | :white_check_mark: |
| TC65: an opponent rook on the first square, a1 | a new `Board`; move the a2 pawn `(0, 2)` to h3 `(7, 3)`; `color = BLACK`, square a6 `(0, 6)`; the a-file from a2 to a5 is empty | true | :x: |
