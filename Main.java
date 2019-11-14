import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.lang.model.util.ElementScanner6;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class Main {
  // REF:
  // https://github.com/hackjutsu/n-gram-demo/blob/master/src/main/java/Ngram.java
  public static List<String> ngrams(int n, String _content) {
    List<String> ngrams = new ArrayList<String>();

    String[] words = _content.replaceAll("  ", " ").split(" ");
    for (int i = 0; i < words.length - n + 1; i++)
      ngrams.add(concat(words, i, i + n));
    return ngrams;
  }

  // n tane array elemanini birlestiriyor ornegin words[x]+words[x+1]+words[x+2]
  public static String concat(String[] words, int start, int end) {
    StringBuilder sb = new StringBuilder();
    for (int i = start; i < end; i++)
      sb.append((i > start ? " " : "") + words[i]);
    return sb.toString();
  }

  // read all file and return all of as a single string
  public static String readFileAsString(String fileName) throws IOException {
    String data = "";
    data = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);// for turkish letters
    return data;
  }

  public static void main(String[] args) throws IOException {
    GUI gui = new GUI();

    gui.b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // select novel
        String novelPath = gui.novelPath;
        // select "Unigram", "Bigrams", "Trigrams"
        int ngramMethode = gui.ngramMethode;
        if (novelPath != null && ngramMethode != -1) {
          String path = "./novels/" + novelPath + ".txt";

          // update label and reset area and bar
          gui.L.setText("Path: " + path + " and ngram methode:" + ngramMethode + "-" + gui.ngramMethodeAString);
          gui.area.setText(" * * * " + novelPath + " * * * " + "\n");
          gui.num = 0;
          gui.pb.setValue(gui.num);

          String content = "";
          try {
            content = readFileAsString(path);
          } catch (IOException e1) {
            e1.printStackTrace();
          }

          List<ngram> ngramList = new ArrayList<ngram>();
          for (String ngram : ngrams(ngramMethode, content)) {
            boolean founded = false;
            for (int k = 0; k < ngramList.size(); k++) {
              ngram s = ngramList.get(k);
              if (ngram.equals(s.ngram)) {
                founded = true;
                s.count++;
              }
              
            }
            gui.pb.setValue(++gui.num);
            if (!founded)
              ngramList.add(new ngram(1, ngram));
          }
          System.out.println(" ");
          ngramList.sort(Comparator.comparing(ngram::getCount));// kucukten buyuge
          // REF:
          // https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
          for (int i = ngramList.size() - 1; i > ngramList.size() - 35; i--) {
            ngram s = ngramList.get(i);
            System.out.println(s.ngram.replaceAll("\r\n", "") + ";" + s.count);
            gui.area.append(s.ngram.replaceAll("\r\n", "") + ";" + s.count + "\n");

          }

        } else
          gui.L.setText("please select path and ngram methode" + novelPath + ngramMethode);
      }
    });

  }
}

class ngram {
  int count;
  String ngram;

  public ngram(int _count, String _ngram) {
    this.count = _count;
    this.ngram = _ngram;
  }

  public int getCount() {
    return count;
  }
}

class GUI implements ActionListener {
  JFrame f;
  int num = 0;
  JButton b, b1, b2, b3, b4, b5;
  JProgressBar pb;
  JTextArea area;
  JComboBox cb;
  JLabel L;

  int ngramMethode = -1;
  String ngramMethodeAString = null;
  String novelPath = null;

  GUI() {
    f = new JFrame();// creating instance of JFrame

    // files `Button`
    b1 = new JButton("BİLİM İŞ BAŞINDA");// creating instance of JButton
    b2 = new JButton("UNUTULMUŞ DİYARLAR");
    b3 = new JButton("BOZKIRDA");
    b4 = new JButton("DENEMELER");
    b5 = new JButton("DEĞİŞİM");
    b1.setBounds(50, 50, 150, 40);// x axis, y axis, width, height
    b2.setBounds(200, 50, 150, 40);
    b3.setBounds(350, 50, 150, 40);
    b4.setBounds(500, 50, 150, 40);
    b5.setBounds(650, 50, 150, 40);
    f.add(b1);// adding button in JFrame
    f.add(b2);
    f.add(b3);
    f.add(b4);
    f.add(b5);
    b1.addActionListener(this);
    b2.addActionListener(this);
    b3.addActionListener(this);
    b4.addActionListener(this);
    b5.addActionListener(this);

    // Ngrams types `Combo box`
    String Ngrams[] = { "Unigram", "Bigrams", "Trigrams" };
    cb = new JComboBox(Ngrams);
    cb.setBounds(50, 100, 150, 20);
    f.add(cb);
    f.setLayout(null);

    // `Progress Bar`
    pb = new JProgressBar(0, 1000);
    pb.setValue(0);
    pb.setStringPainted(true);
    f.add(pb);
    pb.setBounds(350, 100, 445, 15);

    // `text area`
    area = new JTextArea();
    area.setBounds(100, 150, 500, 600);
    f.add(area);

    // show `Button`
    b = new JButton("Show");
    b.setBounds(200, 100, 75, 20);
    f.add(b);
    cb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ngramMethodeAString = (String) cb.getItemAt(cb.getSelectedIndex());

        if (ngramMethodeAString == "Unigram")
          ngramMethode = 1;
        else if (ngramMethodeAString == "Bigrams")
          ngramMethode = 2;
        else
          ngramMethode = 3;
        // area.append(ngramMethode + num + "\n");
        // pb.setValue(++num);
      }
    });

    // `label` for general use
    L = new JLabel();
    L.setBounds(100, 20, 500, 15);
    f.add(L);

    f.setSize(850, 800);// 400 width and 500 height
    f.setLayout(null);// using no layout managers
    f.setVisible(true);// making the frame visible
  }

  // novel selector
  public void actionPerformed(ActionEvent e) {
    // area.append(e.getActionCommand().toString() + "\n");
    novelPath = e.getActionCommand().toString();
  }

}
