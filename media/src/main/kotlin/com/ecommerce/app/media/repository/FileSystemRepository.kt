package com.ecommerce.app.media.repository

import com.ecommerce.app.media.config.FilesystemConfig
import lombok.SneakyThrows
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Repository
class FileSystemRepository(
    private val filesystemConfig: FilesystemConfig
) {

    private companion object {
        private val log = LoggerFactory.getLogger(FileSystemRepository::class.java)
        private const val DIRECTORY_DOES_NOT_EXIST = "Directory %s does not exist."
    }

    fun persistFile(filename: String, content: ByteArray): String {
        val directory = File(filesystemConfig.directory)
        checkDirectoryExists(directory)
        checkDirectoryPermissions(directory)

        val filePath = buildFilePath(filename)
        filePath.toFile().writeBytes(content)
        log.info("File saved: $filename at $filePath")
        return filePath.toString()
    }

    @SneakyThrows
    fun getFile(filePath: String): InputStream {
        val path = Paths.get(filePath)
        if (!Files.exists(path)) {
            throw IllegalStateException(String.format(DIRECTORY_DOES_NOT_EXIST, filesystemConfig.directory))
        }
        try {
            return Files.newInputStream(path)
        } catch (e: IOException) {
            throw RuntimeException("Failed to read file: $filePath $e")
        }
    }

    fun buildFilePath(filename: String): Path {
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw IllegalArgumentException("Invalid filename")
        }

        val filePath = Paths.get(filesystemConfig.directory, filename).toAbsolutePath().normalize()

        if (!filePath.startsWith(filesystemConfig.directory)) {
            throw IllegalArgumentException("Invalid file path")
        }
        return filePath
    }

    private fun checkDirectoryExists(directory: File) {
        require(directory.exists()) { String.format(DIRECTORY_DOES_NOT_EXIST, filesystemConfig.directory) }
    }

    private fun checkDirectoryPermissions(directory: File) {
        require(directory.canRead() && directory.canWrite()) { "Directory ${directory.absolutePath} is not accessible." }
    }
}
