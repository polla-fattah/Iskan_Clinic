package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class CurrentCaseForm extends FlexTable{
	private DBForm dbForm = new DBForm("case", "idCase");
	
	public CurrentCaseForm(){
	//	dbForm.addHeader(new Header("idCase", "Case ID", null, false));
		dbForm.addHeader(new DataHeader("bedNo", "Bed No", null, true));
		dbForm.addHeader(new DataHeader("addmitionDate", "Addmition Date", null, true));
		dbForm.addHeader(new DataHeader("chiefComplain", "Chief Complain", null, true));
		dbForm.addHeader(new DataHeader("duration", "Duration", null, true));
		dbForm.addHeader(new DataHeader("onset", "Onset", null, true));
		dbForm.addHeader(new DataHeader("site", "Site", null, true));
		dbForm.addHeader(new DataHeader("severity", "Severity", null, true));
		dbForm.addHeader(new DataHeader("nature", "Nature", null, true));
		dbForm.addHeader(new DataHeader("radiation", "Radiation", null, true));
		dbForm.addHeader(new DataHeader("aggravatingFactor", "Aggravating Factor", null, true));
		dbForm.addHeader(new DataHeader("relievingFactor", "Relieving Factor", null, true));
		dbForm.addHeader(new DataHeader("associatedSymptom", "Associated Symptom", null, true));
	//	dbForm.addHeader(new DataHeader("idPatient", "Patient ID", null, false));
	//	dbForm.addHeader(new DataHeader("idDoctor", "Doctor ID", null, false));


	this.setWidget(0, 0, dbForm);
	this.setWidget(0, 1, new HTML(""));
	this.setWidget(1, 0, new HTML(""));
	this.setWidget(1, 1, new HTML(""));
	
	this.getCellFormatter().setWidth(0, 0, "400");	
	}
	public void update(String jsonStr){
		dbForm.loadForm(jsonStr);
	}
}
