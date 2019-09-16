package net.minecraft.util;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtil
{
    public static final ListeningExecutorService DOWNLOADER_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("Downloader %d").build()));

    /** The number of download threads that we have started so far. */
    private static final AtomicInteger DOWNLOAD_THREADS_STARTED = new AtomicInteger(0);
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Builds an encoded HTTP POST content string from a string map
     */
    public static String buildPostString(Map<String, Object> data)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (Entry<String, Object> entry : data.entrySet())
        {
            if (stringbuilder.length() > 0)
            {
                stringbuilder.append('&');
            }

            try
            {
                stringbuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedencodingexception1)
            {
                unsupportedencodingexception1.printStackTrace();
            }

            if (entry.getValue() != null)
            {
                stringbuilder.append('=');

                try
                {
                    stringbuilder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                }
                catch (UnsupportedEncodingException unsupportedencodingexception)
                {
                    unsupportedencodingexception.printStackTrace();
                }
            }
        }

        return stringbuilder.toString();
    }

    /**
     * Sends a POST to the given URL using the map as the POST args
     */
    public static String postMap(URL url, Map<String, Object> data, boolean skipLoggingErrors, @Nullable Proxy proxyIn)
    {
        return post(url, buildPostString(data), skipLoggingErrors, proxyIn);
    }

    /**
     * Sends a POST to the given URL
     */
    private static String post(URL url, String content, boolean skipLoggingErrors, @Nullable Proxy p_151225_3_)
    {
        try
        {
            if (p_151225_3_ == null)
            {
                p_151225_3_ = Proxy.NO_PROXY;
            }

            HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection(p_151225_3_);
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpurlconnection.setRequestProperty("Content-Length", "" + content.getBytes().length);
            httpurlconnection.setRequestProperty("Content-Language", "en-US");
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
            dataoutputstream.writeBytes(content);
            dataoutputstream.flush();
            dataoutputstream.close();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            StringBuffer stringbuffer = new StringBuffer();
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                stringbuffer.append(s);
                stringbuffer.append('\r');
            }

            bufferedreader.close();
            return stringbuffer.toString();
        }
        catch (Exception exception)
        {
            if (!skipLoggingErrors)
            {
                LOGGER.error("Could not post to {}", url, exception);
            }

            return "";
        }
    }
}
