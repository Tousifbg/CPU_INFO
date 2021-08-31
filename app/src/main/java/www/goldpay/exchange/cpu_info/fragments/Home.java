package www.goldpay.exchange.cpu_info.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;

import www.goldpay.exchange.cpu_info.R;

public class Home extends Fragment {

    private TextView Brand_text,Manufacturer_text,model_txt,board,resolution,serial_no,api,hardware,
            density,ram,storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Manufacturer_text = view.findViewById(R.id.Manufacturer_text);
        Brand_text = view.findViewById(R.id.Brand_text);
        model_txt = view.findViewById(R.id.model_txt);
        board = view.findViewById(R.id.board);
        resolution = view.findViewById(R.id.resolution);
        serial_no = view.findViewById(R.id.serial_no);
        api = view.findViewById(R.id.api);
        hardware = view.findViewById(R.id.hardware);
        density = view.findViewById(R.id.density);
        ram = view.findViewById(R.id.ram);
        storage = view.findViewById(R.id.storage);


        //get screen resolution
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //get screen density or dpi
        int densityDpi = getResources().getDisplayMetrics().densityDpi;
        switch (densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                // LDPI
                density.setText(">100 dpi");
                break;

            case DisplayMetrics.DENSITY_MEDIUM:
                // MDPI
                density.setText(">100 dpi");
                break;

            case DisplayMetrics.DENSITY_TV:
            case DisplayMetrics.DENSITY_HIGH:
                // HDPI
                density.setText("100 dpi");
                break;

            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_280:
                // XHDPI
                density.setText("280 dpi");
                break;

            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_360:
                density.setText("360 dpi");
                break;
            case DisplayMetrics.DENSITY_400:
                density.setText("400 dpi");
                break;
            case DisplayMetrics.DENSITY_420:
                // XXHDPI
                density.setText("420 dpi");
                break;

            case DisplayMetrics.DENSITY_XXXHIGH:
            case DisplayMetrics.DENSITY_560:
                // XXXHDPI
                density.setText("560 dpi");
                break;
        }

        //get ram
        ActivityManager actManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        assert actManager != null;
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        long availMemory = memInfo.availMem;
        long usedMemory = totalMemory - availMemory;
        float precentlong = (((float) (availMemory / totalMemory)) * 100);

        //get storage
        getInternalStorage();

        String Manufacturer_value = Build.MANUFACTURER;
        String Brand_value = Build.BRAND;
        String Model_value = Build.MODEL;
        String Board_value = Build.BOARD;
        String Hardware_value = Build.HARDWARE;
        String Serial_nO_value = Build.SERIAL;
        String ScreenResolution_value = height + " * " + width + " Pixels";
        String API_level = Build.VERSION.SDK_INT + "";

        Manufacturer_text.setText(Manufacturer_value);
        Brand_text.setText(Brand_value);
        model_txt.setText(Model_value);
        board.setText(Board_value);
        resolution.setText(ScreenResolution_value);
        serial_no.setText(Serial_nO_value);
        api.setText(API_level);
        hardware.setText(Hardware_value);

        ram.setText("".concat(bytesToHuman(totalMemory)));

        return view;
    }

    private String bytesToHuman(long size) {
        long Kb = 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " KB";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " MB";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " GB";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " TB";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " Pb";
        if (size >= Eb) return floatForm((double) size / Eb) + " Eb";

        return "0";
    }

    private String floatForm(double d) {
        return String.format(java.util.Locale.US, "%.2f", d);
    }

    private void getInternalStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long BlockSize = stat.getBlockSize();
        long TotalBlocks = stat.getBlockCount();
        storage.setText("".concat(formatSize(TotalBlocks * BlockSize)));
    }

    public static String formatSize(long size) {
        String suffixSize = null;

        if (size >= 1024) {
            suffixSize = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffixSize = " MB";
                size /= 1024;
            }
        }

        StringBuilder BufferSize = new StringBuilder(
                Long.toString(size));

        int commaOffset = BufferSize.length() - 3;
        while (commaOffset > 0) {
            BufferSize.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffixSize != null) BufferSize.append(suffixSize);
        return BufferSize.toString();
    }
}