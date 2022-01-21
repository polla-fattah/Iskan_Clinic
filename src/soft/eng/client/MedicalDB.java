package soft.eng.client;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class MedicalDB implements EntryPoint{
	private static OkDialogBox okDlg= new OkDialogBox("Connection Error", "");

	
	private Login login;
	
	private FlexTable mainPanle;
	private Header header;
	private FlexTable body;
	private Footer footer;
	private MainActions mainActions;

	public void onModuleLoad(){
		login = new Login(){
			@Override
			public void onAuthenticationSucceeded(){
				login.hide();
				DecoratedPopupPanel d = new DecoratedPopupPanel(false,true);
				d.add(new Label("Please Wait ..."));
				d.center();
				header = new Header();
				footer = new Footer();
				mainActions = new MainActions();

				//MAIN BODY
				body = new FlexTable();
				body.setSize("100%", "100%");
				body.setStyleName("body");
				body.setCellPadding(0);
				body.setCellSpacing(0);
				body.setWidget(0, 0, mainActions);

				//END OF BODY

				//THE MAIN PANEL
				mainPanle = new FlexTable();
				mainPanle.setSize("100%", "100%");
				mainPanle.setBorderWidth(0);
				mainPanle.setCellPadding(0);
				mainPanle.setCellSpacing(0);
				mainPanle.setWidget(0, 0, header);
				mainPanle.setWidget(1, 0, body);
				mainPanle.setWidget(2, 0, footer);
				mainPanle.getCellFormatter().setHeight(1, 0, "100%");
				//END OF MAIN PANEL
				d.hide();
				RootPanel.get().add( mainPanle);

			}
			public void onAuthenticationFailed(){
				login.hide();
				okDlg = new OkDialogBox("Login Failed", "Username/password combination is wrong."){
					@Override
					public void onOk(){
						login.show();
					}
				};
				okDlg.center();
				
			}
		};
		login.center();
		login.submitBtn.click();
	}
}