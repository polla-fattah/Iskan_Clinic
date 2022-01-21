package soft.eng.client;

import java.util.ArrayList;  
import java.util.Iterator;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;

public class DBForm extends FlexTable implements BlurHandler{
	private final String LOADING_ERROR 	= "Couldn't load Items. Try Again, Please!";
	private final String EDITING_EROR 	= "Couldn't edit this item on the server. Causes<br>1- Server Down."+
								"<br>2- Connection Lost.<br>3- You Entered Wrong Value.<br>Try Again, Please!";

	private final OkDialogBox okDlg;
	private PopupPanel wating;
	private DBForm base = this;
	private String SERVICE_URL;
	private ArrayList<TextBox> addTxt;
	private final String TEXT_SIZE = "200";
	private ArrayList<DataHeader> headerElements = new ArrayList<DataHeader>();
	
	private String ID;
	public DBForm(String table, String id){
		this.setSize("100%", "100%");
		this.setStyleName("DBForm");
		ID = id;
		SERVICE_URL = Security.server + "DBForm.php?key=" + Security.key + "&table="+ table+ "&userName=" + Security.userName;
		
		okDlg = new OkDialogBox("Connection Error", LOADING_ERROR);

		wating = new PopupPanel();
		wating.add(new HTML("<b>Wait Please ...</b>"));
		
		this.setStyleName("TableData");
		this.setSize("100%", "100%");
		this.setCellPadding(0);
		this.setCellSpacing(0);
				
		this.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasAlignment.ALIGN_CENTER);
	}
	
	
	public void addHeader(DataHeader dataHeader){
		headerElements.add(dataHeader);
	}
	public void createForm(){
		buildHeader();
		Button add = new Button();
		add.setText("Add");
		add.addClickHandler(new AddHandler());
    	TextBox tb;
    	
    	int size = headerElements.size();
    	addTxt = new ArrayList<TextBox>();
    	int i = 0;
		for(Iterator<DataHeader> it = headerElements.iterator(); it.hasNext();){
			DataHeader dh = it.next();
			if(!dh.send)
				continue;
			tb = new TextBox();
			tb.setWidth(TEXT_SIZE);
			tb.setName(dh.name);
			addTxt.add(tb);
			tb.setText(dh.value);
			if(dh.hidden){
				tb.setVisible(false);
				continue;
			}
			this.setWidget(i , 1 , tb);
			this.getCellFormatter().setStyleName(i ,1,  "rowData");
			i++;
		}
		this.setWidget(size , 0 , add);
		this.getCellFormatter().setStyleName(size ,0,  "addData");
		this.getFlexCellFormatter().setColSpan(size, 0, 2);

	}
	private void buildHeader(){
		int i = 0;
		for(Iterator<DataHeader> it = headerElements.iterator(); it.hasNext();){
			DataHeader h = it.next();
			if(h.hidden || !h.send)
				continue;
			this.setText(i,0 , h.caption);
			this.getCellFormatter().setHorizontalAlignment(i, 0, HasAlignment.ALIGN_LEFT);
			base.getCellFormatter().setStyleName(i ,0,  "feildName");
			i++;
		}
	}
	
	public String loadForm(String objectStr){
		return fillData(objectStr);
	}

	public void loadForm(int id){
			wating.center();
			
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=loading");
			builder.setHeader("Content-type", "application/x-www-form-urlencoded");
			final StringBuffer postData = new StringBuffer();

			StringBuffer fi = new StringBuffer();
			for(Iterator<DataHeader> i = headerElements.iterator(); i.hasNext();)
				fi.append("`" + i.next().name+ "`, ");
			postData.append("fields").append("=").append(fi);
			postData.append("&");
			postData.append("id").append("=").append(ID);
			postData.append("&");
			postData.append("idValue").append("=").append(id);

			try{
				builder.sendRequest(postData.toString(), new RequestCallback() {
			        public void onError(Request request, Throwable exception){
			        	wating.hide();
			        	okDlg.center(LOADING_ERROR);
			        	}
	
			        public void onResponseReceived(Request request, Response response) {
			          if (200 == response.getStatusCode()) {
			        	  fillData(response.getText().trim());
			          }
			          else {
			        	  
			        	  okDlg.center(LOADING_ERROR);
			        	  System.err.println(response.getText());
				        }
			        }
		      });
			}catch(RequestException ex){
				ex.printStackTrace();
				wating.hide();
				okDlg.center(LOADING_ERROR);
			}
	}
	private String fillData(String jsonStr){
		buildHeader();
		String idStr = "";
		try{
  		String field = "", currentId = "";
    	DataHeader nextOne = null;
    	JSONObject object = (JSONObject)JSONParser.parse(jsonStr);
    	
    	TextBox tb;
    	int i = 0;
    		
    		for(Iterator<DataHeader> it = headerElements.iterator(); it.hasNext();i++){
    			nextOne = it.next();
    			if(nextOne.name.equalsIgnoreCase(ID))
    				idStr = object.isObject().get(nextOne.name).isString().stringValue();

    			field = object.isObject().get(nextOne.name).isString().stringValue();
    			if(nextOne.name.equalsIgnoreCase(ID))
    				currentId = field;

    			if(nextOne.refrence != null)
    				field = nextOne.refrence.view(new Double(field));
    			
    			if(nextOne.editable){
    				tb = new TextBox();
    				tb.setWidth(TEXT_SIZE);
    				tb.setText(field);
    				tb.setName(nextOne + "&%&" + currentId + "&%&" + field);
    				if(nextOne.hidden)	continue;
    				tb.addBlurHandler(base);
    				setWidget(i , 1 , tb);
    			}
    			else{
    				if(nextOne.hidden)	continue;
    				base.setWidget(i , 1, new Label(field));
    			}
    			getCellFormatter().setStyleName(i ,1,  "rowData");
    		  }
    	wating.hide();
  	}
  	catch(Exception e){
  		e.printStackTrace();
  		okDlg.center(LOADING_ERROR);
  	}
		return idStr;
	}
	@Override
	public void onBlur(BlurEvent event) {
		
		final TextBox tb = (TextBox) event.getSource();
		String array[] = tb.getName().split("&%&");
		
		final String currentValue = tb.getText();
		
		if(!array[2].equalsIgnoreCase(currentValue)){
			
			final String field = array[0];
			final String id = array[1];

			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=editing");
			builder.setHeader("Content-type", "application/x-www-form-urlencoded");

			StringBuffer postData = new StringBuffer();
			postData.append("id").append("=").append(ID);
			postData.append("&");
			postData.append("idValue").append("=").append(id);
			postData.append("&");
			postData.append("field").append("=").append(field);
			postData.append("&");
			postData.append("fieldValue").append("=").append(URL.encode(currentValue));
			
			try{
				builder.sendRequest(postData.toString(), new RequestCallback() {
			        public void onError(Request request, Throwable exception){
			        		okDlg.center(EDITING_EROR);
			        		tb.setFocus(true);
			        		tb.selectAll();
			        	}

			        public void onResponseReceived(Request request, Response response) {
			          if (200 == response.getStatusCode()) {
			        	  	if(response.getText().trim().equalsIgnoreCase("Success")){
			        	  		tb.setName(field + "&%&"+ id + "&%&"+ currentValue);
			        	  	} 
			        	  	else {
								okDlg.center(EDITING_EROR);
								tb.setFocus(true);
								tb.selectAll();
							}
			          }
			          else {
			        	  okDlg.center(EDITING_EROR);
			        	  tb.setFocus(true);
			        	  tb.selectAll();
				        }
			        }
		      });
			}catch(RequestException ex){
				ex.printStackTrace();
				okDlg.center(EDITING_EROR);
				tb.setFocus(true);
				tb.selectAll();
			}
		}//end of Big if
		
	}
	public void onFinishAdding(Response message){
		
	}
	private class AddHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=adding");
			builder.setHeader("Content-type", "application/x-www-form-urlencoded");
			
			StringBuffer postData = new StringBuffer();
			StringBuffer fields = new StringBuffer();
			StringBuffer values = new StringBuffer();
			TextBox temp;
			for (Iterator<TextBox> i = addTxt.iterator() ;i.hasNext();){
				temp = i.next();
				fields.append("`"+ temp.getName()+"`,");
				values.append("'"+ temp.getText()+"',");
			}
			
			fields.deleteCharAt(fields.length() -1);
			values.deleteCharAt(values.length() -1);

			postData.append("fields=").append(URL.encode(fields.toString()));
			postData.append("&");
			postData.append("values=").append(URL.encode(values.toString()));

			try{
				builder.sendRequest(postData.toString(), new RequestCallback() {
			        public void onError(Request request, Throwable exception){
			        		okDlg.center(EDITING_EROR);
			        	}

			        public void onResponseReceived(Request request, Response response) {
			          if (200 == response.getStatusCode()) {
			        	 try{
			        		 TextBox temp;
			     			for (Iterator<TextBox> i = addTxt.iterator() ;i.hasNext();){
			     				temp = i.next();
			     				if(temp.isVisible())
			     					temp.setText("");
			     			}
			        	  	onFinishAdding(response);
			        	  	}
			        	  	catch(Exception exc) {
								okDlg.center(EDITING_EROR);
							}
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
		
	}
}
