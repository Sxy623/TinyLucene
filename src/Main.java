import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			// Analyze .bib file
			String anthologyPath = "anthology.bib";
			Anthology anthology = new Anthology();
			anthology.analyzeFile(anthologyPath);
			
			// Download pdf files
			String dirPath = "data";
			for (int index = 20501; index <= 21000; index++) {
				if (anthology.paperInfos[index - 1].url == null) continue;
				String url = anthology.paperInfos[index - 1].url;
				if (!url.endsWith(".pdf")) {
					url += ".pdf";
				}
				String fileName = String.valueOf(index) + ".pdf";
				Downloader.downloadByUrl(url, fileName, dirPath);
			}
			
			// Recovery from file
			String infoPath = "info";
			File file = new File(infoPath);
			int start = 0;
			if (file.isFile() && file.exists()) {
				Scanner scan = new Scanner(file);
				while (scan.hasNext()) {
					anthology.paperInfos[start].abs = scan.nextLine();
					anthology.paperInfos[start].venue = scan.nextLine();
					start++;
				}
				scan.close();
			}
			
			// Crawl data
			FileWriter writer = new FileWriter(file, true);
			for (int index = start; index < anthology.number; index++) {
				try {
					System.out.println("Processing Paper No." + (index + 1) + "...");
					if (anthology.paperInfos[index].url == null) {
						writer.write("\n\n");
						System.out.println("No URL!");
						System.out.println();
						continue;
					}
				
					Document doc = Jsoup.connect(anthology.paperInfos[index].url).get();
			
					// Abstract
					Elements e = doc.getElementsByClass("card-body acl-abstract");
					if (e.hasText()) {
						anthology.paperInfos[index].abs = e.text().substring(9);
						System.out.println("Abstract: " + anthology.paperInfos[index].abs);
					}
			
					// Venue
					e = doc.getElementsByTag("dl");
					if (e.hasText()) {
						int s = e.text().indexOf("Venue:");
						if (s == -1) {
							s = e.text().indexOf("Venues:");
							s += 8;
						}
						else {
							s += 7;
						}
						int t = e.text().indexOf("SIG");
						anthology.paperInfos[index].venue = e.text().substring(s, t);
						System.out.println("Venue: " + anthology.paperInfos[index].venue);
					}
					
					if (anthology.paperInfos[index].abs != null) {
						writer.write(anthology.paperInfos[index].abs);
					}
					writer.write("\n");
					if (anthology.paperInfos[index].venue != null) {
						writer.write(anthology.paperInfos[index].venue);
					}
					writer.write("\n");
					writer.flush();
					System.out.println("Success!");
				}
				catch (Exception e) {
					writer.write("\n\n");
					System.out.println("Fail!");
				}
				System.out.println();
			}
			writer.close();
			
			// Create the index
			String indexPath = "index";
			Lucene lucene = new Lucene(indexPath, anthology);
			File indexFile = new File(indexPath);
			if (!indexFile.exists()) lucene.createIndex();
			
			// Show GUI
			new View(lucene);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
