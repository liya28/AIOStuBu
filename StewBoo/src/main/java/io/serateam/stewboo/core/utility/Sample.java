package io.serateam.stewboo.core.utility;

import java.util.ArrayList;
import java.util.List;

public class Sample implements ISerializable
{
    private String testString;
    private int testInt;
    private boolean test;
    private List<String> myList;

    public Sample()
    {
        testString = "ss";
        testInt = 5;
        test = true;
        myList = new ArrayList<>();
        for (int i = 0; i < testInt; i++) {
            myList.add("Test"+i);
        }
    }

    @Override
    public String toString() {
        return String.format("testString: %s // int: %d // bool: %b // myList: %s", testString, testInt, test, String.join(", ", myList));
    }
}
