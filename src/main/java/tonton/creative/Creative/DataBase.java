package tonton.creative.Creative;

import java.util.ArrayList;

public class DataBase {
	public final String Name;
	private ArrayList<Table> _tables=new ArrayList<Table>();
	public DataBase(String name) {
		Name=name;
	}
	public void AddTable(Table table) {
		_tables.add(table);
		
		DataBaseManager.Get(Name).SendSqlExecute(SqlExecuteType.Default, table.GetCreateString()
				);
		
	}
	public boolean HasTable(String name) {
		return GetTable(name)!=null;
	}
	public Table GetTable(String name) {
		for(Table table:_tables) {
			if(table.Name.equals(name))
				return table;
		}
		return null;
	}
	

}
