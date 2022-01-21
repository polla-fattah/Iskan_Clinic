
package soft.eng.client;



import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
@Deprecated
public class Alergy extends FlexTable{
	private FlexTable addComplainTbl = new FlexTable();
	private FlexTable complainsTbl = new FlexTable();

	private final String ADDING_EROR = "Couldn't add new item to the Server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>3- This Catigory Actually Existes.<br>Try Again, Please!";

	private Button addBtn = new Button("Add");
	private TextBox nameTbx = new TextBox();
	private OkDialogBox okDlg = new OkDialogBox("Error", "");
	
	private int size = 0;
	private final String SERVICE_URL = Security.server + "case.php?key=" + Security.key + "&userName=" + Security.userName;
	private int idPatient;
	public Alergy(int idPatient){
		this.idPatient = idPatient;
		
		addBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				addProperity();
			}
		});
		addComplainTbl.setWidget(0, 0, nameTbx);
		addComplainTbl.setWidget(0, 1, addBtn);
		
		this.setWidget(0, 0, addComplainTbl);
		this.setWidget(1, 0, complainsTbl);
		
	}
	public void addProperity(){
		final String name = nameTbx.getText();

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=adding");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");


		StringBuffer postData = new StringBuffer();
		postData.append("idSystem").append("=").append(idPatient);
		postData.append("&");

		postData.append("name").append("=").append(URL.encode(name));
		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(ADDING_EROR);
		        	}
	
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  	if(response.getText().trim().equals("success")){
		        	  		addToComplainTbl(nameTbx.getText());
		        	  		nameTbx.setText("");
		        	  	}
		        	  	else if (response.getText().trim().equals("1062")){
		        	  		okDlg.center("This Complain is duplicated");
		        	  	}
		        	  	else{
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

	public void removeComplain(final int row, String n){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=removing");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");

		StringBuffer postData = new StringBuffer();
		postData.append("name").append("=").append(n);
		postData.append("&");
		postData.append("idPatient").append("=").append(CurrentCase.currentCase.idCase);
		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(ADDING_EROR);
		        	}
	
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  	if(response.getText().trim().equals("success")){
		        	  		complainsTbl.removeRow(row);
		        	  	}
		        	  	else{
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

	public void updateExistingProperities(ArrayList<String>currenProperities){
		if(currenProperities != null){
			for(Iterator<String> it = currenProperities.iterator(); it.hasNext();){
				addToComplainTbl(it.next());
			}
		}
		else{
			complainsTbl.clear();
		}
	}

	private void addToComplainTbl(final String n){
		Button close = new Button("X");
		//close.set
		close.setStyleName("deleteButton");
		//close.setTitle("" + size);
		close.addClickHandler(new ClickHandler(){
			final int s = size;
			@Override
			public void onClick(ClickEvent event) {
				removeComplain(s, n);
			}
		} );
		
		complainsTbl.setWidget(size, 0, close);
		complainsTbl.setText(size, 1, n);
		size++;
	}
}

