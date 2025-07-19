package tonton.creative.Creative;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlotRules extends PlotData {
	private  boolean _interactionWithBlocks=false;
	private  boolean _interactionWithEntity=false;
	private boolean _useItems=false;
	private boolean _explosions=true;
	private boolean _pvp=false;
	private boolean _update=true;
	public PlotRules(PlotModel pinned) {
		super(pinned,"Rules");
	}
	public boolean GetRule(Rules get) {
	
		switch(get) {
		case Explosions:
			return _explosions;
		case InteractionWithBlocks:
			return _interactionWithBlocks;
		case InteractionWithEntity:
			return _interactionWithEntity;
		case Pvp:
			return _pvp;
		case UseItems:
			return _useItems;
		default:
			return false;
		}
	}
	public String GetInfo() {
		return "\n Правила:\n  Взаимодействие с блоками:"+_interactionWithBlocks+
				"\n  Взаимодействие с сущностями:"+_interactionWithEntity+
				"\n  Использование предметов:"+_useItems+
				"\n  Взрывы:"+_explosions+
				"\n  Пвп:"+_pvp;
	}
	public boolean ChangeRule(Rules rule,boolean val) {
	
		switch(rule) {
		case Explosions:
			_explosions=val;
			break;
		case InteractionWithBlocks:
			_interactionWithBlocks=val;
			break;
		case InteractionWithEntity:
			_interactionWithEntity=val;
			break;
		case Pvp:
			_pvp=val;
			break;
		case UseItems:
			_useItems=val;
			break;
		default:
			break;
				
		}
		return SendSqlUpdate();
	}
	@Override
	protected void LoadFromString(String get) {
		String[] values=get.split(";");
		_interactionWithBlocks=Boolean.parseBoolean(values[0]);
		_interactionWithEntity=Boolean.parseBoolean(values[1]);
		_useItems=Boolean.parseBoolean(values[2]);
		_explosions=Boolean.parseBoolean(values[3]);
		_pvp=Boolean.parseBoolean(values[4]);
	}
	@Override
	public String GetValueString() {
		return "'"+Boolean.toString(_interactionWithBlocks)+";"+Boolean.toString(_interactionWithEntity)+";"+Boolean.toString(_useItems)+";"+Boolean.toString(_explosions)+";"+Boolean.toString(_pvp)+"'";
	}

}
