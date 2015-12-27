import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.ToolRunner;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import fr.univtours.polytech.MoteurHadoop.MapReduce.FirstJob;
import fr.univtours.polytech.MoteurHadoop.MapReduce.SecondJob;
import fr.univtours.polytech.MoteurHadoop.filter.Filter;
import fr.univtours.polytech.MoteurHadoop.signextractors.SignExtractor;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

/***
 * le frame
 * @author hduser
 *
 */
public class Frame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtoutputcosine;
	private JTextField txtoutputinvertedindex_2;
	private JTextField textField_6;
	private List<String> filters;
	private String exactor;
	/** La barre de progression. */
	private JProgressBar progressBar_1;
	/** Le message de progression. */
	private JLabel progressMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		
		final String packageExactor = "fr.univtours.polytech.MoteurHadoop.signextractors.";
		final String packageFilter = "fr.univtours.polytech.MoteurHadoop.filter.";
		filters = new ArrayList<String>();
		
		progressMessage = new JLabel("Preparing ...");
		progressBar_1 = new JProgressBar();

		final JTextArea textArea_1 = new JTextArea();
		final JTextAreaOutputStream out_1 = new JTextAreaOutputStream(
				textArea_1);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//tabbedPane.add(createProgressPanel());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 593, 361);
		ImageIcon imageIcon = new ImageIcon();

		final JPanel panelResearch = new JPanel();
		tabbedPane.addTab("Research", imageIcon, panelResearch,
				"clic for research");
		panelResearch.setLayout(null);

		txtoutputcosine = new JTextField();
		txtoutputcosine.setText("/output/cosine");
		txtoutputcosine.setColumns(10);
		txtoutputcosine.setBounds(88, 42, 192, 19);
		panelResearch.add(txtoutputcosine);

		txtoutputinvertedindex_2 = new JTextField();
		txtoutputinvertedindex_2.setText("/input/invertedindex");
		txtoutputinvertedindex_2.setColumns(10);
		txtoutputinvertedindex_2.setBounds(88, 11, 192, 19);
		panelResearch.add(txtoutputinvertedindex_2);

		JLabel label = new JLabel("Input");
		label.setBounds(12, 13, 70, 15);
		panelResearch.add(label);

		JLabel label_1 = new JLabel("Output");
		label_1.setBounds(12, 44, 70, 15);
		panelResearch.add(label_1);

		final JButton btnResearch = new JButton("Research");
		btnResearch.setBounds(457, 125, 117, 25);
		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Convert the case");
		chckbxNewCheckBox.setBounds(288, 11, 168, 23);
		panelResearch.add(chckbxNewCheckBox);
		
		final JCheckBox chckbxRemoveAccents = new JCheckBox("Remove accents");
		chckbxRemoveAccents.setBounds(288, 38, 168, 23);
		panelResearch.add(chckbxRemoveAccents);
		
		final JCheckBox chckbxDisposeOfEmpty = new JCheckBox("Dispose of empty words");
		chckbxDisposeOfEmpty.setBounds(288, 65, 201, 23);
		panelResearch.add(chckbxDisposeOfEmpty);
		
		final JCheckBox chckbxgram = new JCheckBox("3-Gram");
		chckbxgram.setBounds(493, 11, 129, 23);
		panelResearch.add(chckbxgram);
		
		final JCheckBox chckbxWord = new JCheckBox("Word");
		chckbxWord.setBounds(493, 65, 129, 23);
		panelResearch.add(chckbxWord);
		
		final JCheckBox chckbxRadicalizeWords = new JCheckBox("Radicalize words");
		chckbxRadicalizeWords.setBounds(288, 92, 201, 23);
		panelResearch.add(chckbxRadicalizeWords);
		
		chckbxgram.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(chckbxgram.isSelected() == true) {
					chckbxRadicalizeWords.setEnabled(false);
					chckbxDisposeOfEmpty.setEnabled(false);	
					chckbxDisposeOfEmpty.setSelected(false);
					chckbxRadicalizeWords.setSelected(false);
					chckbxWord.setEnabled(false);	
				}
				if(chckbxgram.isSelected() == false) {
					chckbxRadicalizeWords.setEnabled(true);
					chckbxDisposeOfEmpty.setEnabled(true);		
					chckbxWord.setEnabled(true);	
				}
			}
			
		});
		
		chckbxWord.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(chckbxWord.isSelected() == true) {
					chckbxgram.setEnabled(false);
				}
				if(chckbxWord.isSelected() == false) {
					chckbxgram.setEnabled(true);
				}
			}
			
		});
		
		//faire le recherche dans un autre thread
		btnResearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Runnable r = new Runnable() {
					public void run(){
						
						if(chckbxNewCheckBox.isSelected() == false && 
								chckbxRemoveAccents.isSelected() == false &&
										chckbxRadicalizeWords.isSelected() == false &&
												chckbxDisposeOfEmpty.isSelected() == false) {
							JOptionPane.showMessageDialog(null, "Choose au least one filter please");
							//System.out.println("Choose au least one filter");
							btnResearch.setEnabled(true);
							return;
						}
						
						if(chckbxWord.isSelected() == false && 
								chckbxgram.isSelected() == false) {
							JOptionPane.showMessageDialog(null, "Choose a extractor please");
							//System.out.println("Choose a extractor");
							btnResearch.setEnabled(true);
							return;
						}
						
						if(textField_6.getText().length() == 0) {
							JOptionPane.showMessageDialog(null, "Give me a question ..");
							//System.out.println("Choose a extractor");
							btnResearch.setEnabled(true);
							return;
						}
						
						//Filters
						if(chckbxNewCheckBox.isSelected() == true)
							filters.add(packageFilter + "CaseFilter");
						if(chckbxRemoveAccents.isSelected() == true)
							filters.add(packageFilter + "AccentFilter");
						if(chckbxRadicalizeWords.isSelected() == true)
							filters.add(packageFilter + "StemmingFilter");
						if(chckbxDisposeOfEmpty.isSelected() == true)
							filters.add(packageFilter + "StopWordFilter");
						
						//Extractor
						if(chckbxWord.isSelected() == true)
							exactor = packageExactor + "WordExtractor";
						if(chckbxgram.isSelected() == true)
							exactor = packageExactor + "WordExtractor";
						beginResearch(btnResearch, textArea_1, out_1);
					}
				};
				Thread t = new Thread(r, "code executor");
				t.start();
				btnResearch.setEnabled(false);
			}

		});
		panelResearch.add(btnResearch);

		textArea_1.setBounds(64, 13, 419, 99);
		// panelResearch.add(textArea_1);

		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(88, 104, 192, 19);
		panelResearch.add(textField_6);

		JLabel lblQuestion = new JLabel("Question");
		lblQuestion.setBounds(12, 106, 70, 15);
		panelResearch.add(lblQuestion);

		JScrollPane scrollPane_1 = new JScrollPane(textArea_1);
		scrollPane_1.setBounds(12, 183, 562, 110);
		panelResearch.add(scrollPane_1);

		progressBar_1.setBounds(12, 162, 562, 15);
		panelResearch.add(progressBar_1);
		
		progressMessage.setBounds(12, 135, 183, 15);
		panelResearch.add(progressMessage);
		
		

		getContentPane().add(tabbedPane);
	}

	/**
	 * Fait l'indexation et recherche
	 * 
	 * @param jButton
	 *            le composant jButton
	 * @param textArea_1
	 *            le composant textArea
	 * @param out_1
	 *            le composant JTextAreaOutputStream
	 */
	void beginResearch(JButton jButton, JTextArea textArea_1,
			JTextAreaOutputStream out_1) {

		System.setOut(new PrintStream(out_1));
		System.setErr(new PrintStream(out_1));
		String[] str1 = { "index", "", txtoutputinvertedindex_2.getText(),
				"/output/invertedindex" };

		hadoop(str1, exactor, filters);
		textArea_1.append("Index finished \n");
		textArea_1.append("------------------------------------ \n");
		textArea_1.append("Research begin \n");
		textArea_1.append("Your question is : " + textField_6.getText() + "\n");
		String[] str2 = { "question", "", "/output/invertedindex",
				txtoutputcosine.getText(), textField_6.getText() };
		hadoop(str2, exactor, filters);
		textArea_1.append("Research finished \n");
		jButton.setEnabled(true);
	}

	/**
	 * Met à jour la zone de progression.
	 * 
	 * @param message
	 *            le message
	 * @param cur
	 *            la position du curseur de progression
	 * @param total
	 *            la valeur maximale du curseur de progression
	 */
	public void setProgress(final String message, final int cur, final int total) {
		progressMessage.setText(message);
		progressBar_1.setMaximum(total);
		progressBar_1.setMinimum(0);
		progressBar_1.setValue(cur);
		progressBar_1.repaint();
	}

	private void hadoop(String[] args, String extractorClassName,
			List<String> filterList) {
		int resultNumber = 0;

		FileSystem fs = null;
		try {
			fs = FileSystem.get(new Configuration());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (args[0].equals("index")) {
			Path output1 = new Path("/output/invertedindex");
			Path output2 = new Path("/output/cosine");
			try {
				fs.delete(output1, true);
				fs.delete(output2, true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		/* create the string represent the filter and extractor to use */
		List<String> buf = new ArrayList<String>();
		for (String s : filterList)
			buf.add(s);
		String filters = "(" + StringUtils.join(",", buf) + ")";

		/**
		 * The job is indexing all file
		 */
		if (args[0].compareTo("index") == 0) {
			setProgress("Making index...", 3, 4);
			String[] arg = { args[2], args[3] };
			try {
				ToolRunner.run(new Configuration(), new FirstJob(
						extractorClassName, filters), arg);
				setProgress("Indexation finished", 4, 4);
			} catch (Exception e) {
				e.printStackTrace();
			}

			/**
			 * The job is answer to a question
			 */
		} else if (args[0].compareTo("question") == 0) {

			String[] param = extractorClassName.split("-");
			SignExtractor extract = null;
			if (param.length == 2) {
				extractorClassName = param[0];
				Class<?> clazz;
				try {
					clazz = Class.forName(extractorClassName);
					extract = (SignExtractor) clazz.getConstructor(int.class)
							.newInstance(param[1]);
					setProgress("Exactor loaded", 3, 4);
				} catch (Exception e) {
					System.out
							.println("Unable to load extractor, please check configuration file");
				}
			} else {
				try {
					extract = (SignExtractor) Class.forName(extractorClassName)
							.newInstance();
				} catch (Exception e) {
					System.out.println("Unable to load extractor \""
							+ extractorClassName
							+ "\", please check configuration file");
				}
			}

			ArrayList<Filter> fil = new ArrayList<Filter>();
			for (String className : filterList) {
				try {
					fil.add((Filter) Class.forName(className).newInstance());
				} catch (Exception e) {
					System.out.println("Unable to load filter \"" + className
							+ "\", please check configuration file");
				}
			}
			/* getting the question tf */
			String queryQuestion = args[4];
			HashMap<String, Double> mapQuestion = new HashMap<String, Double>();
			extract.setContent(queryQuestion);
			String sign = extract.nextToken();
			while (sign != null) {
				String signfiltered = "";
				if (fil.size() == 0) {
					signfiltered = sign;
				} else {
					String res = sign;
					for (int i = 0; i < fil.size(); ++i) {
						res = fil.get(i).filter(res);
						if (res == null) {
							break;
						}
					}
					signfiltered = res;
				}
				if (signfiltered.compareTo("") != 0) {
					if (mapQuestion.containsKey(signfiltered))
						mapQuestion.put(signfiltered,
								mapQuestion.get(signfiltered) + 1);
					else
						mapQuestion.put(signfiltered, (double) 1);
				}
				sign = extract.nextToken();
			}

			List<String> item = new ArrayList<String>();
			for (Entry<String, Double> e : mapQuestion.entrySet()) {
				item.add(e.getKey() + "->" + e.getValue());
			}

			String question = StringUtils.join(",", item);

			String[] arg = { args[2], args[3] };
			try {
				ToolRunner.run(new Configuration(), new SecondJob(question),
						arg);
				setProgress("Research finished", 4, 4);
				System.out
						.println("------------------------result--------------------");
				BufferedReader buffer = null;
				try {
					FSDataInputStream result = fs.open(new Path(args[3]
							+ "/part-r-00000"));
					String line;
					buffer = new BufferedReader(new InputStreamReader(result));
					while ((line = buffer.readLine()) != null) {
						String[] str = line.split("\\t");
						if (str.length >= 2
								&& Double.parseDouble(str[1]) != 0.0) {
							resultNumber++;
							System.out.println(line);
						}
					}
					System.out
							.println("I found " + resultNumber + " documents");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (buffer != null)
						buffer.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			System.out.println("Parameter error");

		}
	}

	private static byte[] toByteArray(char[] array, Charset charset) {
		CharBuffer cbuf = CharBuffer.wrap(array);
		ByteBuffer bbuf = charset.encode(cbuf);
		return bbuf.array();
	}
}
