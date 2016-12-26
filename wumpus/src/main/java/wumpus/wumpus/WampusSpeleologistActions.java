package wumpus.wumpus;

public enum WampusSpeleologistActions {
	//[(1;1), Stench, breeze, glitter, bump, scream]
		LEFT("left"),
		RIGHT("right"),
		UP("up"),
	    DOWN("down");

	    private final String name;       

	    private WampusSpeleologistActions(String s) {
	        name = s;
	    }

	    public boolean equalsName(String otherName) {
	        return (otherName == null) ? false : name.equals(otherName);
	    }

	    public String toString() {
	       return this.name;
	    }

}
