package soft.eng.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.HasAlignment;

class Login extends DialogBox
{
	private static OkDialogBox okDlg= new OkDialogBox("Connection Error", "");
	private final String SERVICE_URL = Security.server + "login.php";
	private final String LOGIN_ERROR = "Can not login now.<br> Please check your connection and try agian";
	
	private FlexTable mainPanel = new FlexTable();
	
	private TextBox userNameTxt = new TextBox();
	private PasswordTextBox passwordPsw = new PasswordTextBox();
	
	public Button submitBtn = new Button("Submit"); 
	private Button clearBtn = new Button("Clear"); 

	public Login(){
		this.setText("Please Login");
		userNameTxt.setText("aza");
		passwordPsw.setText("123456");
		passwordPsw.setFocus(true);
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(submitBtn);
		buttons.add(new Label("  "));
		buttons.add(clearBtn);
		
		submitBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL );
				builder.setHeader("Content-type", "application/x-www-form-urlencoded");

				StringBuffer postData = new StringBuffer();
				postData.append("userName").append("=").append(URL.encode(userNameTxt.getText()));
				postData.append("&");
				postData.append("password").append("=").append(URL.encode(passwordPsw.getText()));

				try{
					builder.sendRequest(postData.toString(), new RequestCallback() {
				        public void onError(Request request, Throwable exception){
				        		OkDialogBox bb = new OkDialogBox();
				        		bb.center(exception.getMessage());
				        		okDlg.center(LOGIN_ERROR);
				        	}

				        public void onResponseReceived(Request request, Response response) {
				        	OkDialogBox bb = new OkDialogBox();
				        	bb.center(response.getText());
				        	if (200 == response.getStatusCode()) {
				        	  	try{
				        	  		String data[] = response.getText().trim().split("#2@!");
				        	  		Security.key = Double.parseDouble(data[0]);
				        	  		Security.spetiality = data[1];
				        	  		Security.fullName = data[2];
				        	  		Security.degree = data[3];
				        	  		Security.userName = userNameTxt.getText();
				        	  		Security.password = passwordPsw.getText();			
				        	  		onAuthenticationSucceeded();
				        	  	}
				        	  	catch(Exception ex){
				        	  		ex.printStackTrace();
				        	  		onAuthenticationFailed();
				        	  	}
				          }
				          else{
				        	  okDlg.center(LOGIN_ERROR);
					        }
				        }
			      });
				}catch(RequestException ex){
					okDlg.center(LOGIN_ERROR);
				}
			}

			});
		passwordPsw.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					submitBtn.click();
				}
			}
		});
		clearBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event){
				userNameTxt.setText("");
				passwordPsw.setText("");
			}});	
		mainPanel.setWidget(0, 0, new Label("User Name:  "));
		mainPanel.setWidget(0, 1, userNameTxt);
		mainPanel.setWidget(1, 0, new Label("Password:  "));
		mainPanel.setWidget(1, 1, passwordPsw);
		mainPanel.setWidget(2, 0, buttons);
		
		mainPanel.getFlexCellFormatter().setColSpan(2, 0, 2);
		mainPanel.getCellFormatter().setAlignment(2, 0, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
		
		this.add(mainPanel);
		
	}
	public void onAuthenticationSucceeded(){}
	public void onAuthenticationFailed(){}
}