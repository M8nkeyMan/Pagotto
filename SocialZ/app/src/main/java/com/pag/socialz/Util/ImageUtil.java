package com.pag.socialz.Util;

import com.pag.socialz.Enums.UploadImagePrefix;

import java.util.Date;

public class ImageUtil {

    public static String generateImageTitle(UploadImagePrefix prefix, String parentId) {
        if (parentId != null) {
            return prefix.toString() + parentId;
        }

        return prefix.toString() + new Date().getTime();
    }
}
