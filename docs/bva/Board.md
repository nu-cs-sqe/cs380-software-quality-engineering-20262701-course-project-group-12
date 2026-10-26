# BVA Analysis for Board

`Board` owns the 64 squares of the starting position. The piece array is private. This feature does not move pieces.

- `Board()`: places the starting position. Rank 8, a through h: black rook, knight, bishop, queen, king, bishop, knight, rook. Rank 7: eight black pawns. Ranks 6 through 3: empty. Rank 2: eight white pawns. Rank 1, a through h: white rook, knight, bishop, queen, king, bishop, knight, rook.
- `Piece pieceAt(Location location)`: returns the piece on that square, or null when the square is empty. Requires a non-null location and throws `IllegalArgumentException` otherwise. Does not change the board.

File 0 is the a-file and file 7 is the h-file. The squares are private, so the constructor is checked through `pieceAt`.

`BoardView` is a view, and `PieceType` and `PieceColor` are enums, so they have no BVA file.

## Steps 1-3: Characteristics and partitions

### `pieceAt(Location location)`

The ranks fall into five blocks. The test values are the edges of those blocks, not a square from the middle of an empty rank. Ranks 4 and 5 are inside the empty block, so rank 3 and rank 6 stand for that block. On a rank where every square is the same kind of piece, file 0 and file 7 stand for that rank. Queen and king are different cases, so d1, e1, d8, and e8 are included even though those files are not 0 or 7.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the board | Collection of 64 squares, the maximum size | a new `Board` in the starting position |
| Input: `location` | Pointer | null; a location |
| Input: file of the location | Interval `[0, 7]` | `0`, `7` |
| Input: rank of the location | Edges of the rank blocks | `1` white back rank; `2` white pawns; `3` first empty rank; `6` last empty rank; `7` black pawns; `8` black back rank |
| Input: the pair `(file, rank)` | Pair of intervals | both lows, a1 `(0, 1)`; both highs, h8 `(7, 8)`; a8 `(0, 8)`; h1 `(7, 1)` |
| Output: the occupant | Pointer, and cases for type and color | null; white and black pawn, rook, knight, bishop, queen, and king |
| Output: the board after the call | Collection | unchanged |

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
| TC8: h2, last file of the white pawns | a new `Board`; location `(7, 2)` | white pawn | :x: |
| TC9: a3, first empty rank at file 0 | a new `Board`; location `(0, 3)` | null | :x: |
| TC10: h3, first empty rank at file 7 | a new `Board`; location `(7, 3)` | null | :x: |
| TC11: a6, last empty rank at file 0 | a new `Board`; location `(0, 6)` | null | :x: |
| TC12: h6, last empty rank at file 7 | a new `Board`; location `(7, 6)` | null | :x: |
| TC13: a7, first file of the black pawns | a new `Board`; location `(0, 7)` | black pawn | :x: |
| TC14: h7, last file of the black pawns | a new `Board`; location `(7, 7)` | black pawn | :x: |
| TC15: a8, low file and high rank | a new `Board`; location `(0, 8)` | black rook | :x: |
| TC16: b8, black knight | a new `Board`; location `(1, 8)` | black knight | :x: |
| TC17: c8, black bishop | a new `Board`; location `(2, 8)` | black bishop | :x: |
| TC18: d8, black queen | a new `Board`; location `(3, 8)` | black queen | :x: |
| TC19: e8, black king | a new `Board`; location `(4, 8)` | black king | :x: |
| TC20: h8, both highs | a new `Board`; location `(7, 8)` | black rook | :x: |
| TC21: null location | a new `Board`; location is null | throws `IllegalArgumentException` | :x: |
| TC22: the board is unchanged | a new `Board`; call `pieceAt` on `(0, 1)` twice | both calls return a white rook | :x: |

### Method under test: `Board()`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC23: starting position at the corners and the rank edges | implemented in TC1 through TC20 | the occupants listed in those cases | implemented in TC1 through TC20 |
