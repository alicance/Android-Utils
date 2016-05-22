package com.samego.alic.androidutils.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 *
 * Created by alic on 16-5-20.
 */

    public class FileUtil {

        static boolean isKitKat = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;     //如果版本大与4.4


        /**
         * Get a file path from a Uri. This will get the the path for Storage Access
         * <p/>
         * Framework Documents, as well as the _data field for the MediaStore and
         * <p/>
         * other file-based ContentProviders.
         *
         * @param context The context.
         * @param uri     The Uri to query.
         * @author paulburke
         */
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static String getPath(Context context, Uri uri) {
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String doc_id = DocumentsContract.getDocumentId(uri);
                    final String[] spilt = doc_id.split(":");
                    final String type = spilt[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + spilt[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocuments(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);

                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);

                    // MediaProvider
                } else if (isMediaDocuments(uri)) {
                    final String doc_id = DocumentsContract.getDocumentId(uri);

                    final String[] split = doc_id.split(":");

                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                    // MediaStore (and general)
                    return getDataColumn(context, uri, null, null);

                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    // File
                    return uri.getPath();

                }

            }
            return "";
        }


        /**
         * Get the value of the data column for this Uri. This is useful for
         * <p/>
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param contentUri    The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        private static String getDataColumn(Context context, Uri contentUri, String selection, String[] selectionArgs) {
            Cursor cursor = null;

            final String column = "_data";

            final String[] projection = {column};

            try {
                cursor = context.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private static boolean isMediaDocuments(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private static boolean isDownloadsDocuments(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        private static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }
    }


