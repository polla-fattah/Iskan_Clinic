package soft.eng.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;

public class OkDialogBox extends DialogBox{
	private Button okBtn;
	private HTML message;
	
	public void onOk(){
		this.setText("Error!");
	}
	
	private void createFrame(){
		FlexTable table = new FlexTable();
		table.setSize("100%", "100%");
		table.setCellPadding(3);
		table.setCellSpacing(4);
		message = new HTML("Yes Or No");
		
		okBtn = new Button ("Ok");
		okBtn.setSize("40", "30");
		okBtn.setFocus(true);
		okBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				hide();
				onOk();
			}});
		

		table.setWidget(0, 0,message);
		table.getFlexCellFormatter().setColSpan(0, 0, 2);
		
		table.setWidget(1, 0, okBtn);
		table.setWidget(1, 1, new Label(""));
		
		table.getCellFormatter().setAlignment(1, 0, HasAlignment.ALIGN_RIGHT, HasAlignment.ALIGN_MIDDLE);
		table.getCellFormatter().setAlignment(1, 1, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
		table.getCellFormatter().setAlignment(1, 2, HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_MIDDLE);
		
		this.add(table);
	}
	public OkDialogBox(){
		super(false, true);
		createFrame();
	}
	
	public OkDialogBox(String title, String ques){
		super(false, true);
		createFrame();
		message.setHTML(ques);
		this.setText(title);
	}
	
	public void show(String text){
		this.message.setHTML(text);
		this.show();
		okBtn.setFocus(true);
	}
	
	public void center(String text){
		this.message.setHTML(text);
		this.center();
		okBtn.setFocus(true);
	}
}
