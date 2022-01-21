package soft.eng.client;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;

public class Main extends DecoratedTabPanel {
	public Main(CurrentCase currentCase, MainActions mainActions){
		final ActiveCases activeCases = new ActiveCases(currentCase, mainActions);
		this.add(new NewCase(), "New Case");
		this.add(new NewPateint(), "New Pateint");
		this.add(activeCases, "Active Cases");
		this.add(new PateintSearch(currentCase, mainActions), "Search For Pateint");
		this.selectTab(0);
		
		this.addSelectionHandler(new SelectionHandler<Integer>(){

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if(event.getSelectedItem() == 2)
					activeCases.refresh();
			}});
	}
}
