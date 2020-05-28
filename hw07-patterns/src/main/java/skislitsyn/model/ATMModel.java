package skislitsyn.model;

public enum ATMModel {
    M100("M100 model"), M200("M200 model"), M201("M201 model");

    private String name;

    ATMModel(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }
}
