package ui;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class PieceImages {
    private static final String STANDARD_FOLDER = "/pieces/";
    private static final String IMAGE_EXTENSION = ".png";

    private final Map<PieceColor, Map<PieceType, BufferedImage>> images =
            new EnumMap<>(PieceColor.class);

    public PieceImages() {
        this(STANDARD_FOLDER);
    }

    PieceImages(String folder) {
        for (PieceColor color : PieceColor.values()) {
            images.put(color, loadColor(folder, color));
        }
    }

    public BufferedImage imageOf(Piece piece) {
        if (piece == null) {
            throw new IllegalArgumentException("Cannot find the image of a null piece");
        }
        return images.get(piece.getColor()).get(piece.getType());
    }

    private static Map<PieceType, BufferedImage> loadColor(String folder, PieceColor color) {
        Map<PieceType, BufferedImage> imagesOfColor = new EnumMap<>(PieceType.class);
        for (PieceType type : PieceType.values()) {
            imagesOfColor.put(type, load(folder + fileName(color, type)));
        }
        return imagesOfColor;
    }

    private static String fileName(PieceColor color, PieceType type) {
        return lowerCase(color.name()) + "-" + lowerCase(type.name()) + IMAGE_EXTENSION;
    }

    private static String lowerCase(String enumName) {
        return enumName.toLowerCase(Locale.ROOT);
    }

    private static BufferedImage load(String path) {
        URL resource = PieceImages.class.getResource(path);
        if (resource == null) {
            throw new PieceImageException(failureMessage(path, "the file is missing"));
        }
        return read(path, resource);
    }

    private static BufferedImage read(String path, URL resource) {
        try {
            return decode(path, resource);
        } catch (IOException e) {
            throw new PieceImageException(failureMessage(path, "the file cannot be read"), e);
        }
    }

    private static BufferedImage decode(String path, URL resource) throws IOException {
        BufferedImage image = ImageIO.read(resource);
        if (image == null) {
            throw new PieceImageException(failureMessage(path, "the file is not an image"));
        }
        return image;
    }

    private static String failureMessage(String path, String reason) {
        return "Cannot load the piece image '" + path + "': " + reason;
    }
}
