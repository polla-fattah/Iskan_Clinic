package soft.eng.client;

import java.util.ArrayList;  
import java.util.Iterator;
import java.util.TreeSet;
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
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;

public class DataTable extends FlexTable implements BlurHandler{
	private final String LOADING_ERROR  = "Couldn't load Items. Try Again, Please!";
	private final String REMOVING_ERROR = "Couldn't remove record. Try Again, Please!";
	private final String EDITING_EROR 	= "Couldn't edit this item on the server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>3- You Entered Wrong Value.<br>Try Again, Please!";

	private final OkDialogBox okDlg;
	private PopupPanel wating;
	private FlexTable dataFtbl;
	HorizontalPanel nextPrevHp;
	private Button next, previous;
	private DataTable base = this;
	private String SERVICE_URL;
	
	private ArrayList<DataHeader> headerElements = new ArrayList<DataHeader>();
	private TreeSet<DataFilter> filters = new TreeSet<DataFilter>();
	private TreeSet<DataOrder> orders = new TreeSet<DataOrder>();
	
	private int from, limit;
	private String ID;
	private boolean canDelete;
	public DataTable(String table, String id, boolean d){
		this(table, id);
		canDelete = d;
	}
	public DataTable(String table, String id){
		ID = id;
		SERVICE_URL = Security.server + "datatable.php?key=" + Security.key + "&table="+ table+ "&userName=" + Security.userName;
		
		dataFtbl = new FlexTable();

		next = new Button("&gt;&gt;");
		previous = new Button("&lt;&lt;");

		next.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				setFrom(from + limit);
				refresh();
			}});
		previous.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				setFrom((from - limit));
				next.setEnabled(true);
				refresh();
			}});		
		nextPrevHp = new HorizontalPanel();
    	
    	nextPrevHp.add(previous);
    	nextPrevHp.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
    	nextPrevHp.add(next);
    	
		okDlg = new OkDialogBox("Connection Error", LOADING_ERROR + 0);
		setFrom(0);
		setLimit(10);
		
		wating = new PopupPanel();
		wating.add(new HTML("<b>Wait Please ...</b>"));
		
		dataFtbl.setStyleName("TableData");
		dataFtbl.setSize("100%", "100%");
		dataFtbl.setCellPadding(0);
		dataFtbl.setCellSpacing(0);
		
		this.setWidget(0, 0, dataFtbl);
		this.setWidget(1, 0, nextPrevHp);
		
		this.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasAlignment.ALIGN_CENTER);
	}
	
	public void setFrom(int f){
		if(f <= 0){
			from = 0;
			previous.setEnabled(false);
		}
		else{
			previous.setEnabled(true);
			from = f;
		}
	}
	public int getFrom(){
		return from;
	}
	public void setLimit(int l){
		limit = l;
	}
	public int getLimit(){
		return limit;
	}
	public boolean addFilter(DataFilter f){
		if(!filters.add(f)){
			filters.remove(f);
			return filters.add(f);
		}
		else
			return true;
	}
	
	public boolean removeFilter(DataFilter f){
		return filters.remove(f);
	}
	
	public boolean addOrder(DataOrder o){
		if(!orders.add(o)){
			orders.remove(o);
			return orders.add(o);
		}
		else
			return true;
	}
	
	public boolean removeOrder(DataOrder o){
		return orders.remove(o);
	}
	
	public void addHeader(DataHeader h){
			headerElements.add(h);
	}
	
	private void buildHeader(){
		int i = 0;
		for(Iterator<DataHeader> it = headerElements.iterator(); it.hasNext();i++){
			DataHeader h = it.next();
			if(h.hidden)
				continue;
			dataFtbl.setText(0,i , h.caption);
		}
		dataFtbl.setText(0,i , " ");
		dataFtbl.getRowFormatter().setStyleName(0, "TableHeader");
	}

	private void buildBody(){
		wating.center();
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=loading");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		final StringBuffer postData = new StringBuffer();
		postData.append("filters").append("=").append(filters.toString());
		postData.append("&");
		postData.append("orders").append("=").append(orders.toString());
		postData.append("&");
		StringBuffer fi = new StringBuffer();
		for(Iterator<DataHeader> i = headerElements.iterator(); i.hasNext();){
			DataHeader h = i.next();
			fi.append("`" + h.name+ "`, ");
		}
		postData.append("fields").append("=").append(fi);
		postData.append("&");
		postData.append("from").append("=").append("" + from);
		postData.append("&");
		postData.append("to").append("=").append("" + limit);
		//Window.alert(postData.toString());
		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(LOADING_ERROR + 1);
		        		//Window.alert("1");
		        	}

		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  	try{
		        	  		String field = "", currentId = "";
				        	DataHeader nextOne = null;
				        	JSONArray array = (JSONArray)JSONParser.parse(response.getText());
				        	
				        	int j, size =  array.size();
				        	TextBox tb;
				        	if(size < limit){
				        		next.setEnabled(false);
				        	}
				        	else
				        		next.setEnabled(true);
				        	
				        	for(int i = 0 ; i < size ; i++){
				        		j = 0;
				        		
				        		for(Iterator<DataHeader> it = headerElements.iterator(); it.hasNext();j++){
				        			nextOne = it.next();

				        			field = array.get(i).isObject().get(nextOne.name).isString().stringValue();
				        			if(nextOne.name.equalsIgnoreCase(ID))
				        				currentId = field;
				        			if(nextOne.refrence != null)
				        				field = nextOne.refrence.view(new Double(field));
				        			if(nextOne.editable){
				        				tb = new TextBox();
				        				tb.setWidth("100");
				        				tb.setText(field);
				        				tb.setName(nextOne + "&%&" + currentId + "&%&" + field);
				        				tb.addBlurHandler(base);
					        			if(nextOne.hidden)
					        				continue;
				        				dataFtbl.setWidget(i + 1 , j , tb);
				        			}
				        			else if(nextOne.action != null){
				        				HTML html = new HTML("<font color= blue><u>"+ field + "</u></font>");
				        				html.setStyleName("handMouse");
				        				final String CURRENT_ID = currentId;
				        				final DataHeader NEXT_ONE = nextOne;
				        				html.addClickHandler(new ClickHandler(){
											@Override
											public void onClick(ClickEvent event) {
												NEXT_ONE.action.onDataClicked(Integer.parseInt(CURRENT_ID));
											}});

				        				dataFtbl.setWidget(i + 1 ,j, html);
				        			}
				        			else{
					        			if(nextOne.hidden)
					        				continue;
				        				dataFtbl.setWidget(i + 1 ,j, new Label(field));
				        			}

				        		  }
			        			if(canDelete){
			        				Button close = new Button("X");
			        				close.setStyleName("deleteButton");
			        				final int II = i +1;
			        				final String CURRENT_ID = currentId;
			        				close.addClickHandler(new ClickHandler(){
			        					final int S = II;
			        					final String ID = CURRENT_ID;
			        					@Override
			        					public void onClick(ClickEvent event) {
			        						deleteRecord(S, ID);
			        					}
			        				} );
			        				dataFtbl.setWidget(i+1, j, close);
			        			}
			        			if(i % 2 == 1 )
			        				dataFtbl.getRowFormatter().setStyleName(i + 1 , "evenRow");
			        			else
			        				dataFtbl.getRowFormatter().setStyleName(i + 1 , "oddRow");

				        	  }
				        	
				        	wating.hide();
				        	onFinishLoading();
		        	  	}
		        	  	catch(Exception e){
		        	  		System.out.println(response.getText());
		        	  		okDlg.center(LOADING_ERROR +2);
		        	  	}
		          }
		          else {
		        	  okDlg.center(LOADING_ERROR+3);
		        	  //Window.alert("3");
			        }
		        }
	      });
		}catch(RequestException ex){
			okDlg.center(LOADING_ERROR+4);
			//Window.alert("4");
		}

	}
	public void onFinishLoading(){}
	public void refresh(){
		dataFtbl.clear();
		buildHeader();
		buildBody();
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
			postData.append("field").append("=").append(field);
			postData.append("&");
			postData.append("id").append("=").append(ID);
			postData.append("&");
			postData.append("idValue").append("=").append(id);
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
				okDlg.center(EDITING_EROR);
				tb.setFocus(true);
				tb.selectAll();
			}
		}//end of Big if
		
	}
	
	public void deleteRecord(final int row, String idComplain){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=removing");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");

		StringBuffer postData = new StringBuffer();
		postData.append("id").append("=").append(ID);
		postData.append("&");
		postData.append("idValue").append("=").append(idComplain);
		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(REMOVING_ERROR);
		        	}
	
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  	if(response.getText().trim().equals("success")){
		        	  		dataFtbl.removeRow(row);
		        	  		refresh();
		        	  	}
		        	  	else{
		        	  		okDlg.center(REMOVING_ERROR);
		        	  	}	
		          }
		          else {
		        	  okDlg.center(REMOVING_ERROR);
			        }
		        }
	      });
		}catch(RequestException ex){
			okDlg.center(REMOVING_ERROR);
		}

	}
}
