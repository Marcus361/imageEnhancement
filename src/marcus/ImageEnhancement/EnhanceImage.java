package marcus.ImageEnhancement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class EnhanceImage {
	
	private Mat image;
	private Mat enhancedImage;
	private ArrayList<Histogram> histogram;
	private int limitOnNHE;
	
	public EnhanceImage()
	{
		this.image = Highgui.imread("C:/Users/Marcus Hayward/Dropbox/eclipseWorkspace/ImageEnhancement/src/marcus/ImageEnhancement/dogDistorted.bmp", Highgui.IMREAD_GRAYSCALE);
		this.enhancedImage = this.image.clone();
		limitOnNHE = 4;
	}
	
	public Mat getDistortedImage()
	{
		return this.image;
	}
	
	public Mat getEnhancedImage()
	{
		return this.enhancedImage;
	}
	
	public void setEnhancedImage(Mat mat)
	{
		this.enhancedImage = mat;
	}
	
	public void neighbourHoodEnhance()
	{
		limitOnNHE--;
		Mat temp = this.enhancedImage.clone();
		for(int m = 0; m < temp.rows(); m++){
			for(int n = 0; n < temp.cols(); n++){
				//Need to ensure the borders can averaged as well
				//5 states:
				//-top of image
				//-bottom of image
				//-left of image
				//-right of image
				//-middle of image
				
				//-top left corner
				if(m == 0 && n == 0){
					int greyValue = (int) (temp.get(m, n)[0]*2 + (temp.get(m, n+1)[0])*2
							             +(temp.get(m+1, n)[0])*2 + temp.get(m+1, n+1)[0]);
					greyValue = greyValue/7;
					temp.put(m, n, greyValue);
				}
				//top right corner
				else if(m == 0 && n == temp.cols()-1){
					int greyValue = (int) ((temp.get(m, n-1)[0])*2 + temp.get(m, n)[0]*2 +
					                       temp.get(m+1, n-1)[0] + (temp.get(m+1, n)[0])*2);
					greyValue = greyValue/7;
					temp.put(m, n, greyValue);
				}
				//bottom right corner
				else if(m == temp.rows()-1 && n == temp.cols()-1){
					int greyValue = (int) ((temp.get(m-1, n-1)[0])*2 + temp.get(m-1, n)[0]*2 + 
						                  temp.get(m, n-1)[0] + (temp.get(m, n)[0])*2);
					greyValue = greyValue/7;
					temp.put(m, n, greyValue);
				}
				//bottom left corner
				else if(m == temp.rows()-1 && n == 0){
					int greyValue = (int) ( (temp.get(m-1, n)[0])*2 + temp.get(m-1, n+1)[0] +
							                temp.get(m, n)[0]*2 + (temp.get(m, n+1)[0])*2);
					greyValue = greyValue/7;
					temp.put(m, n, greyValue);
				}
				//top of image
				else if(m == 0 ){
					int greyValue = (int)((temp.get(m, n-1)[0])*2 + temp.get(m, n)[0]*2 + (temp.get(m, n+1)[0])*2 + 
				                          temp.get(m+1, n-1)[0] + (temp.get(m+1, n)[0])*2 + temp.get(m+1, n+1)[0]);
					greyValue = greyValue/10;
					temp.put(m, n, greyValue);
				}
				
				//right side
				else if(n == temp.cols()-1){
					int greyValue = (int) (temp.get(m-1, n-1)[0] + (temp.get(m-1, n)[0])*2 + 
							               (temp.get(m, n-1)[0])*2 + temp.get(m, n)[0]*2 + 
						                  temp.get(m+1, n-1)[0] + (temp.get(m+1, n)[0])*2);
					greyValue = greyValue/10;
					temp.put(m, n, greyValue);
				}
				
				//bottom row
				else if(m == temp.rows()-1){
					int greyValue = (int) ((temp.get(m-1, n-1)[0])*2 + temp.get(m-1, n)[0]*2 + (temp.get(m-1, n+1)[0])*2 + 
						                   temp.get(m, n-1)[0] + (temp.get(m, n)[0])*2 + temp.get(m, n+1)[0]);
					greyValue = greyValue/10;
					temp.put(m, n, greyValue);
				}
				
				//left col
				else if(n == 0){
					int greyValue = (int) ( (temp.get(m-1, n)[0])*2 + temp.get(m-1, n+1)[0] +
											temp.get(m, n)[0]*2 + (temp.get(m, n+1)[0])*2 +
											(temp.get(m+1, n)[0])*2 + temp.get(m+1, n+1)[0]);
					greyValue = greyValue/10;
					temp.put(m, n, greyValue);
				}
				else{
					int greyValue = (int) (temp.get(m-1, n-1)[0] + (temp.get(m-1, n)[0])*2 + temp.get(m-1, n+1)[0] + 
							  (temp.get(m, n-1)[0])*2 + temp.get(m, n)[0]*2 + (temp.get(m, n+1)[0])*2 + 
						      temp.get(m+1, n-1)[0] + (temp.get(m+1, n)[0])*2 + temp.get(m+1, n+1)[0]);
				
				greyValue = greyValue/14;
				temp.put(m, n, greyValue);
				}
			}
		}
		
		MeanSquareError error = new MeanSquareError();
		double errorOfOldMatrix = error.calculateMeanSquareError(this.enhancedImage);
		double errorAfterNeighbourHood = error.calculateMeanSquareError(temp);
		if(errorOfOldMatrix > errorAfterNeighbourHood){
			this.enhancedImage = temp;
			if(limitOnNHE != 0 ){
				neighbourHoodEnhance();
			}
		}
	}
	
	public ArrayList<Histogram> getHistogramOfMat(Mat mat){
		histogram = new ArrayList<Histogram>();
		
		for(int m = 0; m < mat.rows(); m++){
			for(int n = 0; n < mat.cols(); n++){
				
				boolean flag = false;
				
				for(int x = 0; x < histogram.size(); x++){
					if(mat.get(m, n)[0] == histogram.get(x).greyValue){
						histogram.get(x).frequency++; //grey value detected, increase frequency
						x = histogram.size(); //breaks for loop
						flag = true; //do not add a new new item to the histogram
					}
				}
				if(!flag)
					histogram.add(new Histogram((int)mat.get(m, n)[0], 1));
			}
		}
		
		int runningTotal = 0;
		for(int i = 0; i < this.histogram.size(); i++)
		{
			runningTotal += this.histogram.get(i).frequency;
			this.histogram.get(i).frequency = runningTotal;
		}
		
		return this.histogram;
	}
	
	private void enhanceMatrixWithHistogram(int rowStart, int rowEnd, int colStart, int colEnd)
	{
		Mat regionOfInterest = this.image.submat(rowStart, rowEnd, colStart, colEnd);
		this.getHistogramOfMat(regionOfInterest);
		
		int CDFmin= 1000000;
		
		for(Histogram histogram : this.histogram){
			if(histogram.frequency < CDFmin){
				CDFmin = histogram.frequency;
			}
		}
		
		for(int m = rowStart; m < rowEnd; m++){
			for(int n = colStart; n < colEnd; n++){
				this.enhancedImage.put(m, n, calculateNewValueFromHistogram(this.enhancedImage.get(m, n), CDFmin, regionOfInterest.cols() * regionOfInterest.rows()));
			}
		}
		
	}
	
	private int calculateNewValueFromHistogram(double[] k, int CDFmin, int N){
		
		int temp = (int)k[0];

		int CDFvalue = 0;
		
		for(Histogram histogram : this.histogram){
			if(histogram.greyValue == temp){
				CDFvalue = histogram.frequency;
			}
		}
		double var1 = (CDFvalue - CDFmin);
		double var2  = (N - CDFmin);
		double var = var1 / var2;
		return (int) ((255)*(var));
	}
	
	public void histogramEnchanement()
	{
		this.enhanceMatrixWithHistogram(0,369,0,355);
	}
	
	public ArrayList<Histogram> getHistogram()
	{
		return this.histogram;
	}
	
	
	public Mat runPowerLawTransform(double c, double y)
	{
		Mat temp = this.enhancedImage.clone();
		for(int m = 0; m < temp.rows(); m++){
			for(int n = 0; n < temp.cols(); n++){
				//s = cr^y
				int greyValue = (int) (Math.pow(c*temp.get(m, n)[0], y));
			
				temp.put(m, n, greyValue);
			}
		}
		return temp;
	}
	
	public void powerLawTransform(){
		MeanSquareError error = new MeanSquareError();
		double lowestError = error.calculateMeanSquareError(this.enhancedImage);
		int bestCValue = 0;
		double bestYValue = 0;
		for(int c = 10; c < 20; c++)
		{
			for(int y = 18500; y < 19000; y++)
			{
				if(lowestError > error.calculateMeanSquareError(this.runPowerLawTransform((double)c/100, (double)y/10000))){
					bestCValue = c;
					bestYValue = y;
					lowestError = error.calculateMeanSquareError(this.runPowerLawTransform((double)c/100, (double)y/10000));
				}
			}
			System.out.println("On cycle: " + c);
		}

		System.out.println("best y value: "+ bestCValue);
		System.out.println("best c value: "+ bestYValue);
		try {
            File newTextFile = new File("C:/Users/Marcus Hayward/Dropbox/eclipseWorkspace/ImageEnhancement/src/marcus/ImageEnhancement/text.txt");

            FileWriter fw = new FileWriter(newTextFile);
            fw.write(bestCValue);
            fw.write((int) bestYValue);
            fw.close();

        } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        }
		System.out.println("best y value: "+ bestCValue);
		System.out.println("best c value: "+ bestYValue);
	}
	
}
