package tonton.creative.Creative;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
public class PlotModel implements IBaseModel {
	public final String Name; 
	public final PlotLocation Location;
	public final OfflinePlayer MainPlayer;  
	private Table _pinnedTable;
	private PlotCreator _creator;
	private PlotRules _rules;
	private PlotPlayerList _admins;
	private PlotPlayerList _bans;
	private int _UID;
	public PlotModel(String name,PlotLocation location,OfflinePlayer pl) {
		_rules=new PlotRules(this);
		_admins=new PlotPlayerList(this,"Admins","Администраторы");
		_bans=new PlotPlayerList(this,"Bans","Забаненые");
		Name=name;
		MainPlayer=pl;
		Location=location;
		_creator=new PlotCreator(location);
		_UID=new Random().nextInt(0,1000000);
		// TODO Auto-generated constructor stub
	}
	public boolean TpPlayer(Player pl) {
		if(GetBanList().HasPlayer(pl.getName()))
			return false;
		pl.teleport(new Location(pl.getWorld(), Location.Center.getBlockX(),pl.getLocation().getBlockY(),Location.Center.getBlockY()));
		return true;
	}
	public boolean ChangePlayerList(String name,PlayerParam param) {
		boolean can=false;
		switch(param) {
		case Ban:
			can=GetBanList().AddPlayer(name);
			if(can)
			GetAdminList().RemovePlayer(name);
			return can;
		case Deop:
			return GetAdminList().RemovePlayer(name);
		case Op:
			can=GetAdminList().AddPlayer(name);
			if(can)
			GetBanList().RemovePlayer(name);
			return can;
		case Pardon:
			return GetBanList().RemovePlayer(name);
		default:
			return false;
		
		}
	}
	public static void TpHome(Player pl,String message) {
		TablePlot _plot=(TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots");
		PlotModel model1=_plot.GetPlotFinder().FindByName(pl.getName());
		Random random=new Random();
		if(model1==null)
			pl.teleport(new Location(pl.getWorld(),random.nextInt(-10000,10000),100,random.nextInt(-10000,10000)));
		else
			pl.teleport(new Location(pl.getWorld(),model1.Location.Center.getX(),100,model1.Location.Center.getY()));
		if(!message.equals(""))
			pl.sendMessage(message);
	}
	public PlotCreator GetCreator() {
		return _creator;
	}
	public boolean PlayerInPlot(Player pl) {
		return MainPlayer.getName().equals(pl.getName())||GetAdminList().HasPlayer(pl.getName());
	}
	public boolean PlayerInBan(Player pl) {
		return GetBanList().HasPlayer(pl.getName());
	}
	public PlotRules GetRules() {
		_rules.MustUpdate();
		return _rules;
	}
	public PlotPlayerList GetAdminList() {
		_admins.MustUpdate();
		return _admins;
	}
	public PlotPlayerList GetBanList() {
		_bans.MustUpdate();
		return _bans;
	}
	public String GetShortInfo() {
		String info="\n"+Name;
		info+="\n Администратор:"+MainPlayer.getName();
		return info;
	}
	public String GetInfo() {
		String info=GetShortInfo();
		info+=_rules.GetInfo();
		return info;
	}
	@Override
	public String GetValues() {
		// TODO Auto-generated method stub
		_rules.Update();
		return " ('"+MainPlayer.getName()+"',"+Location.Center.getBlockX()+","+Location.Center.getBlockY()+","+Location.Size+",'"+Name+"', "+_rules.GetValueString()+","+_admins.GetValueString()+","+_bans.GetValueString()+")";
	}

	@Override
	public void SetPinnedModel(Table table) {
		// TODO Auto-generated method stub
		_pinnedTable=table;
	}

	@Override
	public Table PinnedModel() {
		// TODO Auto-generated method stub
		return _pinnedTable;
	}

	@Override
	public String GetRemoveString() {
		// TODO Auto-generated method stub
		return "WHERE Player='"+MainPlayer.getName()+"'";
	}
	@Override
	public int GetUID() {
		// TODO Auto-generated method stub
		return _UID;
	}

}
