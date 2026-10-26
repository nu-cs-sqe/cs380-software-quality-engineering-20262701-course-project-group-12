# BVA Analysis for Location

`Location` is a data class for one square. File 0 is the a-file and file 7 is the h-file. Rank runs from 1 through 8.

- `Location(int file, int rank)`: requires a file in `[0, 7]` and a rank in `[1, 8]`. Throws `IllegalArgumentException` otherwise.
- `int getFile()`: returns the file given at construction.
- `int getRank()`: returns the rank given at construction.

`getFile()` and `getRank()` only return those stored values, so they are checked inside the constructor cases below and do not have separate cases.

## Steps 1-3: Characteristics and partitions

### `Location(int file, int rank)`

File and rank are a pair of intervals, so the corners are included: both lows together, and both highs together. An invalid file is never paired with an invalid rank. Two bad inputs at once would hide which check failed.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `file` | Interval `[0, 7]` | `-1`, `0`, `7`, `8` |
| Input: `rank` | Interval `[1, 8]` | `0`, `1`, `8`, `9` |
| Input: the pair `(file, rank)` | Pair of intervals | both lows `(0, 1)`; both highs `(7, 8)`; low file with high rank `(0, 8)`; high file with low rank `(7, 1)` |
| Output: the location | Interval endpoints | `getFile()` and `getRank()` equal the arguments |
| Output: signal for an invalid argument | Cases | `IllegalArgumentException` |

## Step 4: Test cases (each-choice strategy)

Every value from Step 3 is used in at least one test case. `-1` and `8` are each paired with a valid rank. `0` and `9` are each paired with a valid file.

### Method under test: `Location(int file, int rank)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: both lows, a1 | `file = 0`, `rank = 1` | location is created; `getFile()` is `0`; `getRank()` is `1` | :white_check_mark: |
| TC2: both highs, h8 | `file = 7`, `rank = 8` | location is created; `getFile()` is `7`; `getRank()` is `8` | :white_check_mark: |
| TC3: low file and high rank, a8 | `file = 0`, `rank = 8` | location is created; `getFile()` is `0`; `getRank()` is `8` | :x: |
| TC4: high file and low rank, h1 | `file = 7`, `rank = 1` | location is created; `getFile()` is `7`; `getRank()` is `1` | :x: |
| TC5: file just below the interval | `file = -1`, `rank = 1` | throws `IllegalArgumentException` | :x: |
| TC6: file just above the interval | `file = 8`, `rank = 1` | throws `IllegalArgumentException` | :x: |
| TC7: rank just below the interval | `file = 0`, `rank = 0` | throws `IllegalArgumentException` | :x: |
| TC8: rank just above the interval | `file = 0`, `rank = 9` | throws `IllegalArgumentException` | :x: |

### Method under test: `getFile()`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC9: file of a1 | implemented in TC1 | `0` | implemented in TC1 |
| TC10: file of h8 | implemented in TC2 | `7` | implemented in TC2 |

### Method under test: `getRank()`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC11: rank of a1 | implemented in TC1 | `1` | implemented in TC1 |
| TC12: rank of h8 | implemented in TC2 | `8` | implemented in TC2 |
