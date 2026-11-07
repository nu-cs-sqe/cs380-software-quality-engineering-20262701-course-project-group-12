# BVA Analysis for Piece

`Piece` stores one piece's type and color. Both are fixed after construction. A piece whose type has no movement rules yet cannot move; each piece story adds a subclass that overrides `canMove` (issue #34).

- `Piece(PieceType type, PieceColor color)`: stores the type and the color. It does not check them for null; our code never passes null (instructor code review, Week 6).
- `PieceType getType()`: returns the type given at construction.
- `PieceColor getColor()`: returns the color given at construction.
- `boolean canMove(Location from, Location to, Board board)`: returns `false`, because a plain `Piece` has no movement rules. It does not look at the board.

`getType()` and `getColor()` only return those stored values, so they are checked inside the constructor cases below.

## Steps 1-3: Characteristics and partitions

### `Piece(PieceType type, PieceColor color)`

Type and color are small sets of cases, and every pair is possible, so Step 4 uses all-combination for the two valid sets. No case passes null. TC13 and TC14 used to pass a null type or color and expect `IllegalArgumentException`; they were removed with the check.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `type` | Cases | `PAWN`, `ROOK`, `KNIGHT`, `BISHOP`, `QUEEN`, `KING` |
| Input: `color` | Cases | `WHITE`, `BLACK` |
| Output: the piece | Cases | `getType()` equals `type`; `getColor()` equals `color` |

### `canMove(Location from, Location to, Board board)`

A plain `Piece` answers the same for every input, so one case stands for all of them. `Board` is mocked with EasyMock and expects no calls, which shows the answer does not depend on the board.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the piece | Cases | a plain `Piece` (a type without its own subclass), such as a white queen |
| Input: `from`, `to` | Pointer | two locations (no null: callers never pass one) |
| Input: `board` | Pointer | a board (no null: callers never pass one) |
| Output: the answer | Boolean | `false` |

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
| TC9: white queen | `type = QUEEN`, `color = WHITE` | piece is created; `getType()` is `QUEEN`; `getColor()` is `WHITE` | :white_check_mark: |
| TC10: black queen | `type = QUEEN`, `color = BLACK` | piece is created; `getType()` is `QUEEN`; `getColor()` is `BLACK` | :white_check_mark: |
| TC11: white king | `type = KING`, `color = WHITE` | piece is created; `getType()` is `KING`; `getColor()` is `WHITE` | :white_check_mark: |
| TC12: black king | `type = KING`, `color = BLACK` | piece is created; `getType()` is `KING`; `getColor()` is `BLACK` | :white_check_mark: |

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

### Method under test: `canMove(Location from, Location to, Board board)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC19: a piece without movement rules | a white queen as a plain `Piece`; `from` is d1 `(3, 1)`, `to` is d3 `(3, 3)`; a mocked `Board` that expects no calls | `false` | :white_check_mark: |
