package com.qualcify.utils;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
// import java.util.List;

public class WekaClassifier {

    public static String classifyBatch(int defectCount) throws Exception {
        // 1. Define attributes
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("Defect"));

        // Class attribute
        ArrayList<String> classVals = new ArrayList<>();
        classVals.add("Type A");
        classVals.add("Type B");
        classVals.add("Type C");
        attributes.add(new Attribute("DefectType", classVals));

        // 2. Create dataset
        Instances dataset = new Instances("Defects", attributes, 0);
        dataset.setClassIndex(1);

        // Add sample instances (training)
        dataset.add(new DenseInstance(1.0, new double[]{2, classVals.indexOf("Type A")}));
        dataset.add(new DenseInstance(1.0, new double[]{5, classVals.indexOf("Type B")}));
        dataset.add(new DenseInstance(1.0, new double[]{7, classVals.indexOf("Type C")}));
        dataset.add(new DenseInstance(1.0, new double[]{3, classVals.indexOf("Type A")}));
        dataset.add(new DenseInstance(1.0, new double[]{6, classVals.indexOf("Type B")}));

        // 3. Train classifier
        Classifier cls = new J48(); // Decision tree
        cls.buildClassifier(dataset);

        // 4. Classify new instance
        DenseInstance newInst = new DenseInstance(2);
        newInst.setValue(attributes.get(0), defectCount);
        newInst.setDataset(dataset);

        double predIndex = cls.classifyInstance(newInst);
        return classVals.get((int) predIndex);
    }
}
