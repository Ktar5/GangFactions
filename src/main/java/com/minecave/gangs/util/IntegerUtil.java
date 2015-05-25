package com.minecave.gangs.util;

public class IntegerUtil {

	/**
	 * Limits a number to an upper bound
	 *
	 * @param number The number to limit
	 * @param limit The upper bound value
	 * @return Returns limit if the number is above limit, otherwise returns number
	 */
	public static int limitNumber(int number, int limit) {
		return number > limit ? limit : number;
	}

}
