package soft.eng.client;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;

public class Complains implements DrivedData{

	private OkDialogBox okDlg;
	private final String SERVICE_URL = Security.server + "management.php?key=" + Security.key + "&table=complain&userName=" + Security.userName;
	
	public static final String ID = "idComplain";
	public static final String NAME = "name";
	
	private final String LODING_EROR = "Couldn't retrieve Data from Server.<br>Try Again, Please!";
	private final String EDITING_EROR = "Couldn't Edit this item on the Server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>3- This Catigory Actually Existes.<br>Try Again, Please!";
	private final String DELETING_EROR = "Couldn't delete this item on the Server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>Try Again, Please!";
	private final String ADDING_EROR = "Couldn't add new item to the Server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>3- This Catigory Actually Existes.<br>Try Again, Please!";
	
	private  TreeMap<Integer, Complain> complainMap;
	private TreeMap<Integer, TreeSet<Complain>> index;
	private TreeMap<String, TreeSet<Complain>> nameIndex;
	
	public Complains() {

		complainMap = new TreeMap<Integer, Complain>();
		index = new TreeMap<Integer, TreeSet<Complain>>();
		nameIndex = new TreeMap<String, TreeSet<Complain>>();
		
		okDlg = new OkDialogBox("Connection Error", LODING_EROR);
		loadData();
	}//End of Constructor
	
	private void putToIndex(Complain m){
		int id = m.getSystem();
		String n = m.getName();
		
		if(!nameIndex.containsKey(n))
			nameIndex.put(n, new TreeSet<Complain>());
		nameIndex.get(n).add(m);
		
		if(!index.containsKey(id))
			index.put(id, new TreeSet<Complain>());
		index.get(id).add(m);
	}
	
	public TreeSet<Complain> getSystemChildren(int idSystem){
		return index.get(idSystem);
	}
	
	private void loadData(){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, SERVICE_URL + "&require=loading");

