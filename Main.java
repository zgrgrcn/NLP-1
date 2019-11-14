import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
//   
import java.awt.event.*;
public class Main {
  static String ngramMethode = null;

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

  //read all file and return all of as a single string
  public static String readFileAsString(String fileName) throws IOException {
    String data = "";
    data = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);// for turkish letters
    return data;
  }

  public static void main(String[] args) throws IOException {
    GUI gui = new GUI();
    // select novel

    // select Unigram-Bigrams-Trigrams from combo box

    gui.b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ngramMethode = (String) gui.cb.getItemAt(gui.cb.getSelectedIndex());
        //gui.area.append(ngramMethode + gui.num + "\n");
        //gui.pb.setValue(++gui.num);
      }
    });
    String paths[] = { "./novels/BİLİM İŞ BAŞINDA.txt", "./novels/UNUTULMUŞ DİYARLAR.txt", "./novels/BOZKIRDA.txt",
        "./novels/DENEMELER.txt", "./novels/DEĞİŞİM.txt" };
    for (String path : paths) {// for each novel
      System.out.println(" * * * " + path.substring(9) + " * * * ");
      String content = readFileAsString(path);
      for (int n = 1; n <= 3; n++) {// for each n gram (1-2-3)
        List<ngram> ngrams2 = new ArrayList<ngram>();
        for (String ngram : ngrams(n, content)) {
          boolean founded = false;
          for (int k = 0; k < ngrams2.size(); k++) {
            ngram s = ngrams2.get(k);
            if (ngram.equals(s.ngram)) {
              founded = true;
              s.count++;
            }
          }
          if (!founded)
            ngrams2.add(new ngram(1, ngram));
        }
        System.out.println(" ");
        ngrams2.sort(Comparator.comparing(ngram::getCount));// kucukten buyuge
        // REF:
        // https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
        for (int i = ngrams2.size() - 1; i > ngrams2.size() - 3; i--) {
          ngram s = ngrams2.get(i);
          System.out.println(s.ngram.replaceAll("\r\n", "") + ";" + s.count);
          gui.area.append(s.ngram.replaceAll("\r\n", "") + ";" + s.count+ "\n");
          gui.pb.setValue(++gui.num);
        }
      }
    }

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

class GUI implements ActionListener{
  JFrame f;
  int num = 0;
  JButton b,b1,b2,b3,b4,b5;
  JProgressBar pb;
  JTextArea area;
  JComboBox cb;

  GUI() {
    f = new JFrame();// creating instance of JFrame

    // files `Button`
    b1 = new JButton("Bilim is basinda");// creating instance of JButton
    b2 = new JButton("Unutulmus diyarlar");
    b3 = new JButton("Bozkirda");
    b4 = new JButton("Denemeler");
    b5 = new JButton("Degisim");
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
    pb = new JProgressBar(0, 100);
    pb.setBounds(40, 40, 160, 30);
    pb.setValue(0);
    pb.setStringPainted(true);
    f.add(pb);
    pb.setBounds(350, 100, 445, 15);

    // `text area`
    area = new JTextArea();
    area.setBounds(100, 150, 200, 600);
    f.add(area);

    // show `Button`
    b = new JButton("Show");
    b.setBounds(200, 100, 75, 20);
    f.add(b);

    f.setSize(850, 800);// 400 width and 500 height
    f.setLayout(null);// using no layout managers
    f.setVisible(true);// making the frame visible
  }

  public void actionPerformed(ActionEvent e) {
    String Ngram = (String) cb.getItemAt(cb.getSelectedIndex());
    area.append(Ngram + num + "\n");
    area.append(e.toString()+"\n");
    pb.setValue(++num);
  }

}