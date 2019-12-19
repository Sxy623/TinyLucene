import java.io.*;

public class Anthology {
	
	public PaperInfo[] paperInfos = new PaperInfo[53745];
	public int number = 0;
	
	public void analyzeFile(String path) {
		try {
			FileInputStream file = new FileInputStream(path);
			int fileLength = file.available();
			byte[] bytes = new byte[fileLength];
			file.read(bytes);
			file.close();
			String content = new String(bytes);
			String[] papers = content.split("}\n");
			int index = 0;
			for (String s: papers) {
				paperInfos[index] = new PaperInfo();
				paperInfos[index].index = index;
				String[] lines = s.split("\n");
				for (String line: lines) {
					
					line = line.trim();
					int length = line.length();
					
					if (line.startsWith("@inproceedings")) continue;
					
					if (line.startsWith("title")) {
						paperInfos[index].title = line.substring(9, length - 2);
					}
					else if (line.startsWith("author")) {
						if (line.indexOf('"') != line.lastIndexOf('"')) {
							paperInfos[index].author = line.substring(10, length - 2);
						}
						else  {
							paperInfos[index].author = line.substring(10, length);
						}
					}
					else if (line.startsWith("booktitle")) {
						paperInfos[index].booktitle = line.substring(13, length - 2);
					}
					else if (line.startsWith("month")) {
						paperInfos[index].month = line.substring(8, length - 1);
					}
					else if (line.startsWith("year")) {
						paperInfos[index].year = line.substring(8, length -2);
					}
					else if (line.startsWith("address")) {
						paperInfos[index].address = line.substring(11, length - 2);
					}
					else if (line.startsWith("publisher")) {
						paperInfos[index].publisher = line.substring(13, length - 2);
					}
					else if (line.startsWith("url")) {
						paperInfos[index].url = line.substring(7, length - 2);
					}
					else if (line.startsWith("pages")) {
						paperInfos[index].pages = line.substring(9, length - 2);
					}
					else {
						if (line.contains("\"")) {
							paperInfos[index].author += " " + line.substring(0, length - 2);
						}
						else paperInfos[index].author += " " + line;
					}
				}
				index++;
			}
			number = index;
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
	}
	
}
