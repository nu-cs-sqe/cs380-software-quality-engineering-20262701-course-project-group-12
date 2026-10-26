package ui;

public class WelcomeView {
    private String errorMessage = "";

    public void showNamesRequired() {
        errorMessage = "Both player names are required.";
    }

    public void close() {
    }

    public String errorMessage() {
        return errorMessage;
    }
}
