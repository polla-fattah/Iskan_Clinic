package soft.eng.client;

import com.google.gwt.user.client.ui.HTML;


public class MainActions extends VerticalTabPanel {
	private Systems stm;
	private Complains comp;
	private final MainActions base = this;
	public MainActions(){
		setTabBarSize("100%", "175");

		stm = new Systems(){
			@Override
			public void onFinishLoading(){
				comp = new Complains(){
					@Override
					public void onFinishLoading(){
						CurrentCase currentCase = new CurrentCase(stm, comp, base);
						addPanel("Main",new Main(currentCase, base));
						addPanel("Current Case",currentCase);
						addPanel("About",new HTML("This project is created as a requiremnt for completing Highre Deploma Degree"));
					}
				};
			}
		};//end of System stm
	}
}




/*		if(Security.userName.equalsIgnoreCase("administrator")){

modelLst = new Models(){
	@Override public void onFinishLoading(){
		brandLst = new Brands(this){
			@Override public void onFinishLoading(){
				typeLst = new Types(this){
					@Override public void onFinishLoading(){
						categoryLst = new Categories(this){
							@Override public void onFinishLoading(){
								setTabBarSize("100%", "175");
								addPanel("Management",new Managements(categoryLst, typeLst, brandLst, modelLst));
							}
						};
					}
				};
			}
		};
	}
};

}
else{
modelLst = new Models(){
	@Override public void onFinishLoading(){
		brandLst = new Brands(this){
			@Override public void onFinishLoading(){
				typeLst = new Types(this){
					@Override public void onFinishLoading(){
						categoryLst = new Categories(this){
							@Override public void onFinishLoading(){
								setTabBarSize("100%", "175");
								addPanel("Invoices",new Invoices(categoryLst, typeLst, brandLst, modelLst));
								addPanel("Search & Report",new SearchAndReport());
								addPanel("Project",new Project());
							}
						};
					}
				};
			}
		};
	}
};

}
*/
