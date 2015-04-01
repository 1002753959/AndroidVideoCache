package com.danikula.videocache.support;

import com.danikula.android.garden.io.IoUtils;
import com.danikula.videocache.HttpProxyCache;
import com.google.common.io.Files;

import org.robolectric.Robolectric;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

/**
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class ProxyCacheTestUtils {

    public static final String HTTP_DATA_URL = "https://dl.dropboxusercontent.com/u/15506779/persistent/proxycache/android.jpg";
    public static final String HTTP_DATA_BIG_URL = "https://dl.dropboxusercontent.com/u/15506779/persistent/proxycache/phones.jpg";
    public static final String ASSETS_DATA_NAME = "android.jpg";
    public static final String ASSETS_DATA_BIG_NAME = "phones.jpg";
    public static final int HTTP_DATA_SIZE = 4768;
    public static final int HTTP_DATA_BIG_SIZE = 94363;

    public static byte[] getFileContent(File file) throws IOException {
        return Files.asByteSource(file).read();
    }

    public static Response readProxyResponse(HttpProxyCache proxy) throws IOException {
        return readProxyResponse(proxy, -1);
    }

    public static Response readProxyResponse(HttpProxyCache proxy, int offset) throws IOException {
        URL url = new URL(proxy.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            if (offset >= 0) {
                connection.setRequestProperty("Range", "bytes=" + offset + "-");
            }
            return new Response(connection);
        } finally {
            connection.disconnect();
        }
    }

    public static byte[] loadAssetFile(String name) throws IOException {
        InputStream in = Robolectric.application.getResources().getAssets().open(name);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IoUtils.copy(in, out);
        IoUtils.closeSilently(in);
        IoUtils.closeSilently(out);
        return out.toByteArray();
    }

    public static File getTempFile(File file) {
        return new File(file.getParentFile(), file.getName() + ".download");
    }

    public static File newCacheFile() {
        return new File(Robolectric.application.getCacheDir(), UUID.randomUUID().toString());
    }

    public static byte[] generate(int capacity) {
        Random random = new Random(System.currentTimeMillis());
        byte[] result = new byte[capacity];
        random.nextBytes(result);
        return result;
    }
}
