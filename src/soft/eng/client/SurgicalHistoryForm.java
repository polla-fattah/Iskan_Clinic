package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class SurgicalHistoryForm extends FlexTable{
	private FormTable surgical = new FormTable("surgicalhistory", "idSurgicalHistory");
	
	public SurgicalHistoryForm(){
		surgical.addHeader(new DataHeader("idSurgicalHistory", "", null, true, true, null, false));
		surgical.addHeader(new DataHeader("name", "Name ", null, true));
		surgical.addHeader(new DataHeader("type", "Type ", null, true));
		
		this.setWidget(0, 0, surgical);
		this.setWidget(0, 1, new HTML(""));
		this.setWidget(1, 0, new HTML(""));
		this.setWidget(1, 1, new HTML(""));
		
		this.getCellFormatter().setWidth(0, 0, "500");	
	}
	public void update(String pateintId){
		surgical.addFilter(new DataFilter("Patient_idPatient", pateintId));
		surgical.addHeader(new DataHeader("Patient_idPatient", "", null, false, true, pateintId));
		surgical.load();
	}
}

