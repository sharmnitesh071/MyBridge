package com.databridge.mybridge.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.Nullable
import java.io.*


internal object UriHelper {

    fun getPath(context: Context,uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context,uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type,ignoreCase = true)) {
                    return (Environment.getExternalStorageDirectory().toString() + "/"
                            + split[1])
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUriPrefixesToTry = arrayOf("content://downloads/public_downloads","content://downloads/my_downloads","content://downloads/all_downloads")

                for (contentUriPrefix in contentUriPrefixesToTry) {
                    try {
                        val contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix),java.lang.Long.valueOf(id))
                        val path = getDataColumn(context,contentUri,null,null)
                        if (path != null) {
                            return path
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                val fileName = getFileName(context,uri)
                val cacheDir = getDocumentCacheDir(context)
                val file = generateFileName(fileName,cacheDir)
                var destinationPath: String? = null
                if (file != null) {
                    destinationPath = file!!.getAbsolutePath()
                    saveFileFromUri(context,uri,destinationPath)
                }

                return destinationPath
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context,contentUri,selection,
                        selectionArgs)
            }// MediaProvider
            // DownloadsProvider
            else if ("content".equals(uri.scheme,ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri))
                    uri.lastPathSegment
                else
                    getDataColumn(context,uri,null,null)

            } else if ("file".equals(uri.scheme,ignoreCase = true)) {
                return uri.path
            }
            return uri.path
        } else if ("content".equals(uri.scheme,ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context,uri,null,null)

        } else if ("file".equals(uri.scheme,ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     * The context.
     * @param uri
     * The Uri to query.
     * @param selection
     * (Optional) Filter used in the query.
     * @param selectionArgs
     * (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private fun getDataColumn(context: Context,uri: Uri?,
                              selection: String?,selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!,projection,
                    selection,selectionArgs,null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        }catch (e:Exception){
            e.printStackTrace()
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
                .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
                .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
                .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
                .authority
    }

    val DOCUMENTS_DIR = "documents"
    fun getDocumentCacheDir(context: Context): File {
        val dir = File(context.cacheDir,DOCUMENTS_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }


        return dir
    }

    private fun saveFileFromUri(context: Context,uri: Uri,destinationPath: String) {
        var `is`: InputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            `is` = context.contentResolver.openInputStream(uri)
            bos = BufferedOutputStream(FileOutputStream(destinationPath,false))
            val buf = ByteArray(1024)
            `is`!!.read(buf)
            do {
                bos!!.write(buf)
            } while (`is`!!.read(buf) !== -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (`is` != null) `is`!!.close()
                if (bos != null) bos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun generateFileName(name: String?, directory: File): File? {
        var name: String? = name ?: return null

        var file = File(directory,name)

        if (file.exists()) {
            var fileName: String = name!!
            var extension = ""
            val dotIndex = name!!.lastIndexOf('.')
            if (dotIndex > 0) {
                fileName = name.substring(0,dotIndex)
                extension = name.substring(dotIndex)
            }

            var index = 0

            while (file.exists()) {
                index++
                name = "$fileName($index)$extension"
                file = File(directory,name)
            }
        }

        try {
            if (!file.createNewFile()) {
                return null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }


        return file
    }


    fun getFileName(context: Context,uri: Uri): String? {
        val mimeType = context.contentResolver.getType(uri)
        var filename: String? = null

        if (mimeType == null && context != null) {
            val path = getPath(context,uri)
            if (path == null) {
                filename = getName(uri.toString())
            } else {
                val file = File(path)
                filename = file.name
            }
        } else {
            val returnCursor = context.contentResolver.query(uri,null,null,null,null)
            if (returnCursor != null) {
                val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                filename = returnCursor.getString(nameIndex)
                returnCursor.close()
            }
        }

        return filename
    }

    fun getName(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val index = filename.lastIndexOf('/')
        return filename.substring(index + 1)
    }

}