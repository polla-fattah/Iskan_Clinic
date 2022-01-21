package soft.eng.client;

public class DataOrder implements Comparable<DataFilter>{
	String field;
	String sort;
	public static String DESCENDING  = " DESC ", ASCENDING  = " ASC ";
	public DataOrder(String f){
		field = f;
		sort = ASCENDING;
	}
	public DataOrder(String f, String s){
		field = f;
		sort = s;
	}
	@Override
	public boolean equals(Object obj){
		DataFilter temp = (DataFilter) obj;
		return field.equalsIgnoreCase(temp.field);
	}
	@Override
	public String toString(){
		return "{\"field\":\"" + field + "\",\"sorting\":\"" + sort + "\"}";
	}
	@Override
	public int compareTo(DataFilter f){
		return field.compareTo(f.field);
	}
}
