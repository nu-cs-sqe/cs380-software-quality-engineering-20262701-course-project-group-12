# BVA Analysis for PieceImages

`PieceImages` loads the twelve piece images once and hands `BoardView` the image for a piece.

- `PieceImages()`: loads every image from the game's image folder, `/pieces/`.
- `PieceImages(String imageFolder)`: loads every image from `imageFolder`. Each file is named `<color>-<type>.png` in lower case, such as `white-pawn.png`. Throws `PieceImageException` if a file cannot be read, with the read failure (`IOException`) as its cause.
- `BufferedImage imageOf(Piece piece)`: returns the loaded image for the piece's color and type.

A missing file is a packaging bug, not an expected state, so `PieceImages` doesn't check for it and has no case for it (no null checks: see `clean-code-java` errors, Rule 10).

## Steps 1-3: Characteristics and partitions

### `PieceImages(String imageFolder)`

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the files in `imageFolder` | Cases | readable PNG images (`/pieces/`); truncated PNG files that cannot be read (`/pieces-truncated/`) |
| Output: the loaded images | Cases | observed through `imageOf` |
| Output: signal for an unreadable file | Cases | `PieceImageException` whose cause is an `IOException` |

### `imageOf(Piece piece)`

Type and color are small sets of cases, and every pair is possible, so Step 4 uses all-combination.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `piece` type | Cases | `PAWN`, `ROOK`, `KNIGHT`, `BISHOP`, `QUEEN`, `KING` |
| Input: `piece` color | Cases | `WHITE`, `BLACK` |
| Input: `piece` | Pointer | a piece (no null: callers never pass one) |
| Output: the image | Cases | the same pixels as `/pieces/<color>-<type>.png` |

## Step 4: Test cases

### Method under test: `PieceImages(String imageFolder)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: unreadable images | `imageFolder = "/pieces-truncated/"`, whose files are truncated PNGs | throws `PieceImageException` whose cause is an `IOException` | :white_check_mark: |
| TC2: readable images | `PieceImages()` loads `/pieces/` | the images are loaded | implemented in TC3 |

### Method under test: `imageOf(Piece piece)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC3: white pawn | images from `PieceImages()`; `piece` is a white pawn | the pixels of `white-pawn.png` | :white_check_mark: |
| TC4: black pawn | images from `PieceImages()`; `piece` is a black pawn | the pixels of `black-pawn.png` | :white_check_mark: |
| TC5: white rook | images from `PieceImages()`; `piece` is a white rook | the pixels of `white-rook.png` | :white_check_mark: |
| TC6: black rook | images from `PieceImages()`; `piece` is a black rook | the pixels of `black-rook.png` | :white_check_mark: |
| TC7: white knight | images from `PieceImages()`; `piece` is a white knight | the pixels of `white-knight.png` | :white_check_mark: |
| TC8: black knight | images from `PieceImages()`; `piece` is a black knight | the pixels of `black-knight.png` | :white_check_mark: |
| TC9: white bishop | images from `PieceImages()`; `piece` is a white bishop | the pixels of `white-bishop.png` | :white_check_mark: |
| TC10: black bishop | images from `PieceImages()`; `piece` is a black bishop | the pixels of `black-bishop.png` | :white_check_mark: |
| TC11: white queen | images from `PieceImages()`; `piece` is a white queen | the pixels of `white-queen.png` | :white_check_mark: |
| TC12: black queen | images from `PieceImages()`; `piece` is a black queen | the pixels of `black-queen.png` | :white_check_mark: |
| TC13: white king | images from `PieceImages()`; `piece` is a white king | the pixels of `white-king.png` | :x: |
| TC14: black king | images from `PieceImages()`; `piece` is a black king | the pixels of `black-king.png` | :x: |
