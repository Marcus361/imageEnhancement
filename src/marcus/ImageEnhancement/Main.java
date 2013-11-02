package marcus.ImageEnhancement;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Main {

	@SuppressWarnings("static-access")
	public static void main (String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat perfectImage = Highgui.imread("C:/Users/Marcus Hayward/Dropbox/eclipseWorkspace/ImageEnhancement/src/marcus/ImageEnhancement/dogOriginal.bmp", Highgui.IMREAD_GRAYSCALE);
		DisplayImage displayImage = new DisplayImage();
		EnhanceImage enhancement = new EnhanceImage();
		
		
		
		//calculate error from perfect image to distorted image
		MeanSquareError error = new MeanSquareError();
//		System.out.println("Initial image distortion: " + error.calculateMeanSquareError(enhancement.getEnhancedImage()));
//
//
//		enhancement.neighbourHoodEnhance();
//
//		System.out.println("Distortion after neighbour hood enhance: "+error.calculateMeanSquareError(enhancement.getEnhancedImage()));
//		enhancement.setEnhancedImage(enhancement.runPowerLawTransform(0.1, 1.746));
//		

		FastFourierTransform FFT = new FastFourierTransform(enhancement.getEnhancedImage());
		Mat fftMat = new Mat();
		fftMat = FFT.calculateDFT();
  		displayImage.image(fftMat);

		//System.out.println("Distortion after power law transform: "+error.calculateMeanSquareError(fftMat));
  		
  		
  		FastFourierTransform FFasdfT = new FastFourierTransform(perfectImage);
		
  		displayImage.image(FFasdfT.calculateDFT());
	}
	
}
