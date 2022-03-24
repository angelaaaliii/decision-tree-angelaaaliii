package sol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.ArrayList;
import java.util.List;

public class DecisionTreeTest {

    // initializing objects to use for unit testing
    Row spinach;
    Row orange;
    Row carrot;
    Dataset testDataSet;
    List<String> attributeList;
    List<Row> dataObjects;

    @Before
    public void setupData() {
        // adding field values to objects to use for unit testing
        spinach = new Row("spinach");
        spinach.setAttributeValue("color", "green");
        spinach.setAttributeValue("highProtein", "true");
        spinach.setAttributeValue("calories", "low");
        spinach.setAttributeValue("foodType", "vegetable");

        orange = new Row("orange");
        orange.setAttributeValue("color", "orange");
        orange.setAttributeValue("highProtein", "false");
        orange.setAttributeValue("calories", "high");
        orange.setAttributeValue("foodType", "fruit");

        carrot = new Row("carrot");
        carrot.setAttributeValue("color", "orange");
        carrot.setAttributeValue("highProtein", "false");
        carrot.setAttributeValue("calories", "medium");
        carrot.setAttributeValue("foodType", "vegetable");

        attributeList = new ArrayList<String>();
        attributeList.add("color");
        attributeList.add("highProtein");
        attributeList.add("calories");
        attributeList.add("foodType");

        dataObjects = new ArrayList<Row>();
        dataObjects.add(spinach);
        dataObjects.add(orange);
        dataObjects.add(carrot);

        testDataSet = new Dataset(attributeList, dataObjects);
    }

    // partition into two datasets
    @Test
    public void testPartition() {
        List<Dataset> subset = testDataSet.partition("color");
        Assert.assertEquals(subset.size(), 2);
        boolean b = true;
        for (Dataset set : subset) {
            String prevValue = set.getDataObjects().get(0).getAttributeValue(
                    "color");

            for (Row row : set.getDataObjects()) {
                if (!row.getAttributeValue("color").equals(prevValue)) {
                    b = false;
                }
            }
        }
        Assert.assertEquals(b, true);
    }

    // newDataset on high protein - getting a copy of the original dataset
    // that only contains rows with false as the highProtein attribute value
    @Test
    public void testNewDataset() {
        Dataset newData = testDataSet.newDataset("highProtein", "false");
        Assert.assertEquals(newData.getDataObjects().size(), 2);
        boolean b = true;
        for (Row row : newData.getDataObjects()) {
            if (!"false".equals(row.getAttributeValue("highProtein")))
                b = false;
        }
        Assert.assertEquals(b, true);
    }

    // getValues on calorie attribute - getting all of the possible unique
    // values of the calorie attribute. 
    @Test
    public void testGetValues() {
        List<String> values = testDataSet.getValues("calories");
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values.contains("high"), true);
        Assert.assertEquals(values.contains("medium"), true);
        Assert.assertEquals(values.contains("low"), true);
    }

    // testing computing the default value when dataset contains more rows
    // that have the vegetable attribute value.
    @Test
    public void testComputeDefaultValue() {
        String defaultVal = testDataSet.computeDefaultValue("foodType");
        Assert.assertEquals(defaultVal, "vegetable");
    }

    // testing removing the color attribute from the dataset rows
    @Test
    public void testRemoveAttribute() {
        List<String> newAttributeList = testDataSet.removeAttribute("color");
        Assert.assertEquals(newAttributeList.contains("color"), false);
    }

    // testing allSameValue method when attribute values in the rows of the
    // dataset are different
    @Test
    public void testAllSameValue() {
        boolean b = testDataSet.allSameValue("foodType");
        Assert.assertEquals(b, false);
    }

    // testing getting attribute value when all of the values of the rows in the
    // dataset are equal
    @Test
    public void getSingleValueTest() {
        Row cucumber = new Row("cucumber");
        cucumber.setAttributeValue("color", "green");
        cucumber.setAttributeValue("highProtein", "false");
        cucumber.setAttributeValue("calories", "low");
        cucumber.setAttributeValue("foodType", "vegetable");
        List<Row> dataObjects2 = new ArrayList<Row>();
        dataObjects2.add(spinach);
        dataObjects2.add(cucumber);
        Dataset testSet2 = new Dataset(attributeList, dataObjects2);

        Assert.assertEquals(testSet2.getSingleValue("foodType"), "vegetable");

    }

    // partition resulting in 2 datasets in list
    @Test
    public void partitionTwo() {
        Row cucumber = new Row("cucumber");
        cucumber.setAttributeValue("color", "green");
        Row tangerine = new Row("tangerine");
        tangerine.setAttributeValue("color", "orange");
        List<Row> dataObjects2 = new ArrayList<Row>();
        dataObjects2.add(tangerine);
        dataObjects2.add(cucumber);
        List<String> attributeList2 = new ArrayList<String>();
        attributeList2.add("color");
        Dataset testSet2 = new Dataset(attributeList2, dataObjects2);

        List<Dataset> result = testSet2.partition("color");
        Assert.assertEquals(result.size(), 2);
    }

    // Constructor for DecisionTreeTest tester class - don't need to add
    public DecisionTreeTest() {

    }

    // food csv predicting row that is in the dataset we trained on,
    // i.e. the sweet potato item
    @Test
    public void testGetDecision1() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse("sol/food.csv");
        List<String> attributeList = new ArrayList<>(dataObjects.get(
                0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects);

        TreeGenerator generator = new TreeGenerator();
        generator.generateTree(training, "foodType");

        String decision = generator.getDecision(dataObjects.get(4));
        Assert.assertEquals(decision, "vegetable");
    }

    // admissions csv predicting row that is in the dataset we trained on,
    // i.e. the fourth student in the dataset (should be reject)
    @Test
    public void testGetDecision3() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(
                "sol/admissions.csv");
        List<String> attributeList = new ArrayList<>(dataObjects.get(
                0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects);

        TreeGenerator generator = new TreeGenerator();
        generator.generateTree(training, "outcome");

        String decision = generator.getDecision(dataObjects.get(3));
        Assert.assertEquals(decision, "reject");
    }

    // admissions csv predicting row that contains the same attributes
    // as one of the rows in the dataset that we trained on
    @Test
    public void testGetDecision4() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(
                "sol/admissions.csv");
        List<String> attributeList = new ArrayList<>(dataObjects.get(
                0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects);

        TreeGenerator generator = new TreeGenerator();
        generator.generateTree(training, "outcome");

        Row studentTwo = new Row("studentTwo");
        studentTwo.setAttributeValue("GPA", "C");
        studentTwo.setAttributeValue("high school", "red school");
        studentTwo.setAttributeValue("extra curriculars", "weak");
        studentTwo.setAttributeValue("letters of rec", "weak");

        String decision = generator.getDecision(studentTwo);
        Assert.assertEquals(decision, "reject");
    }

}
