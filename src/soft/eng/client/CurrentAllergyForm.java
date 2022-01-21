package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class CurrentAllergyForm extends FlexTable{
	private FormTable allergy = new FormTable("allergy","idAllergy");
	
	public CurrentAllergyForm(){
		allergy.addHeader(new DataHeader("idAllergy", "", null, true, true, null, false));
		allergy.addHeader(new DataHeader("name", "Alergies", null, true));

		
		this.setWidget(0, 0, allergy);
		this.setWidget(0, 1, new HTML(""));
		this.setWidget(1, 0, new HTML(""));
		this.setWidget(1, 1, new HTML(""));
		
		this.getCellFormatter().setWidth(0, 0, "500");	
	}
	public void update(String pateintId){
		allergy.addFilter(new DataFilter("idPatient", pateintId));
		allergy.addHeader(new DataHeader("idPatient", "", null, false, true, pateintId));
		allergy.load();
	}
}

