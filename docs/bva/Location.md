# BVA Analysis for Location

`Location` is a data class for one square. File 0 is the a-file and file 7 is the h-file. Rank runs from 1 through 8.

- `Location(int file, int rank)`: requires a file in `[0, 7]` and a rank in `[1, 8]`. Throws `IllegalArgumentException` otherwise.
- `int getFile()`: returns the file given at construction.
- `int getRank()`: returns the rank given at construction.
- `boolean isSameSquareAs(Location other)` (issue #34): returns true when `other` has the same file and the same rank, even when it is a different `Location` object. The board view builds a new `Location` for every square it paints, so it uses this to find the selected square.
- `int fileDistanceTo(Location other)` (issue #53): returns how many files apart the two squares are, from `0` through `7`. It is the same in both directions and ignores the ranks.
- `int rankDistanceTo(Location other)` (issue #53): returns how many ranks apart the two squares are, from `0` through `7`. It is the same in both directions and ignores the files.

Every piece class measured these distances with its own private copy of the same arithmetic. They only use the two locations, so they belong on `Location`.

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

### `isSameSquareAs(Location other)`

The file and the rank are compared separately, so each one gets a case where only it differs, by the smallest step, from a1. The matching case uses a second `Location` object so the answer cannot come from comparing references.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `other`'s file compared with this file | Cases | same (`0` and `0`); one apart (`0` and `1`) |
| Input: `other`'s rank compared with this rank | Cases | same (`1` and `1`); one apart (`1` and `2`) |
| Input: `other` | Pointer | a location (no null: callers never pass one) |
| Output: the answer | Boolean | true; false |

### `fileDistanceTo(Location other)` and `rankDistanceTo(Location other)`

The two methods have the same shape, so they share one analysis. The signed difference `other - this` is an interval `[-7, 7]`. Its two edges are the farthest squares in each direction, and `0` is where the sign changes. The output is the interval `[0, 7]`, and those three inputs reach both of its edges. The other coordinate is different in the `0` case, so a method that mixed up file and rank would fail it. The farthest cases have one square at the low edge, where adding the coordinates gives the same answer as subtracting them. The `0` case therefore uses two squares at the high edge, where a sum would give `14` or `16` instead of `0`.

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: `other` | Pointer | a location (no null: callers never pass one) |
| Input: the signed difference `other - this` along the measured coordinate | Interval `[-7, 7]` | `-7`; `0`; `7` |
| Input: the other coordinate | Cases | the same; different |
| Input: the measured coordinate of the two squares | Interval edges | low edge and high edge, in the farthest cases; both at the high edge, in the `0` case |
| Output: the distance | Interval `[0, 7]` | `0`; `7` |

## Step 4: Test cases (each-choice strategy)

Every value from Step 3 is used in at least one test case. `-1` and `8` are each paired with a valid rank. `0` and `9` are each paired with a valid file.

### Method under test: `Location(int file, int rank)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: both lows, a1 | `file = 0`, `rank = 1` | location is created; `getFile()` is `0`; `getRank()` is `1` | :white_check_mark: |
| TC2: both highs, h8 | `file = 7`, `rank = 8` | location is created; `getFile()` is `7`; `getRank()` is `8` | :white_check_mark: |
| TC3: low file and high rank, a8 | `file = 0`, `rank = 8` | location is created; `getFile()` is `0`; `getRank()` is `8` | :white_check_mark: |
| TC4: high file and low rank, h1 | `file = 7`, `rank = 1` | location is created; `getFile()` is `7`; `getRank()` is `1` | :white_check_mark: |
| TC5: file just below the interval | `file = -1`, `rank = 1` | throws `IllegalArgumentException` | :white_check_mark: |
| TC6: file just above the interval | `file = 8`, `rank = 1` | throws `IllegalArgumentException` | :white_check_mark: |
| TC7: rank just below the interval | `file = 0`, `rank = 0` | throws `IllegalArgumentException` | :white_check_mark: |
| TC8: rank just above the interval | `file = 0`, `rank = 9` | throws `IllegalArgumentException` | :white_check_mark: |

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

### Method under test: `isSameSquareAs(Location other)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC13: another object for the same square | a1 `(0, 1)` compared with a second a1 `(0, 1)` | true | :white_check_mark: |
| TC14: one rank apart | a1 `(0, 1)` compared with a2 `(0, 2)` | false | :white_check_mark: |
| TC15: one file apart | a1 `(0, 1)` compared with b1 `(1, 1)` | false | :white_check_mark: |

### Method under test: `fileDistanceTo(Location other)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC16: same file, different rank | h1 `(7, 1)` to h8 `(7, 8)` | `0` | :white_check_mark: |
| TC17: farthest file to the right | a1 `(0, 1)` to h1 `(7, 1)` | `7` | :white_check_mark: |
| TC18: farthest file to the left | h1 `(7, 1)` to a1 `(0, 1)` | `7` | :x: |

### Method under test: `rankDistanceTo(Location other)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC19: same rank, different file | a8 `(0, 8)` to h8 `(7, 8)` | `0` | :x: |
| TC20: farthest rank upward | a1 `(0, 1)` to a8 `(0, 8)` | `7` | :x: |
| TC21: farthest rank downward | a8 `(0, 8)` to a1 `(0, 1)` | `7` | :x: |
