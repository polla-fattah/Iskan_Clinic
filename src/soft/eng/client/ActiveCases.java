package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class ActiveCases extends FlexTable{
	
	private DataTable dataTable;
	private CurrentCase currentCase;

	public ActiveCases(final CurrentCase currentCase, final MainActions mainActions){
		this.currentCase = currentCase;
		
		dataTable = new DataTable("`Case`", "idCase");
		dataTable.addHeader(new DataHeader("idCase", "Case ID",null,true,true,null,true));
		dataTable.addHeader(new DataHeader("bedNo", "Bed No",null,new OnAction()));
		dataTable.addHeader(new DataHeader("chiefComplain", "chiefComplain"));
		dataTable.addHeader(new DataHeader("addmitionDate", "Addmition Date"));
		dataTable.addHeader(new DataHeader("idDoctor", "Doctor ID",null,true,true,null,true));
		dataTable.addFilter(new DataFilter("status", "1"));
		dataTable.setWidth("500");
		dataTable.refresh();
		this.setWidget(0, 0, new HTML(" "));
		this.setWidget(0, 1, new HTML(" "));
		this.setWidget(2, 0, new HTML(" "));
		this.setWidget(2, 1, dataTable);
		this.setWidget(3, 0, new HTML(" "));
		this.setWidget(3, 1, new HTML(" "));
		
	}
	public void refresh() {
		dataTable.refresh();
	}
	private class OnAction implements ActionedData{
		@Override
		public void onDataClicked(int idData) {
			currentCase.loadNewCase(idData);
		}
		
	}

}
