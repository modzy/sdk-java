package com.modzy.sdk.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {

    public static byte[] readFile( String path ) throws IOException {
        return IOUtils.toByteArray( new FileInputStream( new File(path) ) );
    }

}
