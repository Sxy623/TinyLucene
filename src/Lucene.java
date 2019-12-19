import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Lucene {
	
	public String indexPath;
	public Anthology anthology;
	
	public Lucene(String indexPath, Anthology anthology) {
		this.indexPath = indexPath;
		this.anthology = anthology;
	} 

	public void createIndex() {
		File f = new File(indexPath);
		IndexWriter iwr = null;
		try {
			Directory dir = FSDirectory.open(f);
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_0,analyzer);
			iwr = new IndexWriter(dir, conf);
			
			for (PaperInfo info: anthology.paperInfos) {
				Document doc = getDocument(info);
				iwr.addDocument(doc);
			}
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			iwr.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Document getDocument(PaperInfo info) {
		Document doc = new Document();
		Field field = null;
		if (info.title != null) {
			field = new TextField("title", info.title, Field.Store.YES);
			doc.add(field);
		}
		if (info.author != null) {
			field = new TextField("author", info.author, Field.Store.YES);
			doc.add(field);
		}
		if (info.booktitle != null) {
			field = new TextField("booktitle", info.booktitle, Field.Store.YES);
			doc.add(field);
		}
		if (info.month != null) {
			field = new TextField("month", info.month, Field.Store.YES);
			doc.add(field);
		}
		if (info.year != null) {
			field = new TextField("year", info.year, Field.Store.YES);
			doc.add(field);
		}
		if (info.address != null) {
			field = new TextField("address", info.address, Field.Store.YES);
			doc.add(field);
		}
		if (info.publisher != null) {
			field = new TextField("publisher", info.publisher, Field.Store.YES);
			doc.add(field);
		}
		if (info.url != null) {
			field = new TextField("url", info.url, Field.Store.YES);
			doc.add(field);
		}
		if (info.pages != null) {
			field = new TextField("pages", info.pages, Field.Store.YES);
			doc.add(field);
		}
		return doc;
	}
	
	public String[][] search(String field, String queryStr, int number) {
		File f = new File(indexPath);
		try {
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(f)));
			Analyzer analyzer = new IKAnalyzer();
			QueryParser parser = new QueryParser(field, analyzer);
			
			Query query = parser.parse(queryStr);
			TopDocs hits = searcher.search(query, number);
			System.out.println(hits.scoreDocs.length);
			String[][] result = new String[number][2];
			int index = 0;
			for(ScoreDoc doc: hits.scoreDocs) {
				Document d = searcher.doc(doc.doc);
				result[index][0] = d.get("title");
				result[index][1] = d.get("url");
				index++;
			}
			System.out.println(index);
			return result;
		}
		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
