package com.guitartuner;

import org.apache.commons.math3.complex.*;
import org.apache.commons.math3.transform.*;

public class FrequencyDetector {
	FrequencyDetector() {
		this.fs=8000;
	}
	FrequencyDetector(double fs) {
		this.fs=fs;
	}
	double fs;
	public double detect(double[] x) {
		int i, imax1, imax2, imax=0;
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		double[] w = hammingWindow(x.length);
		for(i=0; i<x.length; i++)
			x[i]*=w[i];
		Complex[] spectrum = fft.transform(x, TransformType.FORWARD);

		for(i=imax1=(int) (spectrum.length*50/fs); i<spectrum.length*350/fs; i++) {
			if(spectrum[i].abs()>spectrum[imax1].abs())
				imax1=i;
		}
		imax2 = imax1/2;
		if(imax2>(spectrum.length*50/fs)) {
			for(i=imax2-5; i<imax2+6; i++) {
				if(spectrum[i].abs()>spectrum[imax2].abs())
					imax2 = i;
			}
			if(spectrum[imax1].abs()>spectrum[imax2].abs()*10)
				imax = imax1;
			else imax = imax2;
		}
		else imax = imax1;

		return (double)imax*fs/(x.length);
	}
	
	private double[] hammingWindow(int N){
		double[] w = new double[N];
		for(int i=0; i<N; i++)
			w[i] = 0.53836 - 0.46164*Math.sin(2*Math.PI*i/(N-1));
		return w;
	}
	
	public char getNote(double f) {
		char note;
		if(f<96)
			note = 'E';
		else if(f<128.5)
			note = 'A';
		else if(f<172)
			note = 'd';
		else if(f<221)
			note = 'g';
		else if(f<288)
			note = 'b';
		else note = 'e';
		
		return note;
	}
	
	public double getDeviation(double f, char note) {
		double deviation;
		switch(note) {
		case 'E':
			deviation = f/82.41;
			break;
		case 'A':
			deviation = f/110;
			break;
		case 'd':
			deviation = f/146.83;
			break;
		case 'g':
			deviation = f/196;
			break;
		case 'b':
			deviation = f/246.94;
			break;
		default:
			deviation = f/329.63;
			break;
		}
		deviation = (deviation-1)*500;
		if(deviation < -88)
			return -88;
		if(deviation > 88)
			return 88;
		return deviation;
	}
}
