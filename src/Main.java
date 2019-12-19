import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		String anthologyPath = "anthology.bib";
		Anthology anthology = new Anthology();
		anthology.analyzeFile(anthologyPath);
		
		String indexPath = "index";
		Lucene lucene = new Lucene(indexPath, anthology);
		lucene.createIndex();
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Field: ");
		String field = scan.next();
		System.out.print("Content: ");
		String content = scan.next();
		System.out.print("Number: ");
		int number = scan.nextInt();
		lucene.search(field, content, number);
		
		scan.close();
	}
	
}
