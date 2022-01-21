package soft.eng.client;


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


public class Systems implements DrivedData{
	private final String LODING_EROR = "Couldn't retrieve Some Data from Server.<br>Try Again, Please!";
	private final String EDITING_EROR = "Couldn't Edit this item on the Server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>3- This Catigory Actually Existes.<br>Try Again, Please!";
	private final String DELETING_EROR = "Couldn't delete this item on the Server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>Try Again, Please!";
	private final String ADDING_EROR = "Couldn't add new item to the Server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>3- This Catigory Actually Existes.<br>Try Again, Please!";

	private final String SERVICE_URL = Security.server + "management.php?key=" + Security.key + "&table=system&userName=" + Security.userName;
	private OkDialogBox okDlg = new OkDialogBox("Connection Error", LODING_EROR);

	public static final String ID = "idSystem";
	public static final String NAME = "name";

	private static TreeMap<Integer, System> systemMap = new TreeMap<Integer, System>();
	private static TreeSet<System> index = new TreeSet<System>();
	private static TreeMap<String, System> nameIndex = new TreeMap<String, System>();
	
	public Systems(){	
		loadData();
	}
	
	public TreeSet<System> getSystems(){
		return (new TreeSet<System>(index));
	}
	public System getSystem(String name){
		return nameIndex.get(name);
	}
	public System getSystem(int id){
		System value = systemMap.get(id);
		if(value == null)
			return null;
		System temp = new System(value.getId(), value.getName());
		return temp;
	}
	
	private void loadData(){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, SERVICE_URL + "&require=loading");

		try{
			builder.sendRequest(null, new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(LODING_EROR);
		        	}
	
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  int id = 0;
		        	  String name = "";
		        	  System temp;
		        	  JSONArray array = (JSONArray)JSONParser.parse(response.getText());

		        	  for(int i = 0 ; i < array.size(); i++){
		        		  id = Integer.parseInt(array.get(i).isObject().get(ID).isString().stringValue());
		        		  name = array.get(i).isObject().get(NAME).isString().stringValue();
		        		  temp = new System(id, name);
		        		  systemMap.put(id, temp);
		        		  index.add(temp);
		        		  nameIndex.put(temp.getName(), temp);
		        	  }
		        	  
		        	  onFinishLoading();
		          }
		          else {
		        	  okDlg.center(LODING_EROR);
			        }
		        }
	      });
		}catch(RequestException ex){
			okDlg.center(LODING_EROR);
		}
	}
	
	public void onFinishLoading() {		
	}
	

	public void put(final System cat){
		if(systemMap.containsValue(cat))
			return;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=adding");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");


		StringBuffer postData = new StringBuffer();
		postData.append(URL.encode("row")).append("=").append(cat.toJSON());
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
		        	  		cat.setId(i);
		        			systemMap.put(i, cat);
		        			index.add(cat);
		        			nameIndex.put(cat.getName(), cat);
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
	
	public void set(final System cat){
		final int id = cat.getId();
		if(!systemMap.containsKey(id))
			return;
		if(cat.equals(systemMap.get(id)))
			return;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=editing");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");

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
		        	  		index.remove(systemMap.get(id));
		        	  		nameIndex.remove(systemMap.get(id));
		        			
		        	  		systemMap.put(id, cat);
		        			nameIndex.put(cat.getName(), cat);
		        			index.add(cat);
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
		if(!systemMap.containsKey(id) )
			return;
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=deleting");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");

		StringBuffer postData = new StringBuffer();
		postData.append(URL.encode("idvalue")).append("=").append(id);
		postData.append("&");
		postData.append(URL.encode("id")).append("=").append("idSystem");

		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(DELETING_EROR);
		        	}
	
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  	if(response.getText().trim().equalsIgnoreCase("Success")){
		        			index.remove(systemMap.get(id));
		        			nameIndex.remove(systemMap.get(id));
		        			systemMap.remove(id);

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
		return getSystem((int)id).getName();

	}

	
	public static class System implements Comparable<System>{
		private int id;
		private String name;
		
		public System(){
		}
		public System(int id, String name){
			this.id= id;
			this.name = name;
		}
		@Override 
		public String toString(){
			return name;
		}
		public String toJSON(){
			return "{\""+ID+"\":\"" + id + "\", \""+NAME+"\":\"" + name +"\"}";
		}
		@Override 
		public int hashCode(){
			return name.hashCode();
		}
		@Override 
		public boolean equals(Object obj){
			System temp = (System) obj;
			return name.equalsIgnoreCase(temp.getName()) && id == temp.getId();
		}
		@Override 
		public int compareTo(System comp){
			return this.name.toLowerCase().compareTo(comp.getName().toLowerCase());
		}

		public  String getName(){
			return this.name;
		}
		public int getId(){
			return this.id;
		}
		public void setName(String name){
			this.name = name;
		}
		public void setId(int id){
			this.id = id;
		}
	}

}
