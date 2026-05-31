package net.hoggielibrary.modules.storage.backup;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Backup system for creating and managing data backups.
 */
public final class BackupSystem {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private Path backupDirectory;

    /**
     * Sets the backup directory.
     *
     * @param directory the backup directory path
     */
    public void setBackupDirectory(Path directory) {
        this.backupDirectory = directory;
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create backup directory", e);
        }
    }

    /**
     * Creates a backup of a file or directory.
     *
     * @param source the source path to back up
     * @return the backup path, or null on failure
     */
    public Path createBackup(Path source) {
        if (backupDirectory == null) {
            HoggieLogger.warn("Backup directory not set");
            return null;
        }

        String timestamp = LocalDateTime.now().format(FORMATTER);
        Path backupPath = backupDirectory.resolve(source.getFileName() + "_" + timestamp);

        try {
            if (Files.isDirectory(source)) {
                copyDirectory(source, backupPath);
            } else {
                Files.copy(source, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }
            HoggieLogger.info("Backup created: {}", backupPath);
            return backupPath;
        } catch (IOException e) {
            HoggieLogger.error("Backup failed for {}", source, e);
            return null;
        }
    }

    /**
     * Lists all backups.
     *
     * @return array of backup paths
     */
    public Path[] listBackups() {
        if (backupDirectory == null || !Files.isDirectory(backupDirectory)) {
            return new Path[0];
        }
        try (var files = Files.list(backupDirectory)) {
            return files.toArray(Path[]::new);
        } catch (IOException e) {
            HoggieLogger.error("Failed to list backups", e);
            return new Path[0];
        }
    }

    /**
     * Deletes a backup.
     *
     * @param backupPath the backup path to delete
     * @return true if deleted
     */
    public boolean deleteBackup(Path backupPath) {
        try {
            if (Files.isDirectory(backupPath)) {
                Files.walk(backupPath)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException e) {
                                HoggieLogger.error("Failed to delete {}", p, e);
                            }
                        });
            } else {
                Files.deleteIfExists(backupPath);
            }
            return true;
        } catch (IOException e) {
            HoggieLogger.error("Failed to delete backup {}", backupPath, e);
            return false;
        }
    }

    private void copyDirectory(Path source, Path target) throws IOException {
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
}
