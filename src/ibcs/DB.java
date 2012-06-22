package ibcs;

import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author chulett
 */
public class DB {
    private Connection conn = null;
    private String query = null;
    private int nbrColumns = 0;
    public ArrayList colTypes = new ArrayList();
    public ArrayList columnNames = new ArrayList();

    public DB(String user, String pass, String url, String driver){
        try {
            Class.forName (driver).newInstance();
            conn = DriverManager.getConnection (url,user,pass);
            System.out.println ("Database connection established");
        }
        catch (Exception e)
           {
               System.err.println ("Cannot connect to database server");
           }

    }

    public boolean selectRecords(String query, Instances instances){
        this.query = query;
        try{
            Statement s = conn.createStatement();
            s.executeQuery (this.query);
            ResultSet rs = s.getResultSet();
            columnNames = this.getColumnNames(rs);
            instances.setColumnNames(columnNames);
            instances.setNbrColumns(nbrColumns);
            ArrayList columnTypes = this.getColumnTypes(rs);
            instances.setColumnTypes(columnTypes);
            int j =0;
            //loop through all instances (records) in the table
            while ( rs.next() ){
                //loop through column names for each attribute in the instance
                ArrayList instance = new ArrayList();
                for(int i=0;i<columnNames.size();i++){
                    //rs.get<Type>() requires name of column
                    if(colTypes.get(i) == "INT"){
                        instance.add(i, rs.getInt((String) columnNames.get(i)));
                    }
                    if(colTypes.get(i) == "VARCHAR"){
                        instance.add(i, rs.getString((String) columnNames.get(i)));
                    }
                    if(colTypes.get(i) == "DOUBLE"){
                        instance.add(i, rs.getDouble((String) columnNames.get(i)));
                    }

                    //store min and max values across each numeric column
                    //1st loop initialize min and max values
                    if(colTypes.get(i) == "INT"){
                        if( j == 0){
                            instances.max.add(i, (Integer) instance.get(i));
                            instances.min.add(i, (Integer) instance.get(i));
                        }
                        if((Integer) instances.max.get(i) < (Integer) instance.get(i)){
                            instances.max.remove(i);
                            instances.max.add(i, (Integer) instance.get(i));
                        }
                        if((Integer) instances.min.get(i) > (Integer) instance.get(i)){
                            instances.min.remove(i);
                            instances.min.add(i, (Integer) instance.get(i));
                        }
                    }

                    if(colTypes.get(i) == "DOUBLE"){
                        if( j == 0){
                        instances.max.add(i, (Double) instance.get(i));
                        instances.min.add(i, (Double) instance.get(i));
                        }
                        if((Double) instances.max.get(i) <(Double) instance.get(i)){
                            instances.max.remove(i);
                            instances.max.add(i, (Double) instance.get(i));
                        }
                        if((Double) instances.min.get(i) >(Double) instance.get(i)){
                            instances.min.remove(i);
                            instances.min.add(i, (Double) instance.get(i));
                        }
                    }
                    if(colTypes.get(i) == "VARCHAR"){
                        if(j == 0){
                            instances.max.add(i, 0);
                            instances.min.add(i, 0);
                        }
                    }


                }
                instances.add(instance);
                j++;
            }
            rs.close();
            s.close();
        }
        catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    private ArrayList getColumnNames(ResultSet rs){
        ArrayList colNames = new ArrayList();
        if (rs == null) {
            System.out.println("No records in result set");
            return colNames;
        }
        try{
            ResultSetMetaData rsMetaData = rs.getMetaData();
            nbrColumns = rsMetaData.getColumnCount();

            // get the column names; column indexes start from 1
            for (int i = 1; i < nbrColumns + 1; i++) {
                colNames.add(rsMetaData.getColumnName(i));
            }
            System.out.println("Column names: " + colNames.toString());
            return colNames;
        }
        catch(Exception e){
            System.out.println("Couldn't retrieve column names");
            return colNames;
        }

    }

    private ArrayList getColumnTypes(ResultSet rs){
        if (rs == null) {
            System.out.println("No records in result set");
            return colTypes;
        }
        try{
            ResultSetMetaData rsMetaData = rs.getMetaData();

            // get the column types; column indexes start from 1
            for (int i = 1; i < nbrColumns + 1; i++) {
                colTypes.add(rsMetaData.getColumnTypeName(i));
            }
            System.out.println("Column Types: " + colTypes.toString());
            return colTypes;
        }
        catch(Exception e){
            System.out.println("Couldn't retrieve column types");
            return colTypes;
        }

    }

    public void close(){
        if (conn != null){
                   try{
                       conn.close ();
                       System.out.println ("Database connection terminated");
                   }
                   catch (Exception e) { /* ignore close errors */ }
               }
    }
}
