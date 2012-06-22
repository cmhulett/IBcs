package ibcs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author chulett
 */
public class Evaluation<E> {
    
    public double crossValidateIBk(IBk IB, Instances data, int folds){
        Instances dataCopy = (Instances) data.clone();
        String eol = System.getProperty("line.separator");
        String formattedResults = "Results of " + Integer.toString(folds) + "-fold cross-validation" +
                " testing for IBk with k=" +IB.k + ": " + eol +
                "=============================================================="+eol;
        String meanAcc = "";
        ArrayList<E> actualValues = new ArrayList<E>();
        ArrayList<E> predictedValues = new ArrayList<E>();
        DecimalFormat df = new DecimalFormat("#.##");
        //divide data up into k subsets
        ArrayList<Instances> kSubset = new ArrayList();
        Instances trainSet = new Instances();
        Instances testSet = new Instances();
        double[] accuracies = new double[folds];
        int misclassified = 0;
        int max = dataCopy.getNbrInstances();
        max = max/folds;
        for(int i=0;i<folds;i++){
            Instances current = new Instances();
            if(i != folds-1){
                for(int j=max;j>0;j--){
                    current.add(dataCopy.getInstance(j));
                    dataCopy.removeInstance(j);
                }
            }
            else{
                current = (Instances) dataCopy.clone();
                dataCopy.clear();
            }
            kSubset.add(current);
        }
        //for each fold train off folds-1 and validate off the remaining subset
        //with each subset used for validation only once
        int[] misclass = new int[folds];
        for(int i=0;i<folds;i++){
            //select train set
            trainSet.clear();
            for(int j=0;j<folds;j++){
                if(j != i){
                    trainSet.addAll(kSubset.get(j));
                }
            }

            trainSet.setNbrColumns(dataCopy.getNbrColumns());
            trainSet.setColumnTypes(dataCopy.getColumnTypes());
            trainSet.max = (ArrayList) dataCopy.max.clone();
            trainSet.min = (ArrayList) dataCopy.min.clone();
            IB.train(trainSet);
            
            //select test set
            testSet.clear();
            testSet.addAll(kSubset.get(i));
            
            misclassified = 0;
            predictedValues.clear();
            actualValues.clear();
            for(int j=0;j<testSet.getNbrInstances();j++){
                E predictedValue = (E)IB.classify(testSet.getInstance(j), dataCopy.getClassifyAttribute());
                predictedValues.add(predictedValue);
                E actualValue = (E)testSet.getInstance(j).get(dataCopy.getClassifyAttribute());
                actualValues.add(actualValue);

                if(!predictedValue.equals(actualValue)){
                    misclassified++;
                }
            }

            if(i == 7 || i == 8){//folds 8 and 9
                //this.writeToFile(predictedValues, actualValues, "abaloneFold89IBk15");
            }

            //this.ROC(predictedValues, actualValues);
            misclass[i] = misclassified;
            accuracies[i] = (double) (testSet.getNbrInstances() - misclassified) / testSet.getNbrInstances();
            System.out.println(i+1 + " fold accuracy: " + accuracies[i]);
        }
        double meanAccuracy = 0.0;
        int meanSuccess = 0;
        for(int i=0;i<accuracies.length;i++){
            meanAccuracy += accuracies[i];
            meanSuccess += testSet.getNbrInstances() - misclass[i];
        }

        meanSuccess = meanSuccess/accuracies.length;
        meanAccuracy = meanAccuracy/accuracies.length;
        meanAcc = "Mean accuracy across " + folds +"-folds CV: " +
                df.format(meanAccuracy*100) + "% " + eol;

        formattedResults = formattedResults + meanAcc;
        System.out.println(formattedResults);

        return ( meanAccuracy );
    }


