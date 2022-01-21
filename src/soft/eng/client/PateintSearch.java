package soft.eng.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class PateintSearch extends FlexTable{
	
	private DataTable dataTable;
	private CurrentCase currentCase;
	private TextBox pateintIdTbx = new TextBox();
	private Button go = new Button("Go");
	
	public PateintSearch(final CurrentCase currentCase, final MainActions mainActions){
		this.currentCase = currentCase;
		
		dataTable = new DataTable("`Case`", "idCase");
		dataTable.addHeader(new DataHeader("idCase", "Case ID",null,true,true,null,true));
		dataTable.addHeader(new DataHeader("addmitionDate", "Addmition Date",null,new OnAction()));
		dataTable.addHeader(new DataHeader("chiefComplain", "chiefComplain"));
		dataTable.addHeader(new DataHeader("bedNo", "Bed No"));
		dataTable.addHeader(new DataHeader("idDoctor", "Doctor ID",null,true,true,null,true));
		dataTable.setWidth("500");
		FlexTable search = new FlexTable();
		search.setWidget(0, 0, new HTML(""));
		search.setWidget(0, 1, pateintIdTbx);
		search.setWidget(0, 2, go);
		
		this.setWidget(0, 0, new HTML(" "));
		this.setWidget(0, 1, new HTML(" "));
		this.setWidget(1, 0, new HTML(" "));
		this.setWidget(1, 1, search);
		this.setWidget(2, 0, new HTML(" "));
		this.setWidget(2, 1, dataTable);
		this.setWidget(3, 0, new HTML(" "));
		this.setWidget(3, 1, new HTML(" "));
		
		go.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				String numStr = pateintIdTbx.getText().trim();
				try{
					Integer.parseInt(numStr);
				}
				catch(Exception e){
					e.printStackTrace();
					return;
				}
				dataTable.addFilter(new DataFilter("idPatient", numStr));
				dataTable.refresh();
			}
		});
	}
	private class OnAction implements ActionedData{
		@Override
		public void onDataClicked(int idData) {
			currentCase.loadNewCase(idData);
		}
		
	}
}
