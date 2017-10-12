package it.hansir.zxing_qr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.R.attr.width;

public class OpenQrActivity extends AppCompatActivity implements View.OnClickListener {
    private Button createQR;
    private ImageView QR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_qr);
        createQR = (Button) findViewById(R.id.bt_open_qr);
        QR = (ImageView) findViewById(R.id.iv_qr);
        createQR.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open_qr:
                Bitmap bitmapQR = creatQRBitmap("www.baidu.com", 400, 400);
                Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.hello);
                Bitmap bitmapCom = addLogo(bitmapQR, bitmapLogo);
                QR.setImageBitmap(bitmapCom);
                break;
        }
    }

    /**
     * 给二维码添加图标
     *
     * @param bitQR   二维码
     * @param bitLogo 添加的图标
     */
    private Bitmap addLogo(Bitmap bitQR, Bitmap bitLogo) {
        int qrWidth = bitQR.getWidth();
        int qrHeight = bitQR.getHeight();
        int logWidth = bitLogo.getWidth();
        int logoHeight = bitLogo.getHeight();
//        绘图区域
        Bitmap combinationBitmap = Bitmap.createBitmap(qrWidth, qrHeight, Bitmap.Config.ARGB_8888);
        //画布
        Canvas canvas = new Canvas(combinationBitmap);
        canvas.drawBitmap(bitQR, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float scaleSize = 1.0f;
        while ((logWidth / scaleSize) > (qrWidth / 5) || (logoHeight / scaleSize) > (qrHeight / 5)) {
            scaleSize *= 2;
        }

        float fx = 1.0f / scaleSize;
        canvas.scale(fx, fx, qrWidth / 2, qrHeight / 2);
        canvas.drawBitmap(bitLogo, (qrWidth - logWidth) / 2, (qrHeight - logoHeight) / 2, null);
        canvas.restore();
        return combinationBitmap;
    }

    /**
     * 创建二维码
     *
     * @param contant
     * @param width
     * @param height
     */
    private Bitmap creatQRBitmap(String contant, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix bit = qrCodeWriter.encode(contant, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * 400];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (bit.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
