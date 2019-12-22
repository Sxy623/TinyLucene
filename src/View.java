import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	Lucene lucene;
	
    String[] attributes = new String[] {"Title", "URL"};
    String[][] tableContents;
	
	public View(Lucene lucene) {
		this.lucene = lucene;
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		// Set title and size
		setTitle("Tiny Lucene");
		setBounds(150, 100, 800, 600);
		Container container = getContentPane();
		container.setLayout(null);
		
		// Title
		JLabel label = new JLabel("Tiny Lucene");
		label.setFont(new Font("Helvetica Neue", Font.BOLD, 45));
		label.setBounds(35, 20, 500, 50);
		container.add(label);
		
		// Query text field
		JTextField textField = new JTextField();	
		textField.setBounds(30, 80, 570, 30);
		container.add(textField);
		
		// Radio Button
		JRadioButton radioButton1 = new JRadioButton("Title", true);
		JRadioButton radioButton2 = new JRadioButton("Author");
		JRadioButton radioButton3 = new JRadioButton("Abstract");
		JRadioButton radioButton4 = new JRadioButton("Venue");
		radioButton1.setBounds(30, 120, 180, 20);
		radioButton2.setBounds(220, 120, 180, 20);
		radioButton3.setBounds(410, 120, 180, 20);
		radioButton4.setBounds(600, 120, 180, 20);
		container.add(radioButton1);
		container.add(radioButton2);
		container.add(radioButton3);
		container.add(radioButton4);
		ButtonGroup group = new ButtonGroup();
		group.add(radioButton1);
		group.add(radioButton2);
		group.add(radioButton3);
		group.add(radioButton4);
		
		// Table
        DefaultTableModel tableModel = new DefaultTableModel(tableContents, attributes);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 150, 740, 400);
        container.add(scrollPane);
		
		// Button
		JButton button = new JButton("Search");
		button.setBounds(630, 80, 140, 30);
		// Search and refresh the table when button pressed
		button.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent arg0) {
				String query = textField.getText().trim();
				if (radioButton1.isSelected()) {
					attributes = new String[] {"Title", "URL"};
					tableContents = lucene.search("title", query, 50);
				}
				else if (radioButton2.isSelected()) {
					attributes = new String[] {"Title", "Author", "URL"};
					tableContents = lucene.search("author", query, 50);
				}
				else if (radioButton3.isSelected()) {
					attributes = new String[] {"Title", "URL"};
					tableContents = lucene.search("abstract", query, 50);
				}
				else {
					attributes = new String[] {"Title", "Venue", "URL"};
					tableContents = lucene.search("venue", query, 50);
				}
				tableModel.setDataVector(tableContents, attributes);
				tableModel.fireTableDataChanged();
			}
		});
		container.add(button);
	}
	
}
