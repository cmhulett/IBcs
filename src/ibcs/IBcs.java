
package ibcs;

import java.io.BufferedWriter;
import java.io.FileWriter;
 import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chulett
 */
public class IBcs<E> {
    //CD: extends Instances and contains similarity function
    ConceptDescription CD = new ConceptDescription();
    public double adjRegion = 0.0;
    public double initRadius = 0.0;

    public IBcs(double adjRegion, double Radius){
        this.adjRegion = adjRegion;
        this.initRadius = Radius;
    }

    public IBcs(){
    }

    //data- set of instances to be trained
    public void train(Instances data){
        CD = new ConceptDescription();
        CD.setNbrColumns(data.getNbrColumns());
        CD.setColumnTypes(data.getColumnTypes());
        CD.max = (ArrayList) data.max.clone();
        CD.min = (ArrayList) data.min.clone();

        CD.addAll(data);
    }

    //instance1 - the single instance from the data to be classified
    //classifyAttribute- the attribute being classified
    public E classify(ArrayList instance1, int classifyAttribute){
        Instances CoreSet = new Instances();
        ArrayList<Double> distances = CD.Similarity(instance1, classifyAttribute);
        //this.writeDistances(distances, "distances");
        double Radius = initRadius;
        for(int j=0;j<distances.size();j++){

            int distancesIndex = this.getLowestDistance(distances);
            double minDistance = distances.get(distancesIndex);
            //System.out.println("Core Set size: " + CoreSet.size());
            if( CoreSet.isEmpty() ){
                CoreSet.add(CD.getInstance(distancesIndex));
                Radius = distances.get(distancesIndex);
                adjRegion = Radius;
                //replace min distance with null to not use same instance twice
                distances.remove(distancesIndex);
                distances.add(distancesIndex, null);
            }
            else if( isInRadius(distances.get(distancesIndex), Radius) ){
                CoreSet.add(CD.getInstance(distancesIndex));
                distances.remove(distancesIndex);
                distances.add(distancesIndex, null);
            }
            else if( isSaturatedInU(CoreSet, CD.getInstance(distancesIndex),
                     adjRegion, classifyAttribute) ){
                CoreSet.add(CD.getInstance(distancesIndex));
                //expand the Core Set Radius to the new instance
                //added to the Core Set
                if(distances.get(distancesIndex) > Radius){
                    Radius = distances.get(distancesIndex);
                }
                distances.remove(distancesIndex);
                distances.add(distancesIndex, null);
                //reduce the adjacent Region to drop off as the Core Set expands
                adjRegion = Math.log(1 + adjRegion);
            }
            else{
                j = distances.size();
            }

            if( CoreSet.size() == CD.size()){
                System.out.println("Core Set size: " + CoreSet.getNbrInstances());
            }
        }

        if( CoreSet.size() > 1 ){
            //System.out.println("Core Set size: " + CoreSet.getNbrInstances());
        }

        //find majority rule classify attribute value from instance1's
        //Core Set of instances        
        E value;
        ArrayList<E> values = new ArrayList<E>();

        for(int i=0;i<CoreSet.getNbrInstances();i++){
            values.add(i, (E) CoreSet.getInstance(i).get(classifyAttribute));
        }


        value = this.getMode(values);
        return (E) value;
    }

    private boolean isInRadius(double currentDistance, double Radius){
        if( currentDistance < Radius ){
            return true;
        }
        return false;
    }

    //determines if the instance at the currentMinDistance is saturated in
    //point set U of the instance being classified
    private boolean isSaturatedInU(Instances CoreSet,
            ArrayList target, double adjRegion, int classifyAttribute){
        //check if the current instance is within the adjacent region of
        //any node added in the Core Set
        //Instances CoreSetLast = new Instances();
        //CoreSetLast.add(CoreSet.get(CoreSet.size()-1));
        //ArrayList<Double> targetDistances = CD.Similarity(target, classifyAttribute, CoreSetLast);
        ArrayList<Double> targetDistances = CD.Similarity(target, classifyAttribute, CoreSet);
        int targetDistanceMinIndex = this.getLowestDistance(targetDistances);
        if( targetDistances.get(targetDistanceMinIndex) < adjRegion ){
            return true;
        }
        return false;
    }

    private E getMode(ArrayList<E> values) {
        //getMode abstracted from java implementation presented by Chadwick
        // http://stackoverflow.com/questions/716496/get-mode-value-in-java
        HashMap<E,Integer> freqs = new HashMap<E,Integer>();

        for (E val : values) {
            Integer freq = freqs.get(val);
            freqs.put(val, (freq == null ? 1 : freq+1));
          }

          E mode = null;
          int maxFreq = 0;

          for (Map.Entry<E,Integer> entry : freqs.entrySet()) {
            int freq = entry.getValue();
            if (freq > maxFreq) {
              maxFreq = freq;
              mode = entry.getKey();
            }
          }

          return (E) mode;
    }

    private int getLowestDistance(ArrayList<Double> distances){
            //get node with lowest distance to source
            int distancesIndex = 0;
                while(distances.get(distancesIndex) == null){
                    distancesIndex++;
            }
            for(int i=0;i<distances.size();i++){
                if ( distances.get(i) != null ){
                      if ( distances.get(i) < distances.get(distancesIndex) ){
                        distancesIndex = i;
                    }
                }
            }
            return distancesIndex;
    }

    private void writeDistances(ArrayList<Double> distances, String filename){
        ArrayList<Double> orderedDistances = new ArrayList();
        ArrayList<Double> distancesCopy = (ArrayList)distances.clone();
        int orderedDistancesIndex;
        String eol = System.getProperty("line.separator");
        for(int i=0;i<CD.size();i++){
            orderedDistancesIndex = this.getLowestDistance(distancesCopy);
            orderedDistances.add(distancesCopy.get(orderedDistancesIndex));
            distancesCopy.remove(orderedDistancesIndex);
        }
        try{
            FileWriter fstream = new FileWriter(filename,true);
            BufferedWriter out = new BufferedWriter(fstream);

            for(int i=0;i<orderedDistances.size();i++){
                out.write(orderedDistances.get(i) + eol);
            }
            //Close the output stream
            out.close();
        }
        catch(Exception e){
             System.err.println("Error: " + e.getMessage());
        }
    }

}