		try{
			builder.sendRequest(null, new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(LODING_EROR);
		        	}
		        
		        public void onResponseReceived(Request request, Response response){
		          if (200 == response.getStatusCode()) {
		        	  int id = 0, idCategory;
		        	  String name = "";
		        	  JSONArray array = (JSONArray)JSONParser.parse(response.getText());
		        	  for(int i = 0 ; i < array.size(); i++){
		        		  id = Integer.parseInt(array.get(i).isObject().get(ID).isString().stringValue());
		        		  idCategory = Integer.parseInt(array.get(i).isObject().get(Systems.ID).isString().stringValue());
		        		  name = array.get(i).isObject().get(NAME).isString().stringValue();
		        		  Complain temp = new Complain(id, name, idCategory);
		        		  complainMap.put(id, temp);
		        		  putToIndex(temp);
		        	  }
		        	  onFinishLoading();
		          }
		          else{
		        	  okDlg.center(LODING_EROR);
			        }
		        }
	      });
		}catch(RequestException ex){
			okDlg.center(LODING_EROR);
		}
	}
	public void onFinishLoading(){
	}
	public Complain getComplain(int id){
		Complain value = complainMap.get(id);
		return new Complain(value.getId(), value.getName(), value.getSystem());
	}
	public TreeSet<Complain> getComplainsByName(String name){
		return nameIndex.get(name);
	}
	public Set<String> getAllComplainNames(){
		return nameIndex.keySet();
	}
	public void put(final Complain complain){
		if(complainMap.containsValue(complain))
			return;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=adding");
		builder.setHeader("Content-complain", "application/x-www-form-urlencoded");


		StringBuffer postData = new StringBuffer();
		postData.append(URL.encode("row")).append("=").append(complain.toJSON());
		postData.append("&");
		postData.append(URL.encode("id")).append("=").append(ID);
		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(ADDING_EROR);
		        	}
	
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  	try{
		        	  		int i = Integer.parseInt(response.getText().trim());
		        	  		complain.setId(i);
		        			complainMap.put(i, complain);
		        			putToIndex(complain);
		        	  	}
		        	  	catch(Exception e){
		        	  		okDlg.center(ADDING_EROR);

		        	  	}	
		          }
		          else {
		        	  okDlg.center(ADDING_EROR);
			        }
		        }
	      });
		}catch(RequestException ex){
			okDlg.center(ADDING_EROR);
		}
	}
	
	public void set(final Complain cat){
		final int id = cat.getId();
		if(!complainMap.containsKey(id))
			return;
		if(cat.equals(complainMap.get(id)))
			return;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=editing");
		builder.setHeader("Content-complain", "application/x-www-form-urlencoded");

		StringBuffer postData = new StringBuffer();
		postData.append(URL.encode("row")).append("=").append(cat.toJSON());
		postData.append("&");
		postData.append(URL.encode("id")).append("=").append(ID);

		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(EDITING_EROR);
		        	}
	
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  	if(response.getText().trim().equalsIgnoreCase("Success")){

		        	  		TreeSet<Complain> temp = index.get(cat.getSystem());
		        	  		temp.remove(complainMap.get(id));
		        	  		complainMap.put(id, cat);
		        	  		temp.add(cat);
		        			
		        		}
		        	  	else
		        	  		okDlg.center(EDITING_EROR);
		          }
		          else {
		        	  okDlg.center(EDITING_EROR);
			        }
		        }
	      });
		}catch(RequestException ex){
			okDlg.center(EDITING_EROR);
		}
	}
	
	public void delete(final int id){
		if(!complainMap.containsKey(id) )
			return;
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=deleting");
		builder.setHeader("Content-complain", "application/x-www-form-urlencoded");

		StringBuffer postData = new StringBuffer();
		postData.append(URL.encode("idvalue")).append("=").append(id);
		postData.append("&");
		postData.append(URL.encode("id")).append("=").append(ID);

		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(DELETING_EROR);
		        	}
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()){
		        	  
		        	  	if(response.getText().trim().equalsIgnoreCase("Success")){
		        	  		Complain tt = complainMap.get(id);
		        	  		int cat = tt.getSystem();
		        	  		
		        	  		index.get(cat).remove(tt);
		        			complainMap.remove(id);
		        	  	}
		        	  	else
		        	  		okDlg.center(DELETING_EROR);
		          }
		          else {
		        	  okDlg.center(DELETING_EROR);
			        }
		        }
	      });
		}catch(RequestException ex){
			okDlg.center(DELETING_EROR);
		}
	}
	
	@Override
	public String view(double id) {
		return getComplain((int)id).getName();
	}

	public static class Complain implements Comparable<Complain>{
		private int id;
		private int idSystem;
		private String name;
		private String note;
		
		public Complain(){
		}
		public Complain(int id, String name, int idSystem){
			this.id= id;
			this.name = name;
			this.idSystem = idSystem;
		}
		public Complain(int id, String name, int idSystem, String note){
			this.id= id;
			this.name = name;
			this.idSystem = idSystem;
			this.note = note;
		}
		public String toString(){
			return name;
		}
		public String toJSON(){
			return "{\""+ ID + "\":\"" + id + "\", \"" + NAME + "\":\"" + name +"\", \"" + Systems.ID + "\":\"" + idSystem +"\"}";
		}
		@Override 
		public int hashCode(){
			return name.hashCode();
		}
		@Override 
		public boolean equals(Object obj){
			Complain temp = (Complain) obj;
			return name.equalsIgnoreCase(temp.getName()) && id == temp.getId();
		}
		@Override 
		public int compareTo(Complain comp){
			int c = this.name.toLowerCase().compareTo(comp.getName().toLowerCase());
			if(c == 0)
				return (new Integer(id)).compareTo(comp.getId());
			else
				return c;
		}

		public  String getName(){
			return this.name;
		}
		public int getId(){
			return this.id;
		}
		public int getSystem(){
			return this.idSystem;
			
		}
		public String getNote(){
			return note;
		}		
	
		public void setName(String name){
			this.name = name;
		}		
		public void setId(int id){
			this.id = id;
		}
		public void setSytem(int system){
			this.idSystem = system;
		}
		public void setNote(String note){
			this.note = note;
		}
	}

	
}
