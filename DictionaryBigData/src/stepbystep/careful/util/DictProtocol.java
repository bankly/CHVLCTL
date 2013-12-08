package stepbystep.careful.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stepbystep.careful.DictApplication;

public class DictProtocol {

	public static void convertDict(String value, BufferedWriter bw) {
		String result;

		result = value.replace("</a>:</span><span class=\"mexample\">", "+");
		result = result.replace("<br />", "\n");
		result = result.replace("<span class=\"title\">", "@");
		result = result.replace("</span></li>", "\n");

		result = result.replace("<li><span class=\"example\"><a href=\"entry://", "\n=");
		result = result.replace("<li><a href=\"entry://", "!");
		result = result.replace("<span class=\"type\">Idioms</span>", "");
		result = result.replace("\" class=\"aexample\">", "");

		result = result.replace("<li>", "\n-");
		result = result.replace("<span class=\"type\">", "\n*");

		result = result.replace("\" class=\"aidiom\">", "");
		result = result.replace("</a> <a href=\"entry://", "#");

		result = result.replace("<ul>", "");
		result = result.replace("</li>", "\n");
		result = result.replace("</ul>", "\n");
		result = result.replace("</span>", "\n");
		result = result.replace("</a>", "\n");
		result = result.replaceAll("(?m)^[ \t]*\r?\n", "");

		try {

			BufferedReader br = new BufferedReader(new StringReader(result), 2048);
			String line;

			String fistLine = formatApostrophe(br.readLine());
			if (fistLine.startsWith("@")) {
				bw.write(fistLine);
			} else {
				bw.write("@" + fistLine);
			}

			while ((line = br.readLine()) != null) {
				if (line.startsWith("=")) {
					String[] temp = line.substring(1).split("\\+");
					String[] arrWord = temp[0].split("#");
					StringBuilder add = new StringBuilder();
					add.append("=");
					for (String string : arrWord) {
						add.append(string.substring(0, string.length() / 2)).append(" ");
					}
					bw.write("\n");
					bw.write(formatApostrophe(add.toString()));
					if (temp.length == 2) {
						bw.write("\n");
						bw.write("+" + formatApostrophe(temp[1]));
					}
				} else if (line.startsWith("!")) {

					String[] arrWord = line.substring(1).split("#");
					StringBuilder add = new StringBuilder();
					add.append("!");
					for (String string : arrWord) {
						add.append(string.substring(0, string.length() / 2)).append(" ");
					}
					bw.write("\n");
					bw.write(formatApostrophe(add.toString()));

				} else {
					bw.write("\n");
					bw.write(formatApostrophe(line));
				}

			}
			//bw.write("\n");
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String formatApostrophe(String value) {
		Matcher matcher = Pattern.compile("[^ ]\"[^ ]").matcher(value);
		String temp = null;
		while (matcher.find()) {
			temp = matcher.group(0);
			value = value.replace(temp, temp.replace("\"", "'"));
		}

		return value;
	}

	public static void convertIndex(String value) {
		File file = new File(DictApplication.PATH_SDCARD + File.separator + DictApplication.FOLDER
		        + File.separator + DictApplication.FOLDER_EV + File.separator + "index.txt");

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file, true);

			fw.write(formatApostrophe(value) + "\n");
			fw.flush();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void convertPos() {

		File filePos = new File(DictApplication.PATH_SDCARD + File.separator
		        + DictApplication.FOLDER + File.separator + DictApplication.FOLDER_EV
		        + File.separator + "pos.txt");

		try {
			if (!filePos.exists()) {
				filePos.createNewFile();
			}

			OptimizedRandomAccessFile oraf = new OptimizedRandomAccessFile(new File(
			        DictApplication.PATH_SDCARD + File.separator + DictApplication.FOLDER
			                + File.separator + DictApplication.FOLDER_EV + File.separator
			                + "dict.dict"), "r");

			FileWriter fw = new FileWriter(filePos, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(MyConvert.convertDecimalToBase64(0) + "\t");
			String line;
			oraf.seek(0);
			long start = 0;
			long end;

			while ((line = oraf.readLine()) != null) {

				if (line.trim().isEmpty()) {
					end = oraf.getFilePointer();
					bw.write(MyConvert.convertDecimalToBase64(end - 2 - start) + "\n");
					bw.write(MyConvert.convertDecimalToBase64(end) + "\t");
					start = end;
				}

			}

			end = oraf.getFilePointer();
			bw.write(MyConvert.convertDecimalToBase64(end - start));
			bw.flush();
			fw.flush();
			bw.close();
			fw.close();

			oraf.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void convertIndexFull() {
		File fileIndex = new File(DictApplication.PATH_SDCARD + File.separator
		        + DictApplication.FOLDER + File.separator + DictApplication.FOLDER_EV
		        + File.separator + "index.txt");
		File filePos = new File(DictApplication.PATH_SDCARD + File.separator
		        + DictApplication.FOLDER + File.separator + DictApplication.FOLDER_EV
		        + File.separator + "pos.txt");

		File fileIndexFull = new File(DictApplication.PATH_SDCARD + File.separator
		        + DictApplication.FOLDER + File.separator + DictApplication.FOLDER_EV
		        + File.separator + "dict.index");

		try {
			BufferedReader brIndex = new BufferedReader(new FileReader(fileIndex));
			BufferedReader brPos = new BufferedReader(new FileReader(filePos));
			String word;
			String pos;
			FileWriter fw = new FileWriter(fileIndexFull, true);
			while ((word = brIndex.readLine()) != null && (pos = brPos.readLine()) != null) {
				fw.write(word + "\t" + pos + "\n");
			}

			fw.flush();
			brIndex.close();
			brPos.close();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
