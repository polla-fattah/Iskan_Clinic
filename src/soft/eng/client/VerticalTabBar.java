package soft.eng.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VerticalTabBar extends VerticalPanel {
	private ToggleButton pushed;
	private ChangeButtons change;
	private VerticalTabBar me;
	
	public VerticalTabBar(){
		pushed = null;
		change = new ChangeButtons();
		me = this;
		this.setStyleName("verticalTabBar");

	}
	
	public void addTab(String txt){
		ToggleButton tb = new ToggleButton(txt, change);
		if(pushed == null){
			tb.setDown(true);
			pushed = tb;
		}
		super.add(tb);
	}
	
	@Deprecated
	@Override public void add(Widget w){
	}
	public void insertTab(String txt, int index){
		ToggleButton tb = new ToggleButton(txt, change);
		super.insert(tb, index);
	}
	@Deprecated
	@Override public void insert(Widget txt, int index){
	}
	
	public void setEnabled(int index, boolean flag){
		ToggleButton tb = (ToggleButton) this.getWidget(index);
		tb.setEnabled(flag);
	}
	
	public boolean isEnabled(int index){
		ToggleButton tb = (ToggleButton) this.getWidget(index);
		return tb.isEnabled();
	}

	
	public int getSelectedIndex(){
		return getWidgetIndex(pushed);
	}
	
	public ToggleButton getSelected(){
		return pushed;
	}
	
	public void onClick(){
		
	}
	
	public void select(int index){
		pushed.setDown(false);
		pushed = (ToggleButton) this.getWidget(index);
		pushed.setDown(true);
	}
	
	public boolean isSelected(int index){
		ToggleButton temp = (ToggleButton) this.getWidget(index);
		return (pushed == temp);
	}
	private class ChangeButtons  implements ClickHandler{
		@Override public void onClick(ClickEvent event){
			pushed.setDown(false);
			pushed = (ToggleButton) event.getSource();
			pushed.setDown(true);
			me.onClick();
		}
	}
}
