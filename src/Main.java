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
			for (int index = 20640; index <= 21000; index++) {
				if (anthology.paperInfos[index - 1].url == null) continue;
				String url = anthology.paperInfos[index - 1].url;
				if (!url.endsWith(".pdf")) {
					url += ".pdf";
				}
				String fileName = String.valueOf(index) + ".pdf";
				Downloader.downloadByUrl(url, fileName, dirPath);
			}
			
			// Crawl data
			for (int index = 280; index < anthology.number; index++) {
				try {
					System.out.println("Processing Paper No." + (index + 1) + "...");
					if (anthology.paperInfos[index].url == null) {
						System.out.println("No URL!");
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
						int s = e.text().indexOf("Venues:");
						int t = e.text().indexOf("SIG:");
						anthology.paperInfos[index].venue = e.text().substring(s + 8, t);
						System.out.println("Venue: " + anthology.paperInfos[index].venue);
					}
					
					System.out.println("Success!");
				}
				catch (Exception e) {
					System.out.println("Fail!");
				}
				System.out.println();
			}
			
			// Create the index
			String indexPath = "index";
			Lucene lucene = new Lucene(indexPath, anthology);
			lucene.createIndex();
			
			// Show GUI
			new View(lucene);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
