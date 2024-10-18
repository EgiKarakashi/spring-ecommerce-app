package com.ecommerce.app.media.repository

import com.ecommerce.app.media.config.FilesystemConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.io.File
import java.io.IOException
import java.io.InputStream
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

    // Get the user's home directory
//    private val homeDirectory = System.getProperty("user.home")

    // Full path combining home directory and configured directory
    private val baseDirectory = Paths.get(filesystemConfig.directory).toAbsolutePath().normalize()

    init {
        // Ensure the directory exists at startup
        val directoryFile = baseDirectory.toFile()
        if (!directoryFile.exists()) {
            directoryFile.mkdirs() // Create the directory if it doesn't exist
            log.info("Created directory: ${directoryFile.absolutePath}")
        }
    }

    fun persistFile(filename: String, content: ByteArray): String {
        val directory = baseDirectory.toFile()
        checkDirectoryExists(directory)
        checkDirectoryPermissions(directory)

        val filePath = buildFilePath(filename)
        filePath.toFile().writeBytes(content)
        log.info("File saved: $filename at $filePath")
        return filePath.toString()
    }

    @Throws(IOException::class)
    fun getFile(filePath: String): InputStream {
//        log.info("User home path: $homeDirectory")
        val path = Paths.get(filePath)
        require(path.toFile().exists()) { String.format(DIRECTORY_DOES_NOT_EXIST, filesystemConfig.directory) }

        return path.toFile().inputStream()
    }

    fun buildFilePath(filename: String): Path {
        require(!filename.contains("..") && !filename.contains("/") && !filename.contains("\\")) { "Invalid filename" }
        // Combine the base directory (user home + configured directory) with the filename
        val filePath = baseDirectory.resolve(filename).normalize()
        require(filePath.startsWith(baseDirectory)) { "Invalid file path" }
        return filePath
    }

    private fun checkDirectoryExists(directory: File) {
        require(directory.exists()) { String.format(DIRECTORY_DOES_NOT_EXIST, baseDirectory.toString()) }
    }

    private fun checkDirectoryPermissions(directory: File) {
        require(directory.canRead() && directory.canWrite()) { "Directory ${directory.absolutePath} is not accessible." }
    }
}
