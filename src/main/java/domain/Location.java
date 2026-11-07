package domain;

public class Location {
    public static final int MIN_FILE = 0;
    public static final int MAX_FILE = 7;
    public static final int MIN_RANK = 1;
    public static final int MAX_RANK = 8;

    private final int file;
    private final int rank;

    public Location(int file, int rank) {
        requireFileOnBoard(file);
        requireRankOnBoard(rank);
        this.file = file;
        this.rank = rank;
    }

    private static void requireFileOnBoard(int file) {
        if (file < MIN_FILE || file > MAX_FILE) {
            throw new IllegalArgumentException("Cannot create a location at file " + file
                    + "; the file must be from " + MIN_FILE + " to " + MAX_FILE);
        }
    }

    private static void requireRankOnBoard(int rank) {
        if (rank < MIN_RANK || rank > MAX_RANK) {
            throw new IllegalArgumentException("Cannot create a location at rank " + rank
                    + "; the rank must be from " + MIN_RANK + " to " + MAX_RANK);
        }
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public boolean isSameSquareAs(Location other) {
        return true;
    }
}
