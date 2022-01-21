package soft.eng.client;


public class DataHeader implements Comparable<DataHeader>{
	public String name;
	public String caption;
	public DrivedData refrence;
	public ActionedData action;
	public boolean editable;
	public boolean hidden;
	public String value = "";
	public boolean send = true; 
	
	public DataHeader(String n, String c){
		name = n;
		caption = c;
	}
	public DataHeader(String n, String c, DrivedData r){
		name = n;
		caption = c;
		refrence = r;
	}
	public DataHeader(String n, String c, DrivedData r, boolean e){
		name = n;
		caption = c;
		refrence = r;
		editable = e;
	}
	public DataHeader(String n, String c, DrivedData r, boolean e, boolean h, String v){
		name = n;
		caption = c;
		refrence = r;
		editable = e;
		hidden = h;
		value = v;
	}
	public DataHeader(String n, String c, DrivedData r, boolean e, boolean h, String v, boolean s){
		name = n;
		caption = c;
		refrence = r;
		editable = e;
		hidden = h;
		value = v;
		send = s;
	}
	public DataHeader(String n, String c, DrivedData r, ActionedData a){
		name = n;
		caption = c;
		refrence = r;
		action = a;
	}
	@Override 
	public String toString(){
		return name;
	}
	@Override
	public int compareTo(DataHeader f){
		return name.compareTo(f.name);
	}
	@Override
	public boolean equals(Object obj){
		DataHeader temp = (DataHeader) obj;
		return name.equalsIgnoreCase(temp.name);
	}
}
