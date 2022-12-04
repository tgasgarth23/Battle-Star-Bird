import java.io.*;
//code inspired by https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
public class fileWriter {
    public void writer(String info) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        File file = new File("score.txt");

        //if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("New score.txt file created");
        }
        try {
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            pw.println(info);
            System.out.println("Scores added into file");
            pw.flush();
        } finally {
            try {//close files because we can't perform other actions to file if it is always open
                pw.close();
                bw.close();
                fw.close();
            } catch (IOException io) {
            }
        }
    }
}
