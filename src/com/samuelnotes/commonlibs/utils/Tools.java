package com.samuelnotes.commonlibs.utils;

public class Tools {
	/**
	 * ��ʽ������
	 * @param m
	 * @return
	 */
	public static String formatDistance(String m){
		double mDou = 0;
		String mEnd = "��";
		String kmEnd = "ǧ��";
		try {
			mDou = Double.parseDouble(m);
			if(mDou >= 1000){
				mDou = mDou/1000;
				String re = String.valueOf(mDou);
				if(mDou > 10){
					return ">10"+kmEnd;
				}
				if(re.length() > 3) re = re.substring(0, 2);
				return re + kmEnd;
			}
			
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		return m+mEnd;
	}
	
}
