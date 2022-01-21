package soft.eng.client;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class NewPateint extends FlexTable{
	private OkDialogBox okDlg;

	public NewPateint(){
		okDlg = new OkDialogBox("Information", "");
		DBForm personalInfo = new DBForm("patient", "idPatient"){
			@Override
			public void onFinishAdding(Response message){
				okDlg.center("New Pateint ID is : " + message.getText());
			}
		};
		personalInfo.addHeader(new DataHeader("name", "Name", null, true));
		personalInfo.addHeader(new DataHeader("gender", "Gender", null, true));
		personalInfo.addHeader(new DataHeader("religion", "Religion", null, true));
		personalInfo.addHeader(new DataHeader("address", "Address", null, true));
		personalInfo.addHeader(new DataHeader("occupation", "Occupation", null, true));
		personalInfo.addHeader(new DataHeader("maritalStatus", "Marital Status", null, true));
		personalInfo.addHeader(new DataHeader("education", "Education", null, true));
		personalInfo.addHeader(new DataHeader("houseStatus", "House Status", null, true));
		personalInfo.addHeader(new DataHeader("diet", "Diet", null, true));
		personalInfo.addHeader(new DataHeader("economicState", "Economic State", null, true));
		personalInfo.addHeader(new DataHeader("alchoholTaking", "Alchohol Taking", null, true));
		personalInfo.addHeader(new DataHeader("smoking", "Smoking", null, true));
		personalInfo.addHeader(new DataHeader("domesticAnimal", "Domestic Animal", null, true));
		personalInfo.addHeader(new DataHeader("travelAbroad", "Travel Abroad", null, true));
		personalInfo.createForm();
		this.setWidget(0, 0, personalInfo);
		this.setWidget(0, 1, new HTML(""));
		this.setWidget(1, 0, new HTML(""));
		this.setWidget(1, 1, new HTML(""));
		
		this.getCellFormatter().setWidth(0, 0, "400");
	}
}
