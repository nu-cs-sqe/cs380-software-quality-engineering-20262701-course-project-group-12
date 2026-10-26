package domain;

public class Location {
    private final int file;
    private final int rank;

    public Location(int file, int rank) {
        if (file < 0 || file > 7 || rank < 1 || rank > 8) {
            throw new IllegalArgumentException();
        }
        this.file = file;
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }
}
