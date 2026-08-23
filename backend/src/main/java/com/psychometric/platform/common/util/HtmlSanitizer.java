package com.psychometric.platform.common.util;

import org.springframework.web.util.HtmlUtils;

public class HtmlSanitizer {

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        // Strip HTML tags and clean whitespace
        String stripped = input.replaceAll("<[^>]*>", "").trim();
        return HtmlUtils.htmlEscape(stripped);
    }
}
