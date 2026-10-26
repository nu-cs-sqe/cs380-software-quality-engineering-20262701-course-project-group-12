# BVA Analysis for Piece

`Piece` stores one piece's type and color. Both are fixed after construction. This feature has no move behavior on `Piece`.

- `Piece(PieceType type, PieceColor color)`: both arguments are required. Throws `IllegalArgumentException` if either is null.
- `PieceType getType()`: returns the type given at construction.
- `PieceColor getColor()`: returns the color given at construction.

`getType()` and `getColor()` only return those stored values, so they are checked inside the constructor cases below.

## Steps 1-3: Characteristics and partitions

### `Piece(PieceType type, PieceColor color)`

Type and color are small sets of cases, and every pair is possible, so Step 4 uses all-combination for the two valid sets. A null argument is tested alone. Both arguments null at once would hide which check failed.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `type` | Cases, and a pointer | `PAWN`, `ROOK`, `KNIGHT`, `BISHOP`, `QUEEN`, `KING`, null |
| Input: `color` | Cases, and a pointer | `WHITE`, `BLACK`, null |
| Output: the piece | Cases | `getType()` equals `type`; `getColor()` equals `color` |
| Output: signal for a missing argument | Cases | `IllegalArgumentException` |

## Step 4: Test cases (all-combination for the valid pairs)

### Method under test: `Piece(PieceType type, PieceColor color)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: white pawn | `type = PAWN`, `color = WHITE` | piece is created; `getType()` is `PAWN`; `getColor()` is `WHITE` | :white_check_mark: |
| TC2: black pawn | `type = PAWN`, `color = BLACK` | piece is created; `getType()` is `PAWN`; `getColor()` is `BLACK` | :white_check_mark: |
| TC3: white rook | `type = ROOK`, `color = WHITE` | piece is created; `getType()` is `ROOK`; `getColor()` is `WHITE` | :white_check_mark: |
| TC4: black rook | `type = ROOK`, `color = BLACK` | piece is created; `getType()` is `ROOK`; `getColor()` is `BLACK` | :white_check_mark: |
| TC5: white knight | `type = KNIGHT`, `color = WHITE` | piece is created; `getType()` is `KNIGHT`; `getColor()` is `WHITE` | :white_check_mark: |
| TC6: black knight | `type = KNIGHT`, `color = BLACK` | piece is created; `getType()` is `KNIGHT`; `getColor()` is `BLACK` | :white_check_mark: |
| TC7: white bishop | `type = BISHOP`, `color = WHITE` | piece is created; `getType()` is `BISHOP`; `getColor()` is `WHITE` | :white_check_mark: |
| TC8: black bishop | `type = BISHOP`, `color = BLACK` | piece is created; `getType()` is `BISHOP`; `getColor()` is `BLACK` | :white_check_mark: |
| TC9: white queen | `type = QUEEN`, `color = WHITE` | piece is created; `getType()` is `QUEEN`; `getColor()` is `WHITE` | :x: |
| TC10: black queen | `type = QUEEN`, `color = BLACK` | piece is created; `getType()` is `QUEEN`; `getColor()` is `BLACK` | :x: |
| TC11: white king | `type = KING`, `color = WHITE` | piece is created; `getType()` is `KING`; `getColor()` is `WHITE` | :x: |
| TC12: black king | `type = KING`, `color = BLACK` | piece is created; `getType()` is `KING`; `getColor()` is `BLACK` | :x: |
| TC13: missing type | `type = null`, `color = WHITE` | throws `IllegalArgumentException` | :x: |
| TC14: missing color | `type = PAWN`, `color = null` | throws `IllegalArgumentException` | :x: |

### Method under test: `getType()`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC15: type of a white pawn | implemented in TC1 | `PAWN` | implemented in TC1 |
| TC16: type of a black king | implemented in TC12 | `KING` | implemented in TC12 |

### Method under test: `getColor()`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC17: color of a white pawn | implemented in TC1 | `WHITE` | implemented in TC1 |
| TC18: color of a black king | implemented in TC12 | `BLACK` | implemented in TC12 |
