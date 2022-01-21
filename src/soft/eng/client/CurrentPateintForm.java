package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class CurrentPateintForm extends FlexTable{
	private DBForm dbForm = new DBForm("patient", "idPatient");
	private String pateintId;
	public CurrentPateintForm(){
	
		dbForm.addHeader(new DataHeader("idPatient", "", null, false,false, ""));
		dbForm.addHeader(new DataHeader("name", "Name", null, true));
		dbForm.addHeader(new DataHeader("gender", "Gender", null, true));
		dbForm.addHeader(new DataHeader("religion", "Religion", null, true));
		dbForm.addHeader(new DataHeader("address", "Address", null, true));
		dbForm.addHeader(new DataHeader("occupation", "Occupation", null, true));
		dbForm.addHeader(new DataHeader("maritalStatus", "Marital Status", null, true));
		dbForm.addHeader(new DataHeader("education", "Education", null, true));
		dbForm.addHeader(new DataHeader("houseStatus", "House Status", null, true));
		dbForm.addHeader(new DataHeader("diet", "Diet", null, true));
		dbForm.addHeader(new DataHeader("economicState", "Economic State", null, true));
		dbForm.addHeader(new DataHeader("alchoholTaking", "Alchohol Taking", null, true));
		dbForm.addHeader(new DataHeader("smoking", "Smoking", null, true));
		dbForm.addHeader(new DataHeader("domesticAnimal", "Domestic Animal", null, true));
		dbForm.addHeader(new DataHeader("travelAbroad", "Travel Abroad", null, true));


	this.setWidget(0, 0, dbForm);
	this.setWidget(0, 1, new HTML(""));
	this.setWidget(1, 0, new HTML(""));
	this.setWidget(1, 1, new HTML(""));
	
	this.getCellFormatter().setWidth(0, 0, "400");	
	}
	public String update(String jsonStr){
		pateintId = dbForm.loadForm(jsonStr);
		return pateintId;
	}
}
