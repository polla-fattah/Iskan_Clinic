package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class NewCase extends FlexTable {
	NewCase(){
		DBForm caseInfo = new DBForm("case", "idCase");
		caseInfo.addHeader(new DataHeader("bedNo", "Bed No", null, true));
		caseInfo.addHeader(new DataHeader("addmitionDate", "Addmition Date", null, true));
		caseInfo.addHeader(new DataHeader("chiefComplain", "Chief Complain", null, true));
		caseInfo.addHeader(new DataHeader("duration", "Duration", null, true));
		caseInfo.addHeader(new DataHeader("onset", "Onset", null, true));
		caseInfo.addHeader(new DataHeader("site", "Site", null, true));
		caseInfo.addHeader(new DataHeader("severity", "Severity", null, true));
		caseInfo.addHeader(new DataHeader("nature", "Nature", null, true));
		caseInfo.addHeader(new DataHeader("radiation", "Radiation", null, true));
		caseInfo.addHeader(new DataHeader("aggravatingFactor", "Aggravating Factor", null, true));
		caseInfo.addHeader(new DataHeader("relievingFactor", "Relieving Factor", null, true));
		caseInfo.addHeader(new DataHeader("associatedSymptom", "Associated Symptom", null, true));
		caseInfo.addHeader(new DataHeader("idPatient", "Patient ID Card", null, true));
		caseInfo.addHeader(new DataHeader("idDoctor", "Doctor ID", null, true));

		caseInfo.createForm();
		this.setWidget(0, 0, caseInfo);
		this.setWidget(0, 1, new HTML(""));
		this.setWidget(1, 0, new HTML(""));
		this.setWidget(1, 1, new HTML(""));
		
		this.getCellFormatter().setWidth(0, 0, "400");
	}
}
 	 	 	 	 	 	 	 	 	