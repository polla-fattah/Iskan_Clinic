package soft.eng.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ToggleButton;


public class StatusForm extends FlexTable {
	private final String SERVICE_URL = Security.server + "case.php?key=" + Security.key + "&userName=" + Security.userName;
	private final String UPDATING_EROR 	= "Couldn't update case on the server. Causes<br>1- Server Down."+
	"<br>2- Connection Lost.<br>3- You Entered Wrong Value.<br>Try Again, Please!";
	private int caseId;
	private ToggleButton statusTgBtn;
	private final OkDialogBox okDlg;
	private PopupPanel wating;
	public StatusForm(){
		HTML text = new HTML("<b><font color = green>Case Status is </b>");
		statusTgBtn = new ToggleButton("Active", "Closed");
		
		okDlg = new OkDialogBox("Error","");
		
		wating = new PopupPanel();
		wating.add(new HTML("<b>Wait Please ...</b>"));		
		
		statusTgBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				wating.center();
				String currentStatus =statusTgBtn.isDown() ? "0" : "1";
				RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + 
						"&require=statusUpdate&idCase="+caseId+ "&currentStatus="+currentStatus);
				
				try{
					builder.sendRequest(null, new RequestCallback() {
				        public void onError(Request request, Throwable exception){
				        	wating.hide();
				        	statusTgBtn.setDown(!statusTgBtn.isDown());
				        	okDlg.center(UPDATING_EROR);
				        }

				        public void onResponseReceived(Request request, Response response) {
				          if (200 == response.getStatusCode()) {
			        	  	if(response.getText().trim().equalsIgnoreCase("success")){
					        	wating.hide();
			        	  	}
			        	  	else{
					        	statusTgBtn.setDown(!statusTgBtn.isDown());
			        	  		okDlg.center(UPDATING_EROR);
			        	  	}
				          }
				          else {
					        	statusTgBtn.setDown(!statusTgBtn.isDown());
					        	okDlg.center(UPDATING_EROR);
					        }
				        }
			      });
				}catch(RequestException ex){
					wating.hide();
		        	statusTgBtn.setDown(!statusTgBtn.isDown());
					okDlg.center(UPDATING_EROR);
				}
			}
			
		});
		
		FlexTable content = new FlexTable();
		content.setWidget(0, 0, text);
		content.setWidget(0, 1, statusTgBtn);
		
		this.setWidget(0, 0, new HTML(" "));
		this.setWidget(0, 1, new HTML(" "));
		this.setWidget(1, 0, new HTML(" "));
		this.setWidget(1, 1, content);
		this.setWidget(2, 1, new HTML(" "));
		
		this.setSize("100%", "100%");
		this.getCellFormatter().setHeight(0, 0, "100");
		this.getFlexCellFormatter().setWidth(0, 0, "300");
		this.getFlexCellFormatter().setHeight(0, 0, "100%");

	}
	public void updatedStatus(String jsonStr){
		JSONObject jsonObje = (JSONObject)JSONParser.parse(jsonStr);
		caseId = Integer.parseInt(jsonObje.get("idCase").isString().stringValue());
		statusTgBtn.setDown(!jsonObje.get("status").isString().stringValue().equals("1"));
	}
	
}
