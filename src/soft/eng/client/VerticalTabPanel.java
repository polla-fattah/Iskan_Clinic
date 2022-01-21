package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class VerticalTabPanel extends DockPanel{
	private VerticalTabBar tabs = null;
	private DeckPanel bodies = null;
	private VerticalPanel fill;
	private FlexTable tabPanel;
	public VerticalTabPanel(){
		bodies = new DeckPanel();
		
		tabPanel = new FlexTable();
		tabPanel.setSize("100%", "100%");
		tabPanel.setCellPadding(0);
		tabPanel.setCellSpacing(0);
		
		bodies.setStyleName("verticalTabBody");
		bodies.setSize("100%", "100%");
		
		tabs = new VerticalTabBar(){
			public void onClick(){
				bodies.showWidget(getSelectedIndex());
				afterClicked();
			}
		};
		this.setStyleName("verticalTabBar");
		

		tabPanel.getCellFormatter().setHeight(0, 0, "33");
		tabPanel.getCellFormatter().setAlignment(0, 0, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
		tabPanel.setWidget(1, 0, tabs);
		
		
		fill = new VerticalPanel();
		fill.setSize("100%", "100%");
		tabPanel.setWidget(2,0,fill);
		tabPanel.getCellFormatter().setHeight(2, 0, "100%");
		
		add(this.tabPanel, DockPanel.WEST);
		add(this.bodies, DockPanel.EAST);

		setVerticalAlignment(HasAlignment.ALIGN_TOP);
		setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		
		setSize("100%", "100%");
		
		tabs.setWidth("100%");
		tabs.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
	}
	
	public void afterClicked(){
		
	}
	
	public void setTabBarSize(String leftHeight, String leftWidth)
	{
		setCellHeight(tabPanel, leftHeight);
		setCellWidth(tabPanel, leftWidth);
	}

	public void addPanel(String head, Widget tile)
	{
		bodies.add(tile);
		if(tabs.getSelected() == null)
			bodies.showWidget(0);
		tabs.addTab(head);
		
	}
	public int getSelectedIndex(){
		return tabs.getSelectedIndex();
	}
	
	public ToggleButton getSelected(){
		return tabs.getSelected();
	}
	
	public Widget getPanel(int index){
		if(index > -1 && index < tabs.getWidgetCount())
			return bodies.getWidget(index);
		else
			return null;
	}
	
	public Widget getActivePanel(){
		return getPanel(tabs.getSelectedIndex());
	}
	
	public boolean isTabSelected(int index){
		return (tabs.getSelectedIndex() == index);
	}
	
	public void select(int index){
		if(index > -1 && index < tabs.getWidgetCount()){
			bodies.showWidget(index);
			tabs.select(index);
		}
	}

}