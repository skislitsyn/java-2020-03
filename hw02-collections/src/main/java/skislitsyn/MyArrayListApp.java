package skislitsyn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyArrayListApp {
    private static String[] testArray = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
	    "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
	    "twenty" };
    private static String[] testArray2 = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
	    "15", "16", "17", "18", "19", "20" };

    public static void main(String[] args) {
	// Collections.addAll
	// Testing ArrayList
	ArrayList<String> arrayList = new ArrayList<String>();
	for (String s : testArray) {
	    arrayList.add(s);
	}

	testCollectionsAddAll(arrayList, testArray2);

	// Testing MyArrayList
	MyArrayList<String> myArrayList = new MyArrayList<String>();
	for (String s : testArray) {
	    myArrayList.add(s);
	}

	testCollectionsAddAll(myArrayList, testArray2);

	// Collections.copy
	// Testing ArrayList
	ArrayList<String> arrayListSrc = new ArrayList<String>();
	ArrayList<String> arrayListDest = new ArrayList<String>();

	for (String s : testArray) {
	    arrayListSrc.add(s);
	}
	for (String s : testArray2) {
	    arrayListDest.add(s);
	}

	testCollectionsCopy(arrayListSrc, arrayListDest);

	// Testing MyArrayList
	MyArrayList<String> myArrayListSrc = new MyArrayList<String>();
	MyArrayList<String> myArrayListDest = new MyArrayList<String>();

	for (String s : testArray) {
	    myArrayListSrc.add(s);
	}
	for (String s : testArray2) {
	    myArrayListDest.add(s);
	}

	testCollectionsCopy(myArrayListSrc, myArrayListDest);

	// Collections.sort
	// Testing ArrayList
	testCollectionsSort(arrayList);

	// Testing MyArrayList
	testCollectionsSort(myArrayList);
    }

    private static void testCollectionsAddAll(List<String> list, String[] elements) {
	Collections.addAll(list, elements);

	System.out.println("Testing results of Collections.addAll method invocation with " + list.getClass().getName()
		+ " type list");
	for (String s : list) {
	    System.out.println(s);
	}
    }

    private static void testCollectionsCopy(List<String> srcList, List<String> destList) {
	Collections.copy(destList, srcList);

	System.out.println("Testing results of Collections.copy method invocation with " + srcList.getClass().getName()
		+ " type list");
	for (String s : destList) {
	    System.out.println(s);
	}
    }

    private static void testCollectionsSort(List<String> list) {
	Collections.sort(list, Comparator.naturalOrder());

	System.out.println("Testing results of Collections.sort method invocation with " + list.getClass().getName()
		+ " type list");
	for (String s : list) {
	    System.out.println(s);
	}
    }
}
