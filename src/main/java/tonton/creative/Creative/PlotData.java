package tonton.creative.Creative;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class PlotData {
	private PlotModel _pinned;
	private String _pinnedRow;
	private boolean _update=true;
	public PlotData(PlotModel get,String row) {
		_pinned=get;
		_pinnedRow=row;
	}
	protected void Update() {
		ResultSet set = DataBaseManager.Get(_pinned.PinnedModel().PinnedBase.Name).SendSqlWithResult("SELECT * FROM "+ _pinned.PinnedModel().Name+" WHERE Player = '"+_pinned.MainPlayer.getName()+"'");
		try {
			while(set.next()) {
				LoadFromString(set.getString(_pinnedRow));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void MustUpdate() {
		if(_update) {
			_update=false;
			Update();
		}
	}
	protected boolean SendSqlUpdate() {
		String finalString="UPDATE "+ _pinned.PinnedModel().Name+ " SET "+_pinnedRow+" ="+GetValueString()+" WHERE Player = '"+_pinned.MainPlayer.getName()+"'";
		return DataBaseManager.Get(_pinned.PinnedModel().PinnedBase.Name).SendSqlExecute(SqlExecuteType.Update, finalString);
	}
	protected abstract void LoadFromString(String get); 
	public abstract String GetInfo();
	public abstract String GetValueString();
}