    public double crossValidateIBcs(IBcs IB, Instances data, int folds){
        Instances dataCopy =(Instances) data.clone();
        String eol = System.getProperty("line.separator");
        String formattedResults = "Results of " + Integer.toString(folds) + "-fold cross-validation" +
                " testing for IBcs:" + eol +
                "=============================================================="+eol;
        String meanAcc = "";
        ArrayList<E> actualValues = new ArrayList<E>();
        ArrayList<E> predictedValues = new ArrayList<E>();
        DecimalFormat df = new DecimalFormat("#.##");
        //divide data up into k subsets
        ArrayList<Instances> kSubset = new ArrayList();
        Instances trainSet = new Instances();
        Instances testSet = new Instances();
        double[] accuracies = new double[folds];
        int misclassified = 0;
        int max = dataCopy.getNbrInstances();
        max = max/folds;
        for(int i=0;i<folds;i++){
            Instances current = new Instances();
            if(i != folds-1){
                for(int j=max;j>0;j--){
                    current.add(dataCopy.getInstance(j));
                    dataCopy.removeInstance(j);
                }
            }
            else{
                current = (Instances) dataCopy.clone();
                dataCopy.clear();
            }
            kSubset.add(current);
        }
        //for each fold train off folds-1 and validate off the remaining subset
        //with each subset used for validation only once
        int[] misclass = new int[folds];
        for(int i=0;i<folds;i++){            
            //select train set
            trainSet.clear();
            for(int j=0;j<folds;j++){
                if(j != i){
                    trainSet.addAll(kSubset.get(j));
                }
            }

            trainSet.setNbrColumns(dataCopy.getNbrColumns());
            trainSet.setColumnTypes(dataCopy.getColumnTypes());
            trainSet.max = (ArrayList) dataCopy.max.clone();
            trainSet.min = (ArrayList) dataCopy.min.clone();
            IB.train(trainSet);
            
            //select test set
            testSet.clear();
            testSet.addAll(kSubset.get(i));
            
            misclassified = 0;
            predictedValues.clear();
            actualValues.clear();
            for(int j=0;j<testSet.getNbrInstances();j++){
                E predictedValue = (E)IB.classify(testSet.getInstance(j), dataCopy.getClassifyAttribute());
                predictedValues.add(predictedValue);
                E actualValue = (E)testSet.getInstance(j).get(dataCopy.getClassifyAttribute());
                actualValues.add(actualValue);

                if(!predictedValue.equals(actualValue)){
                    misclassified++;
                }
            }

            misclass[i] = misclassified;
            accuracies[i] = (double) (testSet.getNbrInstances() - misclassified) / testSet.getNbrInstances();
            System.out.println(i+1 + " fold accuracy: " + accuracies[i]);
        }
        double meanAccuracy = 0.0;
        int meanSuccess = 0;
        for(int i=0;i<accuracies.length;i++){
            meanAccuracy += accuracies[i];
            meanSuccess += testSet.getNbrInstances() - misclass[i];
        }

        //calculate correlation coefficient between actual and predicted values
        //correlationCoefficient(predictedValues, actualValues, data.getNbrInstances());

        meanSuccess = meanSuccess/accuracies.length;
        meanAccuracy = meanAccuracy/accuracies.length;
        meanAcc = "Mean accuracy across " + folds +"-folds CV: " +
                df.format(meanAccuracy*100) + "% " + eol;

        formattedResults = formattedResults + meanAcc;
        System.out.println(formattedResults);

        return ( meanAccuracy );

    }

