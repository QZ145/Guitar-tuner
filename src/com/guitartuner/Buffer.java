package com.guitartuner;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Buffer {

	private Queue <short[]> qe = new LinkedList<short[]>();
	private int count=0;
	private final int MAX_COUNT;
	public double[] data = null;
	private final int N;
	
	Buffer(int N, int max) {
		MAX_COUNT = max;
		this.N = N;
		data = new double[MAX_COUNT*N/2];
	}
	
	public int getCount() {
		return count;
	}
	
	public void collect(short[] x) {
		if(count!=MAX_COUNT) {
			qe.add(x);
			count++;
			makeDoubleArray();
		}
		else {
			makeDoubleArray();
			qe.poll();
			qe.add(x);
			System.gc();
		}
	}
	
	private void makeDoubleArray() {
		short[] tmp = null;
		int i, j;
		Iterator<short[]> it = qe.iterator();
		for(j=0; j<MAX_COUNT; j++) {
			if(it.hasNext()) {
				tmp = (short[]) it.next();
				for(i=0; i<N/2; i++) {
					data[j*N/2+i] = (double) tmp[i];
				}
			}
		}
	}
}
