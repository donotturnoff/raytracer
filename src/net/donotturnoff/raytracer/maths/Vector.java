package net.donotturnoff.raytracer.maths;

import java.util.Arrays;
import java.awt.Color;

public class Vector {
	
	private double[] array;
	
	public Vector(double... array) {
		this.array = array;
	}
	
	public Vector(Matrix matrix) {
		setArray(matrix);
	}
	
	public Vector(Color color) {
		setArray(color);
	}
	
	public Vector(Color color, boolean hasAlpha) {
		setArray(color, true);
	}
	
	public void setArray(double... array) {
		this.array = array;
	}
	
	public void setArray(Matrix matrix) {
		array = new double[matrix.rows()];
		for (int i = 0; i < matrix.rows(); i++) {
			array[i] = matrix.getElement(i, 0);
		}
	}
	
	public void setArray(Color color) {
		setArray(color, false);
	}
	
	public void setArray(Color color, boolean hasAlpha) {
		if (hasAlpha) {
			array = new double[4];
			array[3] = color.getAlpha();
		} else {
			array = new double[3];
		}
		array[0] = color.getRed();
		array[1] = color.getGreen();
		array[2] = color.getBlue();
	}
	
	public void setComponent(int index, double component) throws ArrayIndexOutOfBoundsException {
		array[index] = component;
	}
	
	public double[] getArray() {
		return array;
	}
	
	public double getComponent(int index) throws ArrayIndexOutOfBoundsException {
		return array[index];
	}
	
	public int components() {
		return array.length;
	}
	
	public boolean isZero() {
		for (double component: array) {
			if (component != 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean equals(Vector comparator) {
		double[] comparatorArray = comparator.getArray();
		if (array.length == comparatorArray.length) {
			for (int i = 0; i < array.length; i++) {
				if (comparatorArray[i] != array[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean opposite(Vector comparator) {
		return equals(negate());
	}
	
	public boolean parallel(Vector comparator) {
		if (isZero() || comparator.isZero()) {
			return true;
		}
		
		double[] comparatorArray = comparator.getArray();
		if (array.length == comparatorArray.length && array.length > 0) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] != 0) {
					double scale = comparatorArray[i]/array[i];
					return product(scale).equals(comparator);
				}
			}
		}
		
		return false;
	}
	
	public boolean antiparallel(Vector comparator) {
		return negate().parallel(comparator);
	}
	
	public double magnitude() {
		double sumOfSquares = 0;
		for (double component: array) {
			sumOfSquares += component*component;
		}
		return Math.sqrt(sumOfSquares);
	}
	
	public Vector normalize() {
		return product(1/magnitude());
	}
	
	public double total() {
		double total = 0;
		for (double component: array) {
			total += component;
		}
		return total;
	}
	
	public Vector negate() {
		double[] negatedArray = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			negatedArray[i] = -array[i];
		}
		return new Vector(negatedArray);
	}
	
	public Vector sum(Vector addend) throws IllegalArgumentException {
		double[] addendArray = addend.getArray();
		if (array.length == addendArray.length) {
			double[] sumArray = new double[array.length];
			for (int i = 0; i < array.length; i++) {
				sumArray[i] = array[i] + addendArray[i];
			}
			return new Vector(sumArray);
		} else {
			throw new IllegalArgumentException("Vector addition requires two vectors of the same size");
		}
	}
	
	public Vector difference(Vector subtrahend) throws IllegalArgumentException {
		return sum(subtrahend.negate());
	}
	
	public Vector hadamard(Vector multiplicand) throws IllegalArgumentException {
		double[] multiplicandArray = multiplicand.getArray();
		if (array.length == multiplicandArray.length) {
			double[] productArray = new double[array.length];
			for (int i = 0; i < array.length; i++) {
				productArray[i] = array[i] * multiplicandArray[i];
			}
			return new Vector(productArray);
		} else {
			throw new IllegalArgumentException("The Hadamard product requires two vectors of the same size");
		}
	}
	
	public Vector product(double multiplicand) {
		double[] productArray = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			productArray[i] = array[i] * multiplicand;
		}
		return new Vector(productArray);
	}
	
	public double dot(Vector operand) {
		double[] operandArray = operand.getArray();
		if (array.length == operandArray.length) {
			double dot = 0;
			for (int i = 0; i < array.length; i++) {
				dot += array[i] * operandArray[i];
			}
			return dot;
		} else {
			throw new IllegalArgumentException("The dot product requires two vectors of the same size");
		}
	}
	
	public Vector cross(Vector operand) {
		double[] a = array;
		double[] b = operand.getArray();
		if (a.length == 3 && b.length == 3) {
			return new Vector(a[1]*b[2]-a[2]*b[1], -(a[0]*b[2]-a[2]*b[0]), a[0]*b[1]-a[1]*b[0]);
		} else {
			throw new IllegalArgumentException("The cross product requires two vectors of the size 3");
		}
	}
	
	public double angle(Vector operand) {
		return Math.acos(dot(operand)/(magnitude()*operand.magnitude()));
	}
	
	public Color toColor() throws UnsupportedOperationException {
		if (array.length == 3) {
			return new Color((int) array[0], (int) array[1], (int) array[2]);
		} else if (array.length == 4) {
			return new Color((int) array[0], (int) array[1], (int) array[2], (int) array[3]);
		} else {
			throw new UnsupportedOperationException("Cannot cast vector with " + array.length + " components to color");
		}
	}
	
	public String toString() {
		return "Vector" + Arrays.toString(array);
	}
}
