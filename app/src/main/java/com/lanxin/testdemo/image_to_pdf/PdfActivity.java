package com.lanxin.testdemo.image_to_pdf;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.lanxin.testdemo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author:张小兵
 * @e-mail:460116602@qq.com
 * @date:2024/02/26
 */
public class PdfActivity extends AppCompatActivity {

    private LinearLayout llContentView;
    private TextView tvImage, tvPdf, tvLocalFile;
    private List<String> imagePathList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        tvImage = findViewById(R.id.tvImage);
        tvPdf = findViewById(R.id.tvPdf);
        tvLocalFile = findViewById(R.id.tvLocalFile);
        llContentView = findViewById(R.id.llContentView);
        tvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mimeType = "image/*";
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType(mimeType);
                startActivityForResult(intent, 111);
            }
        });

        tvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1 准备工作
                String pdf_baseUrl = getExternalFilesDir(null).getAbsolutePath();
                String pdf_Path = pdf_baseUrl + File.pathSeparator.toString() + "test.pdf";
                List<File> pdf_imageList = new ArrayList<>();
                for (String s : imagePathList) {
                    pdf_imageList.add(new File(s));
                }

                //低版本
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        FileOutputStream fos;
//                        try {
//                            fos = new FileOutputStream(pdf_Path);
//                            Document document = new Document();
//                            PdfWriter.getInstance(document, fos);
//
//                            document.add()
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }).start();


                //itext库实现
                //itext库实现
                //itext库实现
                //itext库实现
                try {
                    com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(pdf_Path);
                    com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
                    com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf, PageSize.A3);

                    for (File file : pdf_imageList) {
                        byte[] bytes = streamToByteArray(new FileInputStream(file));
                        if (bytes != null) {
                            com.itextpdf.io.image.ImageData imageData = com.itextpdf.io.image.ImageDataFactory.create(bytes);
//                        PageSize pageSize = new PageSize(imageData.getWidth(), imageData.getHeight());
//                        pdf.setDefaultPageSize(pageSize);

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
//                        image.setHeight(imageHeight);
//                        image.setWidth(imageWidth);
//                        image.setAutoScaleHeight(true);
                            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
                            document.setMargins(10, 10, 10, 10);
                            document.add(image);
                        }
                    }

                    document.close();
                } catch (FileNotFoundException | MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //原生实现逻辑
                //原生实现逻辑
                //原生实现逻辑
                //2
//                long time = System.currentTimeMillis();
//                int pdfCount = pdf_imageList.size();
//                PdfDocument pdfDocument = new PdfDocument();
//                for (int i = 0; i < pdfCount; i++) {
//                    String file = pdf_imageList.get(i).getAbsolutePath();
//                    Bitmap bitmap = BitmapFactory.decodeFile(file);
//                    bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
//
//                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), pdfCount).create();
//                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//                    Canvas page_canvas = page.getCanvas();
//                    page_canvas.drawBitmap(bitmap, 0, 0, null);
//                    pdfDocument.finishPage(page);
//                    bitmap.recycle();
//                }
//
//                try {
//                    pdfDocument.writeTo(new FileOutputStream(new File(pdf_Path)));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    pdfDocument.close();
//                }
            }
        });


        tvLocalFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 222);
            }
        });
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

    @Override9
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String path = getRealPathFromUri(PdfActivity.this, uri);
            imagePathList.add(path);
            String pathList = "";
            for (String s : imagePathList) {
                pathList = pathList + s + "\n";
            }
            tvImage.setText(pathList);
        }
    }

    private String getRealPathFromUri(Context context, Uri uri) {
        Cursor cursor = null;
        String path = null;
        try {
            // 只查询媒体数据的数据列
            String[] projection = {MediaStore.MediaColumns.DATA};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // 获取数据列的索引，一般只有一个数据列，即索引为0
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                // 获取数据列的数据，即文件的真实路径
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            // 处理异常情况
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

//    private void onSavePdfFile(String bitmapPath) {
//        String baseName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String pdfPath = new File(getFilesDir(), "PDF_" + baseName + ".pdf").getAbsolutePath();
//
//        ArrayList<String> maps = new ArrayList<>(1);
//        maps.add(bitmapPath);
//
//        mergeImagesToPdf(maps, pdfPath);
//    }

//    /**
//     * 将给定集合中的图片合并到一个pdf文档里面
//     *
//     * @param imagePathList 图片路径集合
//     * @param destPath      合并之后的PDF文档
//     */
//    public static boolean mergeImagesToPdf(List<String> imagePathList, String destPath) {
//        try {
//            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(destPath));
//            Document document = new Document(pdfDocument);
//            if (imagePathList != null && imagePathList.size() > 0) {
//                int size = imagePathList.size();
//                for (int i = 0; i < size; i++) {
//                    String imgPath = imagePathList.get(i);
//                    ImageData imageData;
//                    if (imgPath.startsWith("http://") || imgPath.startsWith("https://")) {
//                        imageData = ImageDataFactory.create(new URL(imgPath));
//                    } else {
//                        imageData = ImageDataFactory.create(imgPath);
//                    }
//                    Image image = new Image(imageData);
//                /*
//                    设置旋转的弧度值，默认是逆时针旋转的。
//                    弧度、角度换算公式：
//                    1° = PI / 180°
//                    1 rad = 180° / PI
//                */
//                    image.setRotationAngle(-Math.PI / 2); // 顺时针旋转90°
//                    // 设置图片自动缩放，即：图片宽高自适应
//                    image.setAutoScale(true);
//                    document.add(image);
//                    if (i != size - 1) {
//                        // 最后一页不需要新增空白页
//                        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
//                    }
//                }
//            }
//            pdfDocument.close();
//            return true;
//        } catch (Exception e) {
//        }
//        return false;
//    }
//
//    private void getApps() {
//        List<ResolveInfo> infos = getInstalledApplication(PdfActivity.this, false);
//        List<String> packages = new ArrayList<>();
//        for (ResolveInfo info : infos) {
//            packages.add(info.activityInfo.name);
//        }
//
//        if (!packages.isEmpty()) {
//            getApp.setText(packages.size() + "");
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PdfActivity.this, R.layout.item_quanxinapp, packages);
//            listView.setAdapter(adapter);
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // pick image after request permission success
//        if (requestCode == 110 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            getApps();
//        }
//    }
//
//    public static List<ResolveInfo> getInstalledApplication(Context context, boolean needSysAPP) {
//        PackageManager packageManager = context.getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
//        if (!needSysAPP) {
//            List<ResolveInfo> resolveInfosWithoutSystem = new ArrayList<>();
//            for (int i = 0; i < resolveInfos.size(); i++) {
//                ResolveInfo resolveInfo = resolveInfos.get(i);
//                try {
//                    if (!isSysApp(context, resolveInfo.activityInfo.packageName)) {
//                        resolveInfosWithoutSystem.add(resolveInfo);
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//            return resolveInfosWithoutSystem;
//        }
//        return resolveInfos;
//    }
//
//    //判断是否系统应用
//    public static boolean isSysApp(Context context, String packageName) throws PackageManager.NameNotFoundException {
//        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
//        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
//    }
}
