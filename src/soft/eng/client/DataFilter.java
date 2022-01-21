package soft.eng.client;

public class DataFilter implements Comparable<DataFilter>{
	String field;
	String value;
	String operator;
	String whereCondition;
	public static String EQUAL = " = ", LESS_THAN = " < ", GRATOR_THAN = " > ", IN = " IN ", AND = " AND ", OR = " OR ";
	public DataFilter(String f, String v){
		field = f;
		value = v;
		operator = EQUAL;
		whereCondition = AND;
	}
	public DataFilter(String f, String v, String o, String w){
		field = f;
		value = v;
		operator = o;
		whereCondition = w;
	}
	@Override
	public boolean equals(Object obj){
		DataFilter temp = (DataFilter) obj;
		return field.equalsIgnoreCase(temp.field) && value.equalsIgnoreCase(temp.value);
	}
	@Override
	public String toString(){
		return "{\"field\":\"" + field + "\",\"value\":\"" + value + "\", \"operator\":\""+ operator +"\", \"whereCondition\":\""+ whereCondition +"\"}";
	}
	@Override
	public int compareTo(DataFilter f){
		return field.compareTo(f.field);
	}
}
