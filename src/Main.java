import java.net.HttpURLConnection;
import java.net.URL;

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
			
			// Create the index
			String indexPath = "index";
			Lucene lucene = new Lucene(indexPath, anthology);
			lucene.createIndex();
			
			// Show GUI
			View view = new View(lucene);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
