package codeinfer.com.opencvandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private JavaCameraView javaCameraView;
    private Mat matRGBA;
    private BaseLoaderCallback _callbackBaseLoader = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    if(javaCameraView != null)
                    {
                        javaCameraView.enableView();
                    }
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }

        }
    };

    /*static{
        if(OpenCVLoader.initDebug())
        {
            Log.i(MainActivity.class.getName(),"OpenCV is loaded");
        }
        else{
            Log.i(MainActivity.class.getName(),"OpenCV failed to load!");
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        javaCameraView = (JavaCameraView) findViewById(R.id.java_cam_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug())
        {
            Log.i(MainActivity.class.getName(),"OpenCV is loaded");
            _callbackBaseLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            Log.i(MainActivity.class.getName(),"OpenCV failed to load!");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0,this,_callbackBaseLoader);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(javaCameraView != null)
        {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(javaCameraView != null)
        {
            javaCameraView.disableView();
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        matRGBA = new Mat(height,width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        matRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        matRGBA  = inputFrame.rgba();
        return matRGBA;
    }
}
