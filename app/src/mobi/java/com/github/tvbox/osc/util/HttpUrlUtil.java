package com.github.tvbox.osc.util;

import okhttp3.HttpUrl;

public class HttpUrlUtil {
    public static HttpUrl parse(String url) {
        return HttpUrl.get(url);
    }
}