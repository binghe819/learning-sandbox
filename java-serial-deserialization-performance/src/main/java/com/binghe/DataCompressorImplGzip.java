package com.binghe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataCompressorImplGzip implements DataCompressor {

    public byte[] compress(byte[] bytes) {
        assert (bytes != null);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gz = null;
        try {
            gz = new GZIPOutputStream(bos);
            gz.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("IO exception compressing data", e);
        } finally {
            if (gz != null) {
                try {
                    gz.close();
                } catch (IOException e) {
                }
            }
            try {
                bos.close();
            } catch (IOException e) {
            }
        }
        return bos.toByteArray();
    }

    public byte[] decompress(byte[] bytes) {
        ByteArrayOutputStream bos;
        byte[] result = null;
        if (bytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            bos = new ByteArrayOutputStream();
            GZIPInputStream gis = null;
            try {
                gis = new GZIPInputStream(bis);

                byte[] buf = new byte[8192];
                int r;
                while ((r = gis.read(buf)) > 0) {
                    bos.write(buf, 0, r);
                }
                result = bos.toByteArray();
            } catch (IOException ie) {
                throw new RuntimeException("IOException decompressing data", ie);
            } finally {
                if (gis != null) {
                    try {
                        gis.close();
                    } catch (IOException ignore) {
                    }
                }
                try {
                    bis.close();
                } catch (IOException ignore) {
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ignore) {
                    }
                }
            }
        }
        return result;
    }
}
