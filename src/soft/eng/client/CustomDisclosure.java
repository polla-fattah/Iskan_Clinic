package soft.eng.client;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import soft.eng.client.Complains.Complain;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class CustomDisclosure extends Composite{
	private DisclosurePanel mainPanel = new DisclosurePanel();
	private FlexTable layoutTbl = new FlexTable();
	private FlexTable addComplainTbl = new FlexTable();
	private FlexTable complainsTbl = new FlexTable();

	private final String ADDING_EROR = "Couldn't add new item to the Server. Causes<br>1- Server Down.<br>2- Connection Lost.<br>3- This Catigory Actually Existes.<br>Try Again, Please!";

	private Button addBtn = new Button("Add");
	private TextBox noteTbx = new TextBox();
	private ListBox complainLst = new ListBox(false);
	private OkDialogBox okDlg = new OkDialogBox("Error", "");

	private Systems systems;
	
	private int size = 0;
	private final String SERVICE_URL = Security.server + "case.php?key=" + Security.key + "&userName=" + Security.userName;
	private int idSystem;
	public CustomDisclosure(int idSystem, Systems stm, Complains comp){
		systems = stm;
		this.idSystem = idSystem;
		TreeSet<Complain> thisComplain = comp.getSystemChildren(idSystem);
		Complain co;
		
		if(thisComplain != null){
			for(Iterator<Complain> it = thisComplain.iterator(); it.hasNext();){
				co = it.next();
				complainLst.addItem(co.getName(), ""+co.getId());
			}
		}
		addBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				addComplain();
			}
		});
		addComplainTbl.setWidget(0, 0, complainLst);
		addComplainTbl.setWidget(0, 1, noteTbx);
		addComplainTbl.setWidget(0, 2, addBtn);
		
		layoutTbl.setWidget(0, 0, addComplainTbl);
		layoutTbl.setWidget(1, 0, complainsTbl);
		
		mainPanel.setAnimationEnabled(true);
		mainPanel.setHeader(new HTML(systems.view(idSystem)));
		mainPanel.add(layoutTbl);
		mainPanel.setSize("90%", "90%");
		
		initWidget(mainPanel);
	}
	public void addComplain(){
		final String note = noteTbx.getText();

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=adding");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");


		StringBuffer postData = new StringBuffer();
		postData.append("idComplain").append("=").append(complainLst.getValue(complainLst.getSelectedIndex()));
		postData.append("&");
		postData.append("idSystem").append("=").append(idSystem);
		postData.append("&");
		postData.append("idCase").append("=").append(CurrentCase.currentCase.idCase);
		postData.append("&");
		postData.append("note").append("=").append(URL.encode(note));
		try{
			builder.sendRequest(postData.toString(), new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        		okDlg.center(ADDING_EROR);
		        	}
	
		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
		        	  	if(response.getText().trim().equals("success")){
		        	  		addToComplainTbl(new Complains.Complain(Integer.parseInt(complainLst.getValue(complainLst.getSelectedIndex()))
		        	  												, complainLst.getItemText(complainLst.getSelectedIndex()), idSystem, note));
		        	  		noteTbx.setText("");
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

	public void removeComplain(final int row, int idComplain){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=removing");
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");

		StringBuffer postData = new StringBuffer();
		postData.append("idComplain").append("=").append(idComplain);
		postData.append("&");
		postData.append("idCase").append("=").append(CurrentCase.currentCase.idCase);
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

	public void updateExistingComplains(ArrayList<Complains.Complain>currenComplains){
		Complains.Complain temp;
		if(currenComplains != null){
			for(Iterator<Complains.Complain> it = currenComplains.iterator(); it.hasNext();){
				temp = it.next();
				addToComplainTbl(temp);
			}
			mainPanel.setOpen(true);
		}
		else{
			complainsTbl.clear();
			mainPanel.setOpen(false);
		}
	}

	private void addToComplainTbl(final Complains.Complain compl){
		Button close = new Button("X");
		//close.set
		close.setStyleName("deleteButton");
		//close.setTitle("" + size);
		close.addClickHandler(new ClickHandler(){
			final int s = size;
			@Override
			public void onClick(ClickEvent event) {
				removeComplain(s, compl.getId());
			}
		} );
		
		complainsTbl.setWidget(size, 0, close);
		complainsTbl.setText(size, 1, compl.getName());
		complainsTbl.setText(size, 2, ": " + compl.getNote());
		size++;
	}
	
}
