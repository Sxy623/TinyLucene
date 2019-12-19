public class Main {
	
	public static void main(String[] args) {
		
		String anthologyPath = "anthology.bib";
		Anthology anthology = new Anthology();
		anthology.analyzeFile(anthologyPath);
		
		String indexPath = "index";
		Lucene lucene = new Lucene(indexPath, anthology);
		lucene.createIndex();
		
		View view = new View(lucene);
	}
	
}
