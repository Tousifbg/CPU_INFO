package www.goldpay.exchange.cpu_info.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.params.*;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.hardware.camera2.params.StreamConfigurationMap;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.SizeF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import www.goldpay.exchange.cpu_info.R;

import static android.app.Activity.RESULT_OK;

public class Camera_Fragment extends Fragment {

    private TextView cam_pixel,sensor_size,focus_txt,portrait,landscape,night_mode,
            theatre,steady_photo,sports,candlelight,hdr;

    private TextView txt1, txt2,txt3,txt4;

    int camNum;

    SizeF size;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private String[] cameraPermissions;

    private CameraManager cameraManager;

    CameraCharacteristics cameraCharacteristics;

    CameraMetadata cameraMetadata;

    String output = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);


        cam_pixel = view.findViewById(R.id.cam_pixel);
        sensor_size = view.findViewById(R.id.sensor_size);
        portrait = view.findViewById(R.id.portrait);
        landscape = view.findViewById(R.id.landscape);
        night_mode = view.findViewById(R.id.night_mode);
        theatre = view.findViewById(R.id.theatre);
        sports = view.findViewById(R.id.sports);
        candlelight = view.findViewById(R.id.candlelight);
        hdr = view.findViewById(R.id.hdr);


        mPermissionResult.launch(Manifest.permission.CAMERA);
        cameraPermissions = new String[]{Manifest.permission.CAMERA};

        cameraManager = (CameraManager)
                getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            //cameraId = cameraManager.getCameraIdList()[0];
            for (String cameraId : cameraManager.getCameraIdList())
            {
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                cameraMetadata = cameraManager.getCameraCharacteristics(cameraId);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        if (checkCameraPermission())
        {
            try {
                getCamerasMegaPixel();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            getBackCameraResolutionInMp();
            getCameraResolution(camNum);

            //cam_pixel.setText(String.valueOf(maxResolution + " Megapixels"));
            cam_pixel.setText(output);
            sensor_size.setText(String.valueOf(size));
        }else {
            requestCameraPermission();
        }

        return view;
    }

    public String getCamerasMegaPixel() throws CameraAccessException {
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        String[] cameraIds = manager.getCameraIdList();
        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[0]);
        output = "back camera mega pixel: " +  calculateMegaPixel(characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getWidth(),
                characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getHeight()) + "\n";

        characteristics = manager.getCameraCharacteristics(cameraIds[1]);
        output +=  "front camera mega pixel: " + calculateMegaPixel(characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getWidth(),characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).getHeight()) + "\n";
        return output;
    }

    public int calculateMegaPixel(int width, int height) {
        return  Math.round((width * height) / 1024000);

    }

    public void getBackCameraResolutionInMp()
    {

        if (!cameraCharacteristics.getKeys().contains(CameraCharacteristics.CONTROL_SCENE_MODE_PORTRAIT)) {
                    // Autofocus mode is supported
                    portrait.setText("Supported");
                }else {
                    portrait.setText("Not Supported");
                }
                if (!cameraCharacteristics.getKeys().contains(CameraCharacteristics.CONTROL_SCENE_MODE_NIGHT)) {
                    // Autofocus mode is supported
                    night_mode.setText("Supported");
                }else {
                    night_mode.setText("Not Supported");
                }
                if (!cameraCharacteristics.getKeys().contains(CameraCharacteristics.CONTROL_SCENE_MODE_THEATRE)) {
                    // Autofocus mode is supported
                    theatre.setText("Supported");
                }else {
                    theatre.setText("Not Supported");
                }
                if (!cameraCharacteristics.getKeys().contains(CameraCharacteristics.CONTROL_SCENE_MODE_SPORTS)) {
                    // Autofocus mode is supported
                    sports.setText("Supported");
                }else {
                    sports.setText("Not Supported");
                }
                if (!cameraCharacteristics.getKeys().contains(CameraCharacteristics.CONTROL_SCENE_MODE_CANDLELIGHT)) {
                    // Autofocus mode is supported
                    candlelight.setText("Supported");
                }else {
                    candlelight.setText("Not Supported");
                }
                if (!cameraCharacteristics.getKeys().contains(CameraCharacteristics.CONTROL_SCENE_MODE_LANDSCAPE)) {
                    // Autofocus mode is supported
                    landscape.setText("Supported");
                }else {
                    landscape.setText("Not Supported");
                }
                if (!cameraCharacteristics.getKeys().contains(CameraCharacteristics.CONTROL_SCENE_MODE_HDR)) {
                    // Autofocus mode is supported
                    hdr.setText("Supported");
                }else {
                    hdr.setText("Not Supported");
                }

    }

    private SizeF getCameraResolution(int camNum)
    {
        size = new SizeF(0,0);
        CameraManager manager = (CameraManager) getContext()
                .getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIds = manager.getCameraIdList();
            if (cameraIds.length > camNum) {
                CameraCharacteristics character = manager.getCameraCharacteristics(cameraIds[camNum]);
                size = character.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            }
        }
        catch (CameraAccessException e)
        {
            Log.e("YourLogString", e.getMessage(), e);
        }
        return size;
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        return  result;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(getActivity(),cameraPermissions,CAMERA_REQUEST_CODE);
    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        try {
                            getCamerasMegaPixel();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                        getBackCameraResolutionInMp();
                        getCameraResolution(camNum);

                        cam_pixel.setText(output);
                        sensor_size.setText(String.valueOf(size));
                    }
                    else {
                        //PERMISSION DENIED
                        Toast.makeText(getContext(), "Camera permission is necessary",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

}