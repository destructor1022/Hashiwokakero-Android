package com.example.hashiwokakero;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.wear.widget.BoxInsetLayout;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends WearableActivity {

    private int[] gridDimensions;

    private BoxInsetLayout rootLayout;

    private Map<Piece, ImageView> squareToImageView;
    private Map<String, Integer> structToColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = (BoxInsetLayout) findViewById(R.id.root);

        gridDimensions = new int[]{10, 10};
        squareToImageView = new HashMap<>();
        structToColor = new HashMap<>();
        structToColor.put("open", R.color.black);
        structToColor.put("closed", R.color.black);
        structToColor.put("node", R.color.dark_red);
        structToColor.put("bridge", R.color.green);


        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("play");

        PyObject obj = pyobj.callAttr("easy", "ellipse", gridDimensions[0], gridDimensions[1]);

        String pyGame = obj.toString();

        List<List<Piece>> javaGame = convertGame(pyGame);

        createLayout(this, javaGame);




        // Enables Always-on
        setAmbientEnabled();
    }

    public void createLayout(Context context, List<List<Piece>> javaGame) {
        LinearLayout parent = new LinearLayout(context);
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);
        for(List<Piece> horizontal : javaGame) {
            LinearLayout row = new LinearLayout(context);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            row.setOrientation(LinearLayout.VERTICAL);
            for(Piece square : horizontal) {
                ImageView iv = new ImageView(context);
                iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f));
                iv.setImageResource(R.drawable.action_item_icon_background);
                row.addView(iv);
                squareToImageView.put(square, iv);
                setPieceColor(context, square);
            }
            parent.addView(row);
        }
        rootLayout.addView(parent);
    }

    public void setPieceColor(Context context, Piece square) {
        ImageView iv = squareToImageView.get(square);
        int color = structToColor.get(square.struct);
        iv.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public List<List<Piece>> convertGame(String pyGame) {
        List<List<Piece>> board = new ArrayList<>();
        String[] rows = pyGame.split("\n");
        for(String row : rows) {
            String[] cols = row.split(";");
            List<Piece> column = new ArrayList<>();
            for(String col : cols) {
                String[] split = col.split(",");
                column.add(new Piece(split[0], Integer.parseInt(split[1])));
            }
            board.add(column);
        }

        return board;
    }
}