package com.mdgiitr.karthik.bokeh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView imageDisplay;
    Button chooseImage;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageDisplay = findViewById(R.id.imageview);
        chooseImage = findViewById(R.id.button_choose_image);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100);
            }
        });

        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                if (bitmap != null) {
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();
                    float[][] redActual = new float[h][w];
                    float[][] greenActual = new float[h][w];
                    float[][] blueActual = new float[h][w];
                    imageDisplay.setImageBitmap(bitmap);
                    for (int i =0;i<bitmap.getHeight();i++){
                        for(int j=0;j<bitmap.getWidth();j++){
                            redActual[i][j] = (float)Color.red(bitmap.getPixel(j,i));
                            greenActual[i][j] = (float)Color.green(bitmap.getPixel(j,i));
                            blueActual[i][j] = (float)Color.blue(bitmap.getPixel(j,i));
                        }
                    }
                    Bitmap black = Bitmap.createBitmap(
                            w, // Width
                            h, // Height
                            Bitmap.Config.ARGB_8888 // Config
                    );
                    Canvas canvas = new Canvas(black);
                    canvas.drawColor(Color.BLACK);
                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);
                    paint.setAntiAlias(true);
                    canvas.drawCircle(
                            0, // cx
                            canvas.getHeight(), // cy
                            10, // Radius
                            paint // Paint
                    );
                    float[][] redDuplicate = new float[h][w];
                    float[][] greenDuplicate = new float[h][w];
                    float[][] blueDuplicate = new float[h][w];
//                    imageDisplay.setImageBitmap(black);
                    for (int i =0;i<black.getHeight();i++){
                        for(int j=0;j<black.getWidth();j++){
                            redDuplicate[i][j] = (float)Color.red(black.getPixel(j,i));
                            greenDuplicate[i][j] = (float)Color.green(black.getPixel(j,i));
                            blueDuplicate[i][j] = (float)Color.blue(black.getPixel(j,i));
                        }
                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
