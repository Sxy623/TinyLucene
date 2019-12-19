public class PaperInfo {
	
	public int index;
	public String title;
	public String author;
	public String booktitle;
	public String month;
	public String year;
	public String address;
	public String publisher;
	public String url;
	public String pages;
	public String abs;
	
	public void print() {
		System.out.println("index: " + index);
		System.out.println("title: " + title);
		System.out.println("author: " + author);
		System.out.println("booktitle: " + booktitle);
		System.out.println("month: " + month);
		System.out.println("year: " + year);
		System.out.println("address: " + address);
		System.out.println("publisher: " + publisher);
		System.out.println("url: " + url);
		System.out.println("pages: " + pages);
	}
}