        public double percentageSplitIBcs(IBcs IB, Instances data, double percentage){
        Instances dataCopy =(Instances) data.clone();
        String eol = System.getProperty("line.separator");
        String formattedResults = "Results of " + percentage + "-percentage split " +
                " testing for IBcs:" + eol +
                "=============================================================="+eol;
        String runTimes = "";
        String meanAcc = "";
        ArrayList<E> actualValues = new ArrayList<E>();
        ArrayList<E> predictedValues = new ArrayList<E>();
        DecimalFormat df = new DecimalFormat("#.##");
        Instances trainSet = new Instances();
        Instances testSet = new Instances();
        double[] accuracies = new double[1];
        int misclassified = 0;

        long start = 0;
        long runTime = 0;
        int[] misclass = new int[1];
            start = System.currentTimeMillis();

            //select train set
            int trainSetSize =(int)  Math.round(dataCopy.getNbrInstances() * percentage);
            trainSet.clear();

            for(int j=0;j<trainSetSize;j++){
                trainSet.add(dataCopy.get(j));
            }

            trainSet.setNbrColumns(dataCopy.getNbrColumns());
            trainSet.setColumnTypes(dataCopy.getColumnTypes());
            trainSet.max = (ArrayList) dataCopy.max.clone();
            trainSet.min = (ArrayList) dataCopy.min.clone();
            IB.train(trainSet);

            //select test set
            testSet.clear();
            for(int j=trainSetSize;j<dataCopy.getNbrInstances();j++){
                testSet.add(dataCopy.get(j));
            }

            misclassified = 0;
            for(int j=0;j<testSet.getNbrInstances();j++){
                E predictedValue = (E)IB.classify(testSet.getInstance(j), dataCopy.getClassifyAttribute());
                predictedValues.add(predictedValue);
                E actualValue = (E)testSet.getInstance(j).get(dataCopy.getClassifyAttribute());
                actualValues.add(actualValue);

                if(j % 100 == 0){
                    System.out.println(j);
                }

                if(!predictedValue.equals(actualValue)){
                    misclassified++;
                }
            }
            //this.ROC(predictedValues, actualValues);
            misclass[0] = misclassified;
            accuracies[0] = (double) (testSet.getNbrInstances() - misclassified) / testSet.getNbrInstances();
            System.out.println("accuracy: " + accuracies[0]);
            runTime = System.currentTimeMillis() - start;
            //runTimes = runTimes+(i+1) + "-fold CV: " + Long.toString(runTime) + "msec" + eol;
        double meanAccuracy = accuracies[0];
        int meanSuccess = testSet.getNbrInstances() - misclass[0];

        //calculate correlation coefficient between actual and predicted values
        //correlationCoefficient(predictedValues, actualValues, data.getNbrInstances());

        meanSuccess = meanSuccess/accuracies.length;
        meanAccuracy = meanAccuracy/accuracies.length;
        meanAcc = "Mean accuracy: " + df.format(meanAccuracy*100) + "% " +
                meanSuccess + "/" + testSet.getNbrInstances() + eol;

        formattedResults = formattedResults + meanAcc;
        System.out.println(formattedResults);

        return ( meanAccuracy );

    }

        public double percentageSplitIBk(IBk IB, Instances data, double percentage){
        Instances dataCopy =(Instances) data.clone();
        String eol = System.getProperty("line.separator");
        String formattedResults = "Results of " + percentage + "-percentage split " +
                " testing for IBk with k=" +IB.k + ": " + eol +
                "=============================================================="+eol;
        String runTimes = "";
        String meanAcc = "";
        ArrayList<E> actualValues = new ArrayList<E>();
        ArrayList<E> predictedValues = new ArrayList<E>();
        DecimalFormat df = new DecimalFormat("#.##");
        Instances trainSet = new Instances();
        Instances testSet = new Instances();
        double[] accuracies = new double[1];
        int misclassified = 0;

        long start = 0;
        long runTime = 0;
        int[] misclass = new int[1];
            start = System.currentTimeMillis();

            //select train set
            int trainSetSize =(int)  Math.round(dataCopy.getNbrInstances() * percentage);
            trainSet.clear();

            for(int j=0;j<trainSetSize;j++){
                trainSet.add(dataCopy.get(j));
            }

            trainSet.setNbrColumns(dataCopy.getNbrColumns());
            trainSet.setColumnTypes(dataCopy.getColumnTypes());
            trainSet.max = (ArrayList) dataCopy.max.clone();
            trainSet.min = (ArrayList) dataCopy.min.clone();
            IB.train(trainSet);

            //select test set
            testSet.clear();
            for(int j=trainSetSize;j<dataCopy.getNbrInstances();j++){
                testSet.add(dataCopy.get(j));
            }

            misclassified = 0;
            for(int j=0;j<testSet.getNbrInstances();j++){
                E predictedValue = (E)IB.classify(testSet.getInstance(j), dataCopy.getClassifyAttribute());
                predictedValues.add(predictedValue);
                E actualValue = (E)testSet.getInstance(j).get(dataCopy.getClassifyAttribute());
                actualValues.add(actualValue);

                if(j % 100 == 0){
                    System.out.println(j);
                }

                if(!predictedValue.equals(actualValue)){
                    misclassified++;
                }
            }
            //this.ROC(predictedValues, actualValues);
            misclass[0] = misclassified;
            accuracies[0] = (double) (testSet.getNbrInstances() - misclassified) / testSet.getNbrInstances();
            System.out.println("accuracy: " + accuracies[0]);
            runTime = System.currentTimeMillis() - start;
            //runTimes = runTimes+(i+1) + "-fold CV: " + Long.toString(runTime) + "msec" + eol;
        double meanAccuracy = accuracies[0];
        int meanSuccess = testSet.getNbrInstances() - misclass[0];

        //calculate correlation coefficient between actual and predicted values
        //correlationCoefficient(predictedValues, actualValues, data.getNbrInstances());

        meanSuccess = meanSuccess/accuracies.length;
        meanAccuracy = meanAccuracy/accuracies.length;
        meanAcc = "Mean accuracy: " + df.format(meanAccuracy*100) + "% " +
                meanSuccess + "/" + testSet.getNbrInstances() + eol;

        formattedResults = formattedResults + meanAcc;
        System.out.println(formattedResults);

        return ( meanAccuracy );

    }


