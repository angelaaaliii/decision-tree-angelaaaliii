package sol;

import src.IDataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.Row;

/**
 * A class that implements the IDataset interface,
 * representing a training data set.
 */
public class Dataset implements IDataset {
    private List<String> attributeList;
    private List<Row> dataObjects;

    /**
     * Constructs a Dataset object
     *
     * @param attributeList - all the attributes in the dataset
     * @param dataObjects   - all the rows in the dataset
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects) {
        this.attributeList = attributeList;
        this.dataObjects = dataObjects;
    }

    /**
     * Gets list of attributes in the dataset
     *
     * @return a list of strings
     */
    public List<String> getAttributeList() {
        return this.attributeList;
    }

    /**
     * Gets list of data objects (row) in the dataset
     *
     * @return a list of Rows
     */
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }

    /**
     * finds the size of the dataset (number of rows)
     *
     * @return the number of rows in the dataset
     */
    public int size() {
        return this.dataObjects.size();
    }

    /**
     * returns a copy of the original Dataset object, with the input attribute
     * removed
     *
     * @param attribute - the attribute to remove from the list
     * @return a new Dataset object, not containing the input attribute
     */
    public List<String> removeAttribute(String attribute) {
        List<String> newAttributeList = new ArrayList<String>();
        for (String oldAttribute : this.attributeList) {
            if (!oldAttribute.equals(attribute)) {
                newAttributeList.add(oldAttribute);
            }
        }
        return newAttributeList;
    }

    /**
     * returns a new dataset object with all rows containing the specified
     * value for the specified attribute
     *
     * @param attributeValue - the value of the edge that treeGenerator is
     *                       going down
     * @return a new Dataset containing Rows with specified attribute values
     */
    public Dataset newDataset(String attributeName, String attributeValue) {
        List<Row> newRow = new ArrayList<Row>();
        for (Row oldRow : this.dataObjects) {
            if (oldRow.getAttributeValue(attributeName).equals(
                    attributeValue)) {
                newRow.add(oldRow);
            }
        }
        return new Dataset(this.removeAttribute(attributeName), newRow);
    }

    /**
     * returns a list of all the unique values that are present under the
     * specified attribute
     *
     * @param attributeName - the attribute that we want to get the values for
     * @return a list of all unique values of that attribute
     */
    public List<String> getValues(String attributeName) {
        List<String> attributeValues = new ArrayList<String>();
        for (Row row : dataObjects) {
            String value = row.getAttributeValue(attributeName);
            if (!attributeValues.contains(value))
                attributeValues.add(value);
        }
        return attributeValues;
    }

    /**
     * splits the current dataset based on their values for the given attribute
     *
     * @param attributeName - the attribute we want to use to split the dataset
     * @return a list of Datsets, grouped by their values for the given
     * attribute
     */
    public List<Dataset> partition(String attributeName) {
        List<String> values = this.getValues(attributeName);
        List<Dataset> subDatasets = new ArrayList<Dataset>();
        for (String value : values) {
            subDatasets.add(this.newDataset(attributeName, value));
        }
        return subDatasets;
    }

    /**
     * computes and returns the default value at a given node.
     *
     * @param targetAttribute - the attribute we want to the decision tree
     *                        to predict.
     * @return the default value a node would return.
     */
    public String computeDefaultValue(String targetAttribute) {
        List<Dataset> splitSet = this.partition(targetAttribute);
        int maxCounter = -1;
        String defaultValue = null;
        for (Dataset subset : splitSet) {
            if (subset.size() == maxCounter) {
                // if some decision values occur the same number of times
                // in the dataset, randomly pick one to return as the default
                // as we loop through the subsets.
                Random random = new Random();
                int upperBound = 1;
                int randomNum = random.nextInt(upperBound);
                if (randomNum == 0) {
                    defaultValue = subset.getDataObjects().get(
                            0).getAttributeValue(targetAttribute);
                }
            }
            if (subset.size() > maxCounter) {
                maxCounter = subset.size();
                defaultValue = subset.getDataObjects().get(
                        0).getAttributeValue(targetAttribute);
            }
        }
        return defaultValue;
    }

    /**
     * Determines if all the Row objects in the dataset have the same target
     * attribute decision
     *
     * @param targetAttribute - the target attribute whose value is being
     *                        checked
     * @return a boolean true if all the row objects have the same target
     * attribute value, else return false
     */
    public boolean allSameValue(String targetAttribute) {
        List<Row> rows = this.getDataObjects();
        String prevTarget = rows.get(0).getAttributeValue(targetAttribute);
        for (Row row : rows) {
            String currentTarget = row.getAttributeValue(targetAttribute);
            if (!prevTarget.equals(currentTarget)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Produces one value from the dataset's dataObjects list, given that it is
     * known that all the rows in dataObjects will have the same value
     * associated with the input attribute
     *
     * @param attributeName - the attribute whose values are being checked
     * @return a String of the value corresponding to the attributeName of the
     * first Row object in dataObjects
     */
    public String getSingleValue(String attributeName) {
        return this.dataObjects.get(0).getAttributeValue(attributeName);
    }
}
