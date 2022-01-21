package soft.eng.client;

import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class CurrentCase extends DecoratedTabPanel {
	private final String SERVICE_URL = Security.server + "case.php?key=" + Security.key + "&userName=" + Security.userName;
	private final String LOADING_ERROR 	= "Couldn't load Case. Try Again, Please!";

	private final OkDialogBox okDlg;
	private PopupPanel wating;
	private SystemReview systemReview;
	
	private CurrentPateintForm currentPateintFRM = new CurrentPateintForm();
	private CurrentCaseForm currentCaseFRM = new CurrentCaseForm();
	private CurrentAllergyForm currentAllergyFrm = new CurrentAllergyForm();
	private FamilyHistoryForm familyHistoryFrm = new FamilyHistoryForm();
	private SurgicalHistoryForm surgicalHistoryFrm = new SurgicalHistoryForm();
	private StatusForm statusFrm = new StatusForm();
	
	private Systems systems;
	private Complains complains;
	private MainActions mainActions;
	
	public static Case currentCase;
	public static Patient currentPatient; 
	public static TreeMap<Integer , ArrayList<Complains.Complain>>currenComplains;
	
	public CurrentCase(Systems stm, Complains comp, MainActions mainActions){
		this.mainActions = mainActions;
		systems = stm;
		complains = comp;
		okDlg = new OkDialogBox("Connection Error", LOADING_ERROR);
		systemReview = new SystemReview(stm, comp);

		this.add( currentPateintFRM, "Pateint");
		this.add(currentCaseFRM, "Case Detail");
		this.add(systemReview , "System Review");
		this.add(currentAllergyFrm , "Allergies");
		this.add(familyHistoryFrm , "Family History");
		this.add(surgicalHistoryFrm , "Surgical History");
		this.add(statusFrm , "Status");
		
		this.selectTab(0);
		wating = new PopupPanel();
		wating.add(new HTML("<b>Wait Please ...</b>"));
	}
	
	public void loadNewCase(int caseId){
		wating.center();
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, SERVICE_URL + "&require=loading&idCase="+caseId);
		try{
			builder.sendRequest(null, new RequestCallback() {
		        public void onError(Request request, Throwable exception){
		        	wating.hide();
		        	okDlg.center(LOADING_ERROR);
		        }

		        public void onResponseReceived(Request request, Response response) {
		          if (200 == response.getStatusCode()) {
	        	  	try{
	        	  		String data[] = response.getText().trim().split("#2@!");
			        	currentCase = generateCase(data[0]);
			        	currenComplains = generateComplains(data[2]);
			        	systemReview.updateComplains(currenComplains);
			        	String paiteintId = currentPateintFRM.update(data[1]);
			        	currentCaseFRM.update(data[0]);
			        	currentAllergyFrm.update(paiteintId);
			        	surgicalHistoryFrm.update(paiteintId);
			        	familyHistoryFrm.update(paiteintId);
			        	statusFrm.updatedStatus(data[0]);
			        	
			        	wating.hide();
			        	selectTab(0);
			        	mainActions.select(1);
	        	  	}
	        	  	catch(Exception e){
	        	  		e.printStackTrace();
	        	  		okDlg.center(LOADING_ERROR);
	        	  	}
		          }
		          else {
		        	  okDlg.center(LOADING_ERROR);
			        }
		        }
	      });
		}catch(RequestException ex){
			wating.hide();
			okDlg.center(LOADING_ERROR);
		}

	}
	


	private Case generateCase(String caseStr){
		Case caseTemp = new Case();
		JSONObject jsonObje = (JSONObject)JSONParser.parse(caseStr);
		
		caseTemp.addmitionDate = jsonObje.get("addmitionDate").isString().stringValue();
		caseTemp.aggravatingFactor = jsonObje.get("aggravatingFactor").isString().stringValue();
		caseTemp.associatedSymptom = jsonObje.get("associatedSymptom").isString().stringValue();
		caseTemp.bedNo = jsonObje.get("bedNo").isString().stringValue();
		caseTemp.chiefComplain = jsonObje.get("chiefComplain").isString().stringValue();
		caseTemp.duration = jsonObje.get("duration").isString().stringValue();
		caseTemp.idCase = jsonObje.get("idCase").isString().stringValue();
		caseTemp.idDoctor = jsonObje.get("idDoctor").isString().stringValue();
		caseTemp.idPatient = jsonObje.get("idPatient").isString().stringValue();
		caseTemp.nature = jsonObje.get("nature").isString().stringValue();
		caseTemp.onset = jsonObje.get("onset").isString().stringValue();
		caseTemp.radiation = jsonObje.get("radiation").isString().stringValue();
		caseTemp.relievingFactor = jsonObje.get("relievingFactor").isString().stringValue();
		caseTemp.severity = jsonObje.get("severity").isString().stringValue();
		caseTemp.site = jsonObje.get("site").isString().stringValue();
		return caseTemp;
	}
	private Patient generatePatient(String patientStr){
		Patient patientTemp = new Patient();
		JSONObject jsonObje = (JSONObject)JSONParser.parse(patientStr);
		
		patientTemp.address = jsonObje.get("address").isString().stringValue();
		patientTemp.alchoholTaking = jsonObje.get("alchoholTaking").isString().stringValue();
		patientTemp.diet = jsonObje.get("diet").isString().stringValue();
		patientTemp.domesticAnimal = jsonObje.get("domesticAnimal").isString().stringValue();
		patientTemp.economicState = jsonObje.get("economicState").isString().stringValue();
		patientTemp.education = jsonObje.get("education").isString().stringValue();
		patientTemp.gender = jsonObje.get("gender").isString().stringValue();
		patientTemp.houseStatus = jsonObje.get("houseStatus").isString().stringValue();
		patientTemp.idPatient = jsonObje.get("idPatient").isString().stringValue();
		patientTemp.maritalStatus = jsonObje.get("maritalStatus").isString().stringValue();
		patientTemp.name = jsonObje.get("name").isString().stringValue();
		patientTemp.occupation = jsonObje.get("occupation").isString().stringValue();
		patientTemp.religion = jsonObje.get("religion").isString().stringValue();
		patientTemp.smoking = jsonObje.get("smoking").isString().stringValue();
		patientTemp.travelAbroad = jsonObje.get("travelAbroad").isString().stringValue();
		return patientTemp;
	}
	
	private TreeMap<Integer , ArrayList<Complains.Complain>> generateComplains(String compliansStr){
		
		TreeMap<Integer , ArrayList<Complains.Complain>> temp = new TreeMap<Integer , ArrayList<Complains.Complain>>();
		JSONArray array = (JSONArray)JSONParser.parse(compliansStr);
		int idSystem = 0, idComplain = 0;
		Complains.Complain comp;
		JSONObject obj;
		for(int i = 0 ; i < array.size(); i++){
			obj = array.get(i).isObject();
			comp = new Complains.Complain();
			
			idComplain = Integer.parseInt(obj.get("idComplain").isString().stringValue());
			idSystem = Integer.parseInt(obj.get("idSystem").isString().stringValue());			
			comp.setId(idComplain);
			comp.setName(complains.view(idComplain));
			comp.setNote(obj.get("notes").isString().stringValue());
			
			if(!temp.containsKey(idSystem))
				temp.put(idSystem, new ArrayList<Complains.Complain>());
			temp.get(idSystem).add(comp);
		}
		return temp;
	}
}
