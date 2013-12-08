package stepbystep.careful.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class HandlingStringVV {

	public static String formatString(String str) {
		StringBuilder builder = new StringBuilder();
		BufferedReader br = new BufferedReader(new StringReader(str));
		String thisLine;

		try {
			thisLine = br.readLine();
			builder.append("<style text=\"text/css\">	" + "@font-face {	font-family: \'My Font\';"
			        + "src: url(\'file:///android_asset/fonts/DejaVuSans.ttf\');	}	"
			        + "body {	font-family: \'My Font\', Verdana, sans-serif;	}</style>");

			builder.append("<div style='color:red;" + "font-size:22px;'>"
			        + thisLine.substring(1, thisLine.length()) + "</div>");

			while ((thisLine = br.readLine()) != null) {

				if (thisLine.startsWith("-")) {

					builder.append("<div style='"
					        + "font-size:20px; background:URL(file:///android_asset/nghiatu.png);"
					        + "background-repeat:no-repeat; background-position: 0% 5px; text-indent: 15px;"
					        + "margin-top:10px;text-align:left top;'>"
					        + thisLine.subSequence(1, thisLine.length()) + "</div>");

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new String(builder);
	}
}
