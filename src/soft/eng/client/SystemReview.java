package soft.eng.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import soft.eng.client.Complains.Complain;

import com.google.gwt.user.client.ui.FlexTable;

public class SystemReview extends FlexTable{
	private Systems systems;
	private Complains complains;
	private TreeMap<Integer,CustomDisclosure>customSystems = new TreeMap<Integer,CustomDisclosure>();
	public SystemReview(Systems stm, Complains comp){
		systems = stm;
		complains = comp;
		TreeSet<Systems.System> availableSystems = systems.getSystems();
		int row = 0;
		CustomDisclosure custom;
		int idSystem;
		for(Iterator<Systems.System> i = availableSystems.iterator(); i.hasNext();){
			idSystem = i.next().getId();
			custom = new CustomDisclosure(idSystem,systems, complains);
			customSystems.put(idSystem, custom);
			this.setWidget(++row, 1, custom);
		}
	}
	protected void updateComplains(TreeMap<Integer, ArrayList<Complain>> currenComp) {
		for (Iterator<Entry<Integer, CustomDisclosure>> i=customSystems.entrySet().iterator(); i.hasNext(); ) {
		    Map.Entry<Integer, CustomDisclosure> e =  i.next();
		    e.getValue().updateExistingComplains(currenComp.get(e.getKey()));
		}

	}
}
