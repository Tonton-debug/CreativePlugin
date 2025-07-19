package tonton.creative.Creative;

import java.util.ArrayList;

public class DataBaseStorage {

	private static ArrayList<DataBase> _base=new ArrayList<DataBase>();
	public static void Add(DataBase base) {
		_base.add(base);
		DataBaseManager.Get(base.Name);
	}
	public static DataBase Get(String name) {
		for(DataBase base:_base) {
			if(base.Name.equals(name))
				return base;
		}
		return null;
	}

}
