package stepbystep.careful.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class HandlingStringEV {

	public static String formatString(String str) {
		StringBuilder builder = new StringBuilder();
		BufferedReader br = new BufferedReader(new StringReader(str));
		String thisLine;
		String[] meaning;
		boolean readAsterisk = true;
		boolean readingExclaimationFirstTime = true;
		boolean checkAsterisk = false;
	
		try {
			thisLine = br.readLine();
			builder.append("<!DOCTYPE html> <html><head><style text=\"text/css\">"
			        + "a:link{text-decoration:none;}"
			        + "a:visited{text-decoration:none;}"
			        + "a:active{text-decoration:none;}"
			        +

			        "@font-face {font-family: \'MyFont\';	src: url(\'file:///android_asset/fonts/DejaVuSans.ttf\');}" +
			        "@font-face {font-family: \'MyFontItalic\';	src: url(\'file:///android_asset/fonts/DejaVuSans-Oblique.ttf\');}" +
			       
			        "div {font-family: \'MyFont\',  sans-serif;}"

			        + "</style></head><body>");
			builder.append("<div style='color:red; font-family:MyFont; font-style:oblique;" + "font-size:20px;'>"
			        + thisLine.substring(1, thisLine.length()) + "</div>");
			
			while ((thisLine = br.readLine()) != null) {
				if (thisLine.startsWith("*")) {
					readAsterisk = true;
					readingExclaimationFirstTime = true;
					checkAsterisk = true;
					builder.append("<div style='color:blue; color:#00598C;"
					        + "font-size:20px; background:URL(file:///android_asset/tuloai.png);"
					        + "background-repeat:no-repeat; background-size:12px 12px;   text-indent: 15px;"
					        + "background-position: 0% 7px; text-align:left center;'>"
					        + thisLine.subSequence(1, thisLine.length()) + "</div>");

				} else if (thisLine.startsWith("-")) {
					if (readAsterisk == true) {
						String[] arrTempString = thisLine.substring(1, thisLine.length()).split(" ");
						StringBuilder builderTemp = new StringBuilder();
						handlePreSufWord(arrTempString, builderTemp, "185D00");

						if (checkAsterisk == true) {
							builder.append("<div style=' color:#185D00;"
							        + "font-size:20px; background:URL(file:///android_asset/nghiatu.png);"
							        + "background-repeat:no-repeat; background-size:12px 12px;  background-position: 0% 7px;   margin-left:15px; text-indent: 15px;;"
							        + "text-align:left top;'>" + builderTemp + "</div>");
						} else {
							builder.append("<div style=' color:#185D00;"
							        + "font-size:20px; background:URL(file:///android_asset/nghiatu.png);"
							        + "background-repeat:no-repeat; background-size:12px 12px;  background-position: 0% 5px; text-indent: 15px;;"
							        + "text-align:left top;'>" + builderTemp + "</div>");

						}
					} else {
						String[] arrTempString = thisLine.substring(1, thisLine.length()).split(" ");
						StringBuilder builderTemp = new StringBuilder();
						handlePreSufWord(arrTempString, builderTemp, "000000");
						builder.append("<div style='"
						        + "font-size:20px; background:URL(file:///android_asset/nghiathanhngu.png);"
						        + "background-repeat:no-repeat; background-position: 0% 2px; text-indent: 20px; margin-left:35px;"
						        + "text-align:left center;'>"
						        + builderTemp + "</div>");
					}
				} else if (thisLine.startsWith("=")) {

					meaning = thisLine.split("\\+");
					String[] arrTempString = meaning[0].substring(1, meaning[0].length())
					        .split(" ");
					StringBuilder builderTemp = new StringBuilder();
					handlePreSufWord(arrTempString, builderTemp, "000000");

					String[] arrTemp = null;
					StringBuilder builderTemp1 = new StringBuilder();
					if (meaning.length > 1) {
						arrTemp = meaning[1].split(" ");
						handlePreSufWord(arrTemp, builderTemp1, "000000");
					}

					if (readAsterisk == true) {
						if (checkAsterisk == true) {

							if (meaning.length > 1) {

								builder.append("<div style='background:URL(file:///android_asset/vidu.png);"
								        + "background-repeat:no-repeat; margin-left: 30px;background-position: 0% 5px;"
								        + "text-indent: 15px;;text-align:left center;font-size:20px;'>"
								        + builderTemp + "</div>");
								builder.append("<div style='background:URL(file:///android_asset/nghiavidu.png);"
								        + "background-repeat:no-repeat; font-size:20px;"
								        + "margin-left:45px; font-family:MyFontItalic; background-position: 0% 1px;"
								        + "text-indent: 10px;text-align:left center;'>"
								        + builderTemp1 + "</div>");
							} else {
								builder.append("<div style='background:URL(file:///android_asset/vidu.png);"
								        + "background-repeat:no-repeat; margin-left: 30px;background-position: 0% 5px;"
								        + "text-indent: 15px;;text-align:left center;font-size:20px;'>"
								        + builderTemp + "</div>");
							}
						} else {
							if (meaning.length > 1) {
								builder.append("<div style='background:URL(file:///android_asset/vidu.png);"
								        + "background-repeat:no-repeat; margin-left: 10px;background-position: 0% 5px;"
								        + "text-indent: 15px;;text-align:left center;font-size:20px; color:#282828;'>"
								        + builderTemp + "</div>");
								builder.append("<div style='background:URL(file:///android_asset/nghiavidu.png);"
								        + "background-repeat:no-repeat;  "
								        + "margin-left:25px; font-family:MyFontItalic; font-size:20px; "
								        + "text-indent: 10px;;text-align:left center;background-position: 0% 1px;'>"
								        + builderTemp1 + "</div>");
							} else {
								builder.append("<div style='background:URL(file:///android_asset/vidu.png);"
								        + "background-repeat:no-repeat; margin-left: 10px;background-position: 0% 5px;"
								        + "text-indent: 15px;;text-align:left center;font-size:20px; color:#282828;'>"
								        + builderTemp + "</div>");
							}
						}
					} else {
						builder.append("<div style='background:URL(file:///android_asset/vidu.png);"
						        + "background-repeat:no-repeat;margin-left: 50px;"
						        + "text-indent: 15px;; font-size: 20px; text-align:left center;background-position: 0% 5px; '>"
						        + builderTemp + "</div>");
						builder.append("<div style='background:URL(file:///android_asset/nghiavidu.png);"
						        + "background-repeat:no-repeat; "
						        + "margin-left:65px; font-family:MyFontItalic;"
						        + "text-indent: 10px;;font-size: 20px;text-align:left center; background-position: 0% 1px;'>"
						        + builderTemp1 + "</div>");
					}

				} else if (thisLine.startsWith("!")) {
					readAsterisk = false;
					if (readingExclaimationFirstTime == true) {
						builder.append("<div style='background:URL(file:///android_asset/idiom.png); background-position: 0% 5px;"
						        + "background-repeat:no-repeat; background-size:12px 12px; background-position:left;"
						        + "text-indent: 17px;;text-align:left center; color:#E75952;font-size:22px;'>"
						        + "Idioms" + "</div>");
					}
					String[] arrTempString = thisLine.substring(1, thisLine.length()).split(" ");
					StringBuilder builderIdomTemp = new StringBuilder();
					handlePreSufWord(arrTempString, builderIdomTemp, "17658C");
					builder.append("<div style='background:URL(file:///android_asset/vidu_thanhngu.png);"
					        + "background-repeat:no-repeat;margin-left:12px; "
					        + "text-indent:20px;font-size:20px;text-align:left center; background-position: 0% 0px; color:#17658C; font-size:20px;'>"
					        + builderIdomTemp + "</div>");
					readingExclaimationFirstTime = false;
				} else if(thisLine.startsWith("@")) {
					builder.append("<div style='color:blue; color:#f77908;"
					        + "font-size:20px; background:URL(file:///android_asset/chuyennganh.png);"
					        + "background-repeat:no-repeat; background-size:12px 12px;   text-indent: 15px;"
					        + "background-position: 0% 7px; text-align:left center;'>"
					        + thisLine.subSequence(1, thisLine.length()) + "</div>");
					readAsterisk = true;
					checkAsterisk = true;
					
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		builder.append("</body></html>");

		return new String(builder);
	}

	private static void handlePreSufWord(String[] arrTempString, StringBuilder builderTemp,
	        String color) {
		String tempAdd = "";
		String end = "";
		String start = "";
		char[] markedSentence = { '!', '\"', '\'', '(', ')', ',', '.', ':', ';', '?', '[', ']',
		        '{', '}' };
		Pattern pattern = Pattern.compile("[!\"\'(),.:;?\\[\\]{}]");
		for (String string : arrTempString) {
			int beginPos = 0;
			int endPos = 0;
			Matcher matcher = pattern.matcher(string);
			if (matcher.find()) {
				char[] chArr = string.toCharArray();
				for (char c : chArr) {
					if (Arrays.binarySearch(markedSentence, c) < 0) {
						beginPos = string.indexOf(c);
						break;
					}
				}

				for (int i = chArr.length - 1; i > 0; i--) {
					if (Arrays.binarySearch(markedSentence, chArr[i]) < 0) {
						endPos = i;
						break;
					}
				}

				tempAdd = string.substring(beginPos, endPos + 1);
				end = string.substring(endPos + 1, string.length());
				start = string.substring(0, beginPos);
			} else {
				tempAdd = string;
				end = "";
				start = "";
			}

			builderTemp.append(start + "<a style= \"color: #" + color + ";\"   href=\"" + tempAdd
			        + "\">" + tempAdd + "</a>" + end + " ");
		}
	}
}
