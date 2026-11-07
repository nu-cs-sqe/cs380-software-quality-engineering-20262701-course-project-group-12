package ui;

import domain.piece.Piece;
import domain.piece.PieceColor;
import domain.piece.PieceType;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;

public class PieceImages {
    private static final String IMAGE_FOLDER = "/pieces/";
    private static final String IMAGE_EXTENSION = ".png";

    private final Map<PieceColor, Map<PieceType, BufferedImage>> images =
            new EnumMap<>(PieceColor.class);

    public PieceImages() {
        this(IMAGE_FOLDER);
    }

    public PieceImages(String imageFolder) {
        for (PieceColor color : PieceColor.values()) {
            images.put(color, loadColor(imageFolder, color));
        }
    }

    public BufferedImage imageOf(Piece piece) {
        return images.get(piece.getColor()).get(piece.getType());
    }

    private static Map<PieceType, BufferedImage> loadColor(String imageFolder, PieceColor color) {
        Map<PieceType, BufferedImage> imagesOfColor = new EnumMap<>(PieceType.class);
        for (PieceType type : PieceType.values()) {
            imagesOfColor.put(type, load(imageFolder + fileName(color, type)));
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
        try {
            return read(path);
        } catch (IOException e) {
            String message = "Cannot load the piece image '" + path + "': the file cannot be read";
            throw new PieceImageException(message, e);
        }
    }

    private static BufferedImage read(String path) throws IOException {
        return ImageIO.read(PieceImages.class.getResource(path));
    }
}
