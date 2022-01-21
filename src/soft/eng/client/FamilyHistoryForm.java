
package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class FamilyHistoryForm extends FlexTable{
	private FormTable history = new FormTable("familyhistory","idFamilyHistory");
	
	public FamilyHistoryForm(){
		history.addHeader(new DataHeader("idFamilyHistory", "", null, true, true, null, false));
		history.addHeader(new DataHeader("type", "Type", null, true));
		history.addHeader(new DataHeader("description", "Description", null, true));
		
		this.setWidget(0, 0, history);
		this.setWidget(0, 1, new HTML(""));
		this.setWidget(1, 0, new HTML(""));
		this.setWidget(1, 1, new HTML(""));
		
		this.getCellFormatter().setWidth(0, 0, "500");	
	}
	public void update(String pateintId){
		history.addFilter(new DataFilter("idPatient", pateintId));
		history.addHeader(new DataHeader("idPatient", "", null, false, true, pateintId));
		history.load();
	}
}

