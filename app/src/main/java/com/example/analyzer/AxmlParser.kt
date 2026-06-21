package com.example.analyzer

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object AxmlParser {

    /**
     * Parses a compiled AndroidManifest.xml input stream and extracts all strings
     * that align with permission declarations, package names, or service definitions.
     */
    fun parseManifest(inputStream: InputStream): ManifestResult {
        val bytes = inputStream.readBytes()
        if (bytes.size < 8) {
            return ManifestResult("unknown", emptyList())
        }

        val strings = mutableListOf<String>()
        try {
            val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
            val magic = buffer.int // 0x00080003 (AXML template magic)
            if (magic == 0x00080003) {
                buffer.int // fileSize

                // Seek StringPool Chunk
                while (buffer.hasRemaining()) {
                    val chunkType = buffer.int
                    val chunkSize = buffer.int

                    if (chunkType == 0x001C0001) { // String Pool Type
                        val stringCount = buffer.int
                        val styleCount = buffer.int
                        val flags = buffer.int
                        val stringStart = buffer.int
                        val styleStart = buffer.int

                        val offsets = IntArray(stringCount)
                        for (i in 0 until stringCount) {
                            offsets[i] = buffer.int
                        }

                        // Style offsets if any
                        if (styleCount > 0) {
                            buffer.position(buffer.position() + styleCount * 4)
                        }

                        val stringPoolOffset = buffer.position() - (28 + stringCount * 4 + styleCount * 4)
                        val absoluteStringStart = stringPoolOffset + stringStart

                        val isUtf8 = (flags and 0x100) != 0

                        for (i in 0 until stringCount) {
                            val targetPos = absoluteStringStart + offsets[i]
                            if (targetPos < bytes.size) {
                                buffer.position(targetPos)
                                val decoded = if (isUtf8) {
                                    readUtf8String(buffer, bytes)
                                } else {
                                    readUtf16String(buffer, bytes)
                                }
                                if (decoded.isNotBlank()) {
                                    strings.add(decoded.trim())
                                }
                            }
                        }
                        break // We got the string chunk, that holds all permissions/identifiers
                    } else {
                        // Skip other chunks
                        val skipAmount = chunkSize - 8
                        if (skipAmount > 0 && buffer.position() + skipAmount <= bytes.size) {
                            buffer.position(buffer.position() + skipAmount)
                        } else {
                            break
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Fallback to signature sequence scanning on exception
            e.printStackTrace()
        }

        // Defensive Fallback: If String Pool chunk parsing was empty, do pattern matching directly
        // on the binary stream bytes to recover any readable strings.
        if (strings.isEmpty()) {
            strings.addAll(extractStringsBySignature(bytes))
        }

        val permissions = strings.filter { 
            it.startsWith("android.permission.") || it.contains(".permission.")
        }.distinct()

        var packageName = "com.unknown.target"
        
        // Find packages and custom services
        val packageCandidates = strings.filter { 
            it.contains(".") && 
            !it.startsWith("android.") && 
            !it.contains("permission") && 
            it.length in 8..64
        }
        if (packageCandidates.isNotEmpty()) {
            packageName = packageCandidates.first()
        }

        return ManifestResult(packageName, permissions)
    }

    private fun readUtf8String(buffer: ByteBuffer, bytes: ByteArray): String {
        val lenOffset = buffer.position()
        if (lenOffset >= bytes.size) return ""
        // Length of string is encoded in 1 or 2 bytes
        var len1 = buffer.get().toInt() and 0xFF
        if ((len1 and 0x80) != 0) {
            len1 = ((len1 and 0x7F) shl 8) or (buffer.get().toInt() and 0xFF)
        }
        // Bytes length
        var bytesLen = buffer.get().toInt() and 0xFF
        if ((bytesLen and 0x80) != 0) {
            bytesLen = ((bytesLen and 0x7F) shl 8) or (buffer.get().toInt() and 0xFF)
        }
        val pos = buffer.position()
        if (pos + bytesLen <= bytes.size) {
            val strBytes = ByteArray(bytesLen)
            buffer.get(strBytes)
            return String(strBytes, Charsets.UTF_8)
        }
        return ""
    }

    private fun readUtf16String(buffer: ByteBuffer, bytes: ByteArray): String {
        val pos = buffer.position()
        if (pos + 2 > bytes.size) return ""
        var wordLen = buffer.short.toInt() and 0xFFFF
        if ((wordLen and 0x8000) != 0) {
            wordLen = ((wordLen and 0x7FFF) shl 16) or (buffer.short.toInt() and 0xFFFF)
        }
        val bytesLen = wordLen * 2
        val currentPos = buffer.position()
        if (currentPos + bytesLen <= bytes.size) {
            val strBytes = ByteArray(bytesLen)
            buffer.get(strBytes)
            return String(strBytes, Charsets.UTF_16LE)
        }
        return ""
    }

    /**
     * Dual-Method Fallback Sequence Scanner:
     * Scans arbitrary binary byte dumps to reconstruct readable ASCII/UTF-16 alphanumeric sequences.
     */
    private fun extractStringsBySignature(bytes: ByteArray): List<String> {
        val list = mutableListOf<String>()
        val currentString = StringBuilder()
        var i = 0
        while (i < bytes.size) {
            val b = bytes[i].toInt()
            if (b in 32..126) {
                currentString.append(b.toChar())
            } else {
                if (currentString.length >= 6) {
                    list.add(currentString.toString())
                }
                currentString.setLength(0)
            }
            i++
        }
        if (currentString.length >= 6) {
            list.add(currentString.toString())
        }
        return list
    }

    data class ManifestResult(
        val packageName: String,
        val permissions: List<String>
    )
}
