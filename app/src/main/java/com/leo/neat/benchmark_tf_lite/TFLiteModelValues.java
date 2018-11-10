package com.leo.neat.benchmark_tf_lite;

public class TFLiteModelValues {

    // App data
    public static final String CURRENT_MODEL = "current model";

    // Standard Model
    public static final String STANDARD_MODEL_FILENAME = "detect.tflite";
    public static final String STANDARD_MODEL_LABELS = "file:///android_asset/coco_labels_list.txt";
    public static final int STANDARD_MODEL_INPUT_SIZE = 300;
    public static final boolean STANDARD_MODEL_IS_QUANTITIZED = true;
    public static final float STANDARD_MODEL_CONFIDENCE = 0.6f;

    // Ando Model
    public static final String ANDO_MODEL_FILENAME = "ando_optimized_graph.tflite";
    public static final String ANDO_MODEL_LABELS = "file:///android_asset/ando_labels_list.txt";
    public static final int ANDO_MODEL_INPUT_SIZE = 300;
    public static final boolean ANDO_MODEL_IS_QUANTITIZED = false;
    public static final float ANDO_MODEL_CONFIDENCE = 0.85f;
}
