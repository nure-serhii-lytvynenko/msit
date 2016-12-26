package wumpus.wumpus;

public class WampusEnviroment {
	private int pos_i = 0;
	private int pos_j = 0;

	private String visibleField[][] = new String[4][4];
	public String[][] getVisibleField() {
		return visibleField;
	}
	public void setVisibleField(String[][] visibleField) {
		this.visibleField = visibleField;
	}
	public String getSpeleoPosition() {
		return pos_i+"-"+pos_j;
	}
	public void setPos_i(int pos_i) {
		this.pos_i = pos_i;
	}
	public void setPos_j(int pos_j) {
		this.pos_j = pos_j;
	}
	public int getPos_i() {
		return pos_i;
	}
	public int getPos_j() {
		return pos_j;
	}
	
	
	/**
	 * string in format: 1.1
	 * where split indexes is splited by dot
	 * */
	public void changeGameCellValue(String index, String cellValue) {
		String[] a = index.split("-");
		if(a.length == 2){
			this.visibleField[Integer.parseInt(a[0])][Integer.parseInt(a[1])] = cellValue;
			System.out.println(index +"cell value was changed to"+cellValue);
		}
	}
	
	
}
