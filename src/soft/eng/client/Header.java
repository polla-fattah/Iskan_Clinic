package soft.eng.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class Header extends FlexTable{
	public Image image;

	public Header(){
		image = new Image(Security.logo);
		image.setSize("95", "95");
		this.setWidget(0, 0, image);
		this.setCellSpacing(0);
		this.setWidget(0, 1, new HTML("<span style='font-size:25px;'>"+Security.companyName+"</span><BR/><span style='font-size:18px;'>Patients Record Database</span>"));
		this.setSize("100%", "100");
		this.setStyleName("header");
		this.setWidget(0, 2, new Label("Welcome: Dr. " + Security.fullName));
		this.getCellFormatter().setAlignment(0, 0, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
		this.getCellFormatter().setAlignment(0, 2, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_BOTTOM);
	}
}
