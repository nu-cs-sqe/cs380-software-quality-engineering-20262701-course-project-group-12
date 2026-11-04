package ui;

import domain.piece.PieceColor;
import domain.piece.PieceType;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

public class PieceImages {
    private static final String IMAGE_EXTENSION = ".png";

    PieceImages(String folder) {
        for (PieceColor color : PieceColor.values()) {
            loadColor(folder, color);
        }
    }

    private static void loadColor(String folder, PieceColor color) {
        for (PieceType type : PieceType.values()) {
            load(folder + fileName(color, type));
        }
    }

    private static String fileName(PieceColor color, PieceType type) {
        return lowerCase(color.name()) + "-" + lowerCase(type.name()) + IMAGE_EXTENSION;
    }

    private static String lowerCase(String enumName) {
        return enumName.toLowerCase(Locale.ROOT);
    }

    private static void load(String path) {
        URL resource = PieceImages.class.getResource(path);
        if (resource == null) {
            throw new PieceImageException("Cannot load the piece image '" + path + "': the file is missing");
        }
        read(resource);
    }

    private static void read(URL resource) {
        try {
            ImageIO.read(resource);
        } catch (IOException e) {
            throw new PieceImageException("Cannot load the piece image '" + resource + "': the file cannot be read", e);
        }
    }
}
