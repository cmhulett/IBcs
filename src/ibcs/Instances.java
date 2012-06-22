
package ibcs;

import java.util.ArrayList;

/**
 *
 * @author chulett
 */
public class Instances extends ArrayList{
    private int nbrColumns = 0;
    private int classifyAttribute = 0;
    private ArrayList columnNames = new ArrayList();
    private ArrayList columnTypes = new ArrayList();

    //min and max attribute values for normalization
    public ArrayList min = new ArrayList();
    public ArrayList max = new ArrayList();

    public int getNbrInstances(){
        return this.size();
    }

    public void setNbrColumns(int nbrColumns){
        this.nbrColumns = nbrColumns;
        min = new ArrayList();
        max = new ArrayList();
    }

    public int getNbrColumns(){
        return this.nbrColumns;
    }

    public ArrayList getInstance(int index){
        return (ArrayList) this.get(index);
    }

    public double[] getDInstance(int index){
        return (double[]) this.get(index);
    }

    public void removeAttribute(int index){
        //removes attribute at index from all instances
        if(index == 0){
            for(int i=0;i<this.getNbrInstances();i++){
                ArrayList instance = new ArrayList();
                for(int j=0;j<nbrColumns - 1;j++){
                    instance.add(j, this.getInstance(i).get(j+1));
                }
                this.remove(i);
                this.add(i,instance);
            }
        }
        else if(index == nbrColumns){
            for(int i=0;i<this.getNbrInstances();i++){
                ArrayList instance = new ArrayList();
                for(int j=0;j<nbrColumns - 1;j++){
                    instance.add(j, this.getInstance(i).get(j));
                }
                this.remove(i);
                this.add(i,instance);
            }
        }
        else{
            for(int i=0;i<this.getNbrInstances();i++){
                ArrayList instance = new ArrayList();
                for(int j=0;j<index;j++){
                    instance.add(j, this.getInstance(i).get(j));
                }
                for(int j=index;j<nbrColumns -1;j++){
                   instance.add(j, this.getInstance(i).get(j+1));
                }
                this.remove(i);
                this.add(i,instance);
            }
        }
        columnNames.remove(index);
        this.nbrColumns -= 1;
        min = new ArrayList();
        max = new ArrayList();
    }

    public void removeInstance(ArrayList instance){
        this.remove(instance);
    }

    public void removeDInstance(double[] instance){
        this.remove(instance);
    }

    public void removeInstance(int index){
        this.remove(index);
    }

    public void ouputD(){
        for(int i=0;i<this.size();i++){
            String sRecord = "";
            for(int j=0;j<this.getNbrColumns();j++){
                sRecord = sRecord + " | " + Double.toString(this.getDInstance(i)[j]);
            }
            System.out.println(sRecord);
        }
    }
    
    public void setClassifyAttribute(int index){
        classifyAttribute = index;
    }

    public int getClassifyAttribute(){
        return classifyAttribute;
    }

    public String getSClassifyAttribute(){
        String name = (String) this.columnNames.get(classifyAttribute) + " [" +
                (String) this.columnTypes.get(classifyAttribute)+"]";
        return name;
    }

    public void setColumnNames(ArrayList columnNames){
        this.columnNames = columnNames;
    }

    public void setColumnTypes(ArrayList columnTypes){
        this.columnTypes = columnTypes;
    }

    public ArrayList getColumnTypes(){
        return this.columnTypes;
    }

    public boolean equalByValue(ArrayList inst1, ArrayList inst2){
        //compare size and each element against each other
        if( inst1.size() != inst2.size() ){
            return false;
        }
        for(int i=0;i<inst1.size();i++){
            if( !inst1.get(i).equals(inst2.get(i)) ){
                return false;
            }
        }

        return true;
    }

}