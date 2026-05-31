package net.hoggielibrary.util.file;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File system utility functions.
 */
public final class FileUtilities {

    private FileUtilities() {
    }

    /**
     * Reads the entire contents of a file as a string.
     *
     * @param path the file path
     * @return the file contents
     * @throws IOException if reading fails
     */
    public static String readString(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /**
     * Writes a string to a file, creating parent directories if needed.
     *
     * @param path the file path
     * @param content the content to write
     * @throws IOException if writing fails
     */
    public static void writeString(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    /**
     * Reads all lines from a file.
     *
     * @param path the file path
     * @return list of lines
     * @throws IOException if reading fails
     */
    public static List<String> readLines(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    /**
     * Copies a file or directory recursively.
     *
     * @param source the source path
     * @param target the target path
     * @throws IOException if copying fails
     */
    public static void copyRecursive(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)),
                        StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Deletes a file or directory recursively.
     *
     * @param path the path to delete
     * @throws IOException if deletion fails
     */
    public static void deleteRecursive(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (var stream = Files.walk(path)) {
                stream.sorted(java.util.Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException e) {
                                HoggieLogger.error("Failed to delete {}", p, e);
                            }
                        });
            }
        } else {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Gets the file extension from a file name.
     *
     * @param fileName the file name
     * @return the extension (without dot), or empty string
     */
    public static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * Gets the file name without extension.
     *
     * @param fileName the file name
     * @return the file name without extension
     */
    public static String getNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }
}
