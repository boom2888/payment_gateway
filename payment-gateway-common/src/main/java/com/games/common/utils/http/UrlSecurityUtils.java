package com.games.common.utils.http;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

/**
 * Validates outbound HTTP URLs before a connection is opened.
 */
public final class UrlSecurityUtils
{
    private UrlSecurityUtils()
    {
    }

    public static URL requirePublicHttpUrl(String value)
    {
        try
        {
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if (host == null || !("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)))
            {
                throw new IllegalArgumentException("Only public HTTP(S) URLs are allowed");
            }
            if (uri.getUserInfo() != null)
            {
                throw new IllegalArgumentException("URLs containing credentials are not allowed");
            }

            for (InetAddress address : InetAddress.getAllByName(host))
            {
                if (isNonPublic(address))
                {
                    throw new IllegalArgumentException("Private or reserved network addresses are not allowed");
                }
            }
            return uri.toURL();
        }
        catch (IllegalArgumentException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Invalid outbound URL", e);
        }
    }

    private static boolean isNonPublic(InetAddress address)
    {
        if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress()
                || address.isSiteLocalAddress() || address.isMulticastAddress())
        {
            return true;
        }

        byte[] bytes = address.getAddress();
        if (bytes.length == 4)
        {
            int first = bytes[0] & 0xff;
            int second = bytes[1] & 0xff;
            return first == 0 || first >= 224
                    || (first == 100 && second >= 64 && second <= 127)
                    || (first == 198 && (second == 18 || second == 19));
        }

        return (bytes[0] & 0xfe) == 0xfc;
    }
}