        private double mean(ArrayList<Double> values){
            double value = 0.0;
            for(int i=0;i<values.size();i++){
                value += values.get(i);
            }
            value = value/values.size();
            return value;
        }

        private void correlationCoefficient(ArrayList<Double> predictedValues,
                ArrayList<Double> actualValues,int totalNumberOfInstances){
            double predictedValuesMean = mean(predictedValues);
            System.out.println("predicted mean: " + predictedValuesMean);
            double actualValuesMean = mean(actualValues);
            System.out.println("actual mean: " + actualValuesMean);

            //calc standard deviation of predicted and actual values
            double predictedValuesStd = 0.0;
            double actualValuesStd = 0.0;
            for(int i=0;i<actualValues.size();i++){
                predictedValuesStd += Math.pow((predictedValues.get(i) - predictedValuesMean),2);
                actualValuesStd += Math.pow((actualValues.get(i) - actualValuesMean),2);
            }

            predictedValuesStd = Math.pow(predictedValuesStd, 1/2);
            System.out.println("predicted std: " + predictedValuesStd);
            actualValuesStd = Math.pow(actualValuesStd, 1/2);
            System.out.println("actual std: " + actualValuesStd);

            double R = 0.0;
            for(int i=0;i<actualValues.size();i++){
                R += (((predictedValues.get(i) - predictedValuesMean) / predictedValuesStd) *
                        ((actualValues.get(i) - actualValuesMean) / actualValuesStd));
            }
            R = R/(totalNumberOfInstances - 1);
            System.out.println("R: " + R);
        }

        private void ROC(ArrayList predictedValues, ArrayList actualValues){
            int TP = 0;
            int FP = 0;
            int TN = 0;
            int FN = 0;
            for(int i=0;i<predictedValues.size();i++){
                if(predictedValues.get(i).equals("g") && actualValues.get(i).equals("g")){
                    TP++;
                }
                if(predictedValues.get(i).equals("g") && actualValues.get(i).equals("h")){
                    FP++;
                }
                if(predictedValues.get(i).equals("h") && actualValues.get(i).equals("g")){
                    FN++;
                }
                if(predictedValues.get(i).equals("h") && actualValues.get(i).equals("h")){
                    TN++;
                }
            }

            double TPR = TP+FN;
            TPR = TP / TPR;
            double FPR = FP+TN;
            FPR = FP / FPR;
            double specificity = FP+TN;
            specificity = TN/specificity;
            double precision = TP+FP;
            precision = TP/precision;
            System.out.println(TP + ", " + FP + " | " + FN + ", " + TN);
            System.out.println("TPR: " + TPR);
            System.out.println("FPR: " +FPR);
        }

        private void writeToFile(ArrayList<E> predictedValues,
                ArrayList<E> actualValues, String filename){
        String eol = System.getProperty("line.separator");
        try{
            FileWriter fstream = new FileWriter(filename,true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("#    Predicted    Actual");
            for(int i=0;i<predictedValues.size();i++){
                out.write(i + "     " + predictedValues.get(i) + "    " + actualValues.get(i) + eol);
            }
            //Close the output stream
            out.close();
        }
        catch(Exception e){
             System.err.println("Error: " + e.getMessage());
        }
    }

}
