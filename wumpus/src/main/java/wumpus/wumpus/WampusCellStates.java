package wumpus.wumpus;

public enum WampusCellStates {
	//[0-0, Stench, breeze, glitter, bump, scream]
	
	OK("ok"),
	Q("q"),
	
	DIG("dig"),
	WAMPUS("wampus"),
	
	NONE("NONE"),
	
	STENCH("stench"),
    BREEZE("breeze"),
    BUMP("bump"),
    SCREAM("scream"),
    GLITTER("glitter");

    private final String name;       

    private WampusCellStates(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
}
