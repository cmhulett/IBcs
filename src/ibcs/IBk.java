package ibcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chulett
 */
public class IBk<E> {
    //CD: extends Instances and contains similarity function
    ConceptDescription CD = new ConceptDescription();
    public int k = 1;

    public IBk(int k){
        this.k = k;
    }

    public IBk(){
        //no argument- k defaults to 1
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
    //instanceIndex - the index in CD that corresponds to instance1, used
    //to keep from classifying instances against themselves
    //classifyAttribute- the attribute being classified
    public E classify(ArrayList instance1, int classifyAttribute){
        //for each instance in CD find the closest instances across all
        //attributes not being classified and return the value of the attribute
        //being classified from the nearest neighbors. get classification
        //value from a majority rule
        E value = null;
        ArrayList<E> values = new ArrayList<E>();
        ArrayList<Double> distances = CD.Similarity(instance1, classifyAttribute);
        for(int j=0;j<k;j++){
            int minDistancesIndex = this.getLowestDistance(distances);
            values.add(j, (E) CD.getInstance(minDistancesIndex).get(classifyAttribute));
            distances.remove(minDistancesIndex);
            distances.add(minDistancesIndex, null);
        }

        //get most common value (mode) in values[]
        value = this.getMode(values);

        return ( value );
    }

    private E getMode(ArrayList<E> values) {
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

      return ( mode );
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

}
