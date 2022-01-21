package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class Footer extends FlexTable{
	public Footer(){
		this.setWidget(0, 0, new HTML("<span style='font-size:10px;' align='right'>Channge Password &nbsp;|&nbsp; Logout</span>"));
		this.getCellFormatter().setStyleName(0, 0, "footerContent");
		this.setSize("100%", "25");
		this.setStyleName("footer");
	}
}
