package tonton.creative.Creative;

import java.util.ArrayList;

public class PlotPlayerList extends PlotData {
	private ArrayList<String> _playersName=new ArrayList<String>();
	private String _name;
	public PlotPlayerList(PlotModel get, String row,String name) {
		super(get, row);
		_name=name;
		// TODO Auto-generated constructor stub
	}
	public boolean AddPlayer(String name) {
		if(HasPlayer(name))
			return false;
		_playersName.add(name);
		SendSqlUpdate();
		return true;
	}
	public boolean RemovePlayer(String name) {
		if(!HasPlayer(name))
			return false;
		_playersName.remove(name);
		SendSqlUpdate();
		return true;
	}
	public boolean HasPlayer(String name) {
			return _playersName.contains(name);
	}
	@Override
	protected void LoadFromString(String get) {
		String[] values=get.split(";");
		for(String val:values) {
			AddPlayer(val);
		}
		
	}
	@Override
	public String GetInfo() {
		String result=_name+":";
		for(String name:_playersName) {
			result+=" "+name+" ";
		}
		return result;
	}
	@Override
	public String GetValueString() {
		if(_playersName.size()==0)
			return "''";
		String result="";
		for(String name:_playersName) {
			result+=name+";";
		}
		return "'"+result+"'";
	}

}
