package ibcs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;

/**
 *
 * @author chulett
 */
public class Main {

    public static void main(String[] args) {
        String eol = System.getProperty("line.separator");
        DecimalFormat df = new DecimalFormat("#.####");

        //kValues used for selecting IBk and it's k value or IBcs if 101
        int[] kValues = new int[] {1,5,10,101};
        for(int i=1;i<2;i++){
            //int limit = 10000*i;
            //constructor connects to the db, dermatology is a db on the localhost from the UCI ML repo
            DB database = new DB("root","", "jdbc:mysql://localhost:3306/dermatology","com.mysql.jdbc.Driver");

            String query = "SELECT * FROM derm";

            //data: ArrayList of int arrays, Training Set
            Instances data = new Instances();

            //selectRecords populates instances with data from the database
            if( database.selectRecords(query,data) ){
                System.out.println("Number of columns: " + Integer.toString(data.getNbrColumns()));
                System.out.println("Number of instances: " + Integer.toString(data.getNbrInstances()));
            }
            else { System.out.println("Could not retrive data"); }
            database.close();

            //pre-process data
            //removes attribute at index i, index 0 auto-increment generated IDs
            //data.removeAttribute(0);

            //set the classification attribute
            data.setClassifyAttribute(34);

            System.out.println("Classifying for attribute: " + data.getSClassifyAttribute());

            for(int j=0;j<kValues.length;j++){
                if(kValues[j] != 101){
                    IBk IB = new IBk(kValues[j]);
                    Evaluation test = new Evaluation();
                    int folds = 10;
                    double accuracy = test.crossValidateIBk(IB, data, folds);
                    //double accuracy = test.percentageSplitIBk(IB, data, 0.8);

                    try{
                        FileWriter fstream = new FileWriter("output_Dermatology",true);
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(accuracy + eol);
                        out.close();
                    }
                    catch(Exception e){
                         System.err.println("Error: " + e.getMessage());
                    }
                }
                else{
                    IBcs IB = new IBcs();
                    Evaluation test = new Evaluation();
                    int folds = 10;
                    double accuracy = test.crossValidateIBcs(IB, data, folds);

                    try{
                        FileWriter fstream = new FileWriter("output_Dermatology",true);
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(accuracy + eol);
                        out.close();
                    }
                    catch(Exception e){
                         System.err.println("Error: " + e.getMessage());
                    }
                    
                    
                }
            }
        }
    }

}