package soft.eng.client;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.FlexTable;

public class FormTable extends FlexTable {
	private DBForm form;
	private DataTable table;
	public FormTable(String tableName, String idColomn){
		form = new DBForm(tableName, idColomn){
			@Override
			public void onFinishAdding(Response message){
				table.refresh();
			}
		};
		table = new DataTable(tableName, idColomn, true);
		this.setWidget(0, 0, form);
		this.setWidget(1, 0, table);
	}
	public void addHeader(DataHeader header){
		table.addHeader(header);
		form.addHeader(header);
	}
	public void load(){
		form.createForm();
		table.refresh();
	}

	public void addFilter(DataFilter f){
		table.addFilter(f);
	}
	public void addOrder(DataOrder o){
		table.addOrder(o);
	}
	
}
