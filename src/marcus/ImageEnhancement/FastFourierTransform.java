package marcus.ImageEnhancement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

public class FastFourierTransform {
	
	private Mat image;
	private Mat magI;
	
	public FastFourierTransform(Mat mat)
	{
		this.image = mat;
	}
	
	public Mat calculateDFT()
	{
		Mat temp = this.image.clone();
		   
	    
		temp.convertTo(temp, CvType.CV_32FC1);
		
		Mat zeroMat = Mat.zeros(369, 355, CvType.CV_32FC1);
		
		List<Mat> planes = Arrays.asList(temp, zeroMat);
		
		Mat complexI = new Mat();
		
		Core.merge(planes, complexI);
		//Core.dft(complexI, complexI, Core.DFT_SCALE | Core.DFT_COMPLEX_OUTPUT, 0);
		Core.dft(complexI, complexI);
		
		ArrayList<Mat> dftPlanes = new ArrayList<Mat>();
		Core.split(complexI, dftPlanes);  

		
		Core.magnitude(dftPlanes.get(0), dftPlanes.get(1), dftPlanes.get(0));// planes[0] = magnitude
		Mat magI = dftPlanes.get(0);


		for(int m = 0; m < magI.rows(); m++){
			for(int n = 0; n < magI.cols(); n++){					
				magI.put(m, n, (magI.get(m, n)[0]+1));
			}
		}
		    
		
		Core.log(magI, magI);
		
		// crop the spectrum, if it has an odd number of rows or columns
		magI = magI.submat(new Rect(0, 0, magI.cols() & -2, magI.rows() & -2));

	    // rearrange the quadrants of Fourier image  so that the origin is at the image center
		int cx = magI.cols()/2;
		int cy = magI.rows()/2;
		
		Mat q0 = new Mat(magI, new Rect(0, 0, cx, cy));   // Top-Left - Create a ROI per quadrant
		Mat q1 = new Mat(magI, new Rect(cx, 0, cx, cy));  // Top-Right
		Mat q2 = new Mat(magI, new Rect(0, cy, cx, cy));  // Bottom-Left
		Mat q3 = new Mat(magI, new Rect(cx, cy, cx, cy)); // Bottom-Right
		
		Mat tmp = new Mat();                           // swap quadrants (Top-Left with Bottom-Right)
		q0.copyTo(tmp);
		q3.copyTo(q0);
		tmp.copyTo(q3);
	
		q1.copyTo(tmp);                    // swap quadrant (Top-Right with Bottom-Left)
	    q2.copyTo(q1);
		tmp.copyTo(q2);

		Core.normalize(magI, magI, 0, 255, Core.NORM_MINMAX);
		//magI = enchanceWithLowPass(magI);
		
		
		//magI = new Mat(dftPlanes.size());
		magI = inverseDft(complexI, dftPlanes);
		
		
		
		    
		return magI;
	}
	
	private Mat inverseDft(Mat magI, ArrayList<Mat> dftPlanes)
	{		
		Core.merge(dftPlanes, magI);
		Core.dft(magI, magI, Core.DFT_INVERSE | Core.DFT_REAL_OUTPUT, 0);
		magI.convertTo(magI, 0);
		return magI;
	}
	
	
	private Mat enchanceWithLowPass(Mat magI)
	{
		//enhance image
		
		return magI;
	}
	
	
	
	
	
	
	
	public Mat getImage()
	{
		return this.image;
	}
	
}