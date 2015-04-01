package com.danikula.videocache;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static com.danikula.videocache.Preconditions.checkArgument;
import static com.danikula.videocache.Preconditions.checkNotNull;

/**
 * Just simple utils.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
class ProxyCacheUtils {

    static final String LOG_TAG = "ProxyCache";
    static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
    static final int MAX_ARRAY_PREVIEW = 16;

    static String getSupposablyMime(String url) {
        MimeTypeMap mimes = MimeTypeMap.getSingleton();
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return TextUtils.isEmpty(extension) ? null : mimes.getMimeTypeFromExtension(extension);
    }

    static void assertBuffer(byte[] buffer, long offset, int length) {
        checkNotNull(buffer, "Buffer must be not null!");
        checkArgument(offset >= 0, "Data offset must be positive!");
        checkArgument(length >= 0 && length <= buffer.length, "Length must be in range [0..buffer.length]");
    }

    static String preview(byte[] data, int length) {
        int previewLength = Math.min(MAX_ARRAY_PREVIEW, Math.max(length, 0));
        byte[] dataRange = Arrays.copyOfRange(data, 0, previewLength);
        String preview = Arrays.toString(dataRange);
        if (previewLength < length) {
            preview = preview.substring(0, preview.length() - 1) + ", ...]";
        }
        return preview;
    }

    static void createDirectory(File directory) throws IOException {
        checkNotNull(directory, "File must be not null!");
        if (directory.exists()) {
            checkArgument(directory.isDirectory(), "File is not directory!");
        } else {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                String error = String.format("Directory %s can't be created", directory.getAbsolutePath());
                throw new IOException(error);
            }
        }
    }


}
