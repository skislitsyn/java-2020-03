package skislitsyn;

public class TestObjectDependency {
    private int i;
    private String s;

    public TestObjectDependency(int i, String s) {
	this.i = i;
	this.s = s;
    }

    @Override
    public String toString() {
	return "TestObjectDependency{" + "i=" + i + ", s=" + s + "}";
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == this) {
	    return true;
	}

	if (!(obj instanceof TestObjectDependency)) {
	    return false;
	}

	TestObjectDependency testObjectDependency = (TestObjectDependency) obj;

	return Integer.compare(i, testObjectDependency.i) == 0 && s.equals(testObjectDependency.s);
    }
}
