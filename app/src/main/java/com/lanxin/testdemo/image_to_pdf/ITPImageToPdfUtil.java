package com.lanxin.testdemo.image_to_pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @author:张小兵
 * @e-mail:460116602@qq.com
 * @date:2024/07/23
 */
public class ITPImageToPdfUtil {

    private static final String TAG = ITPImageToPdfUtil.class.getSimpleName();
    private static volatile ITPImageToPdfUtil instance = null;

    public static ITPImageToPdfUtil getInstance() {
        if (instance == null) {
            synchronized (ITPImageToPdfUtil.class) {
                if (instance == null) {
                    instance = new ITPImageToPdfUtil();
                }
            }
        }
        return instance;
    }

    public boolean messageToTxt(String message, String txtPath) {
        File file = null;
        File tmpFile = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            file = new File(txtPath);
            tmpFile = new File(txtPath + ".tmp");
            //加密生成的文件源
            OutputStream outputStream = TedFileStreamUtils.newFileOutputStream(tmpFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            osw = new OutputStreamWriter(outputStream);
            bw = new BufferedWriter(osw);
            bw.write(message);
            bw.close();

            if (tmpFile.exists()) {
                return tmpFile.renameTo(file);
            }
        } catch (Exception e) {
            Logger.e(TAG, "io error[%s]", e);
            Logger.e(TAG, "parent error[%s]", e);
            if (file != null && file.exists()) {
                file.delete();
            }

            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
        } finally {
            IOUtil.close(osw);
            IOUtil.close(bw);
        }

        return false;
    }

    public boolean imagesToPdf(ArrayList<File> pdf_imageList, String pdfPath) {
        File file = null;
        File tmpFile = null;
        BufferedOutputStream os = null;
        try {
            Logger.i(TAG, "start imagesToPdf");
            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(pdfPath);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf, PageSize.A3);

            for (File imageFile : pdf_imageList) {
                InputStream inputStream = TedFileStreamUtils.newFileInputStream(imageFile);
                byte[] bytes = streamToByteArray(inputStream);
                if (bytes != null) {
                    com.itextpdf.io.image.ImageData imageData = com.itextpdf.io.image.ImageDataFactory.create(bytes);
//                    PageSize pageSize = new PageSize(imageData.getWidth(),imageData.getHeight());
//                    pdf.setDefaultPageSize(pageSize);

                    com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
                    PageSize pageSize = pdf.getDefaultPageSize();
                    float pageWidth = pageSize.getWidth();
                    float pageHeight = pageSize.getHeight();
                    float imageWidth = imageData.getWidth();
                    float imageHeight = imageData.getHeight();
                    if (pageWidth < imageWidth || pageHeight < imageHeight) {
                        //纸张都比图片小
                        //缩放图片
                        do {
                            //每次缩放0.8
                            float scale = 0.95f;
                            imageHeight = imageHeight * scale;
                            imageWidth = imageWidth * scale;
                        } while (pageWidth < imageWidth || pageHeight < imageHeight);
                    }

                    image.scaleToFit(imageWidth, imageHeight);
//                    image.setHeight(imageHeight);
//                    image.setWidth(imageWidth);
//                    image.setAutoScaleHeight(true);
                    image.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    document.setMargins(10, 10, 10, 10);
                    document.add(image);

                }
            }
            //文档图片写入完毕
            document.close();

            //重新覆盖，进行加密
            file = new File(pdfPath);
            tmpFile = new File(pdfPath + ".tmp");
            //加密生成的文件源
            OutputStream outputStream = TedFileStreamUtils.newFileOutputStream(tmpFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            int len;
            byte[] buffer = new byte[1024 * 8];
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            try {
                os = new BufferedOutputStream(outputStream);
                while ((len = is.read(buffer)) > 0) {
                    os.write(buffer, 0, len);
                }

                if (tmpFile.exists()) {
                    return tmpFile.renameTo(file);
                }
            } catch (Exception e) {
                Logger.e(TAG, "io error[%s]", e);
            } finally {
                IOUtil.close(is);
                IOUtil.close(os);
            }

            Logger.i(TAG, "encode finish imagesToPdf path[%s]", file.getAbsolutePath());
        } catch (Exception e) {
            Logger.e(TAG, "parent error[%s]", e);
            if (file != null && file.exists()) {
                file.delete();
            }

            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
        }

        return false;
    }


    public static byte[] streamToByteArray(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bos.close();
        }
        return null;
    }
}
