
package ibcs;

import java.util.ArrayList;

/**
 *
 * @author chulett
 */
public class ConceptDescription extends Instances{

    public ArrayList<Double> Similarity(ArrayList instance1, int classifyAttribute){
        //calculate the similarity between an instance
        //and all other instances in the CD

        //store similarities to pass back to the caller
        double[][] similarity = new double[this.getNbrInstances()][instance1.size()];
        for(int Column=0;Column<instance1.size();Column++){
                if(Column != classifyAttribute){
                if(this.getColumnTypes().get(Column) == "INT"){
                    for(int i=0;i<this.getNbrInstances();i++){
                        ArrayList instance2 = this.getInstance(i);
                        //compute similarity for att[Column] in inst2 for each att in instance1
                        //int max = (Integer)this.max.get(Column);
                        //int min = (Integer)this.min.get(Column);
                        double diff =(Integer) this.max.get(Column) -(Integer) this.min.get(Column);
                        if( diff != 0){
                            similarity[i][Column] = Math.pow( (((Integer) instance1.get(Column) - (Integer) this.min.get(Column)) / diff)
                                - (((Integer) instance2.get(Column) - (Integer) this.min.get(Column)) / diff), 2 );
                        }
                        else{
                            similarity[i][Column] = 0.0;
                        }

                    }
                }
                if(this.getColumnTypes().get(Column) == "DOUBLE"){
                    for(int i=0;i<this.getNbrInstances();i++){
                        ArrayList instance2 = this.getInstance(i);
                        //compute similarity for att[Column] in inst2 for each att in instance1
                        double diff =(Double) this.max.get(Column) -(Double) this.min.get(Column);
                        if( diff != 0){
                            similarity[i][Column] = Math.pow( (((Double) instance1.get(Column) - (Double) this.min.get(Column)) / diff)
                                - (((Double) instance2.get(Column) - (Double) this.min.get(Column)) / diff), 2 );
                        }
                        else{
                            similarity[i][Column] = 0.0;
                        }

                    }
                }
                if(this.getColumnTypes().get(Column) == "VARCHAR"){
                    for(int i=0;i<this.getNbrInstances();i++){
                        ArrayList instance2 = this.getInstance(i);
                        if( instance1.get(Column).equals(instance2.get(Column))){
                            similarity[i][Column] = 0.0;
                        }
                        else{
                            similarity[i][Column] = 1.0;
                        }
                    }
                }
                
            }
        }

        //loop through each row of similarities to calculate their sum
        //to use for finding minimum distances
        ArrayList<Double> distances = new ArrayList<Double>();
        for(int i=0;i<this.getNbrInstances();i++){
            double sum = 0.0;
            //for(int j=0;j<this.getNbrColumns()-1;j++){
            for(int j=0;j<this.getNbrColumns();j++){
                sum +=  similarity[i][j];
            }
            sum = Math.pow(sum, 0.5);
            distances.add(i,sum);
        }


        return ( distances );

    }

    public ArrayList<Double> Similarity(ArrayList instance1, int classifyAttribute, Instances CoreSet){
        //calculate the similarity between an instance
        //and all other instances in the Core Set

        //store similarities to pass back to the caller
        double[][] similarity = new double[CoreSet.getNbrInstances()][instance1.size()];
        for(int Column=0;Column<instance1.size();Column++){
                if(Column != classifyAttribute){
                if(this.getColumnTypes().get(Column) == "INT"){
                    for(int i=0;i<CoreSet.getNbrInstances();i++){
                        ArrayList instance2 = CoreSet.getInstance(i);
                        //compute similarity for att[Column] in inst2 for each att in instance1
                        double diff =(Integer) this.max.get(Column) -(Integer) this.min.get(Column);
                        if( diff != 0){
                            similarity[i][Column] = Math.pow( (((Integer) instance1.get(Column) - (Integer) this.min.get(Column)) / diff)
                                - (((Integer) instance2.get(Column) - (Integer) this.min.get(Column)) / diff), 2 );
                        }
                        else{
                            similarity[i][Column] = 0.0;
                        }

                    }
                }
                if(this.getColumnTypes().get(Column) == "DOUBLE"){
                    for(int i=0;i<CoreSet.getNbrInstances();i++){
                        ArrayList instance2 = CoreSet.getInstance(i);
                        //compute similarity for att[Column] in inst2 for each att in instance1
                        double diff =(Double) this.max.get(Column) -(Double) this.min.get(Column);
                        if( diff != 0){
                            similarity[i][Column] = Math.pow( (((Double) instance1.get(Column) - (Double) this.min.get(Column)) / diff)
                                - (((Double) instance2.get(Column) - (Double) this.min.get(Column)) / diff), 2 );
                        }
                        else{
                            similarity[i][Column] = 0.0;
                        }

                    }
                }
                if(this.getColumnTypes().get(Column) == "VARCHAR"){
                    for(int i=0;i<CoreSet.getNbrInstances();i++){
                        ArrayList instance2 = CoreSet.getInstance(i);
                        if( instance1.get(Column).equals(instance2.get(Column))){
                            similarity[i][Column] = 0.0;
                        }
                        else{
                            similarity[i][Column] = 1.0;
                        }
                    }
                }

            }
        }

        //loop through each row of similarities to calculate their sum
        //to use for finding minimum distances
        ArrayList<Double> distances = new ArrayList<Double>();
        for(int i=0;i<CoreSet.getNbrInstances();i++){
            double sum = 0.0;
            for(int j=0;j<instance1.size()-1;j++){
                sum +=  similarity[i][j];
            }
            sum = Math.pow(sum, 0.5);
            distances.add(i,sum);
        }


        return ( distances );

    }
}
