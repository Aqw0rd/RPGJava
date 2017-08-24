package Maths;

import java.util.Random;

public class Maths {
	public int randomInt(int a, int b){
		int r = new Random().nextInt(b-a)+1;
		return (int)map(r,0,b-a,a,b);
	}
	
	/**
	 * Limiting the range of a certain value, as it is not
	 * lower or higher than the min and max values wanted.
	 * E.g your range is 0-5 and the value is 6, this function 
	 * will then return 5.
	 * 
	 * @param x		Number to clamp
	 * @param min	Minimum value
	 * @param max	Maximum value
	 * @return		A number that lies within the defined range.
	 */
	public float clamp(float x, float min, float max) {
		if (x < min)
			return min;
		else if (x > max)
			return max;
		else
			return x;
	}

	/**
	 * Mapping a value within a range, to a new range. 
	 * E.g you generate a number within a range of 0-5
	 * and you want to convert this to 0-100. If your number
	 * is 4, then it is converted to 80, and so on.
	 * 
	 * @param x	Number you want to convert
	 * @param a Initial zero
	 * @param b Initial max
	 * @param c Wanted zero
	 * @param d Wanted max
	 * @return 	float value within your desired range.
	 */
	public float map(float x, float a, float b, float c, float d) {
		return (x - a) / (b - a) * (d - c) + c;
	}

	/**
	 * Used to convert any binary number to a decimal
	 * @param bin	Binary number
	 * @return		Decimal integer.
	 */
	public int binaryToDec(int bin) {
		int dec = 0;
		String str = Integer.toString(bin);
		for (int i = 0; i < str.length(); i++) {
			int a = Integer.parseInt(String.valueOf(str.charAt(i)));
			dec = dec * 2 + a;
		}
		return dec;
	}

}	
