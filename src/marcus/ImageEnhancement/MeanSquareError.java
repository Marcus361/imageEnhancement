package marcus.ImageEnhancement;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class MeanSquareError {
	private Mat original;
	private double error;
	
	public MeanSquareError(){
		this.original = Highgui.imread("C:/Users/Marcus Hayward/Dropbox/eclipseWorkspace/ImageEnhancement/src/marcus/ImageEnhancement/dogOriginal.bmp", Highgui.IMREAD_GRAYSCALE);
	}
	
//	public double calculateMeanSquareError()
//	{
//		//m = rows
//		//n = cols
//		error = 0;
//		for(int m = 0; m < original.rows(); m++){
//			for(int n = 0; n < original.cols(); n++){
//				//(f(x,y) - f2(x,y))^2
//				error += (original.get(m, n)[0] - newImage.get(m,n)[0])*(original.get(m, n)[0] - newImage.get(m,n)[0]);
//			}
//		}
//		return error / (original.cols() * original.rows());
//	}
	
	public double calculateMeanSquareError(Mat enhancedImage)
	{
		//m = rows
		//n = cols
		error = 0;
		for(int m = 0; m < this.original.rows(); m++){
			for(int n = 0; n < this.original.cols(); n++){
				//(f(x,y) - f2(x,y))^2
				error += (this.original.get(m, n)[0] - enhancedImage.get(m,n)[0])*(this.original.get(m, n)[0] - enhancedImage.get(m,n)[0]);
			}
		}
		return error / (this.original.cols() * this.original.rows());
	}
	
	
	
	
}
