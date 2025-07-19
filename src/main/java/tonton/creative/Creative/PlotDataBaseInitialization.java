package tonton.creative.Creative;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.Vector;

public class PlotDataBaseInitialization implements IDataBaseInitialization {

	public PlotDataBaseInitialization() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean Initializate(String nameDB, String[] plotsName) {
		// TODO Auto-generated method stub
		try {
		DataBase base=new DataBase(DataBaseManager.Get(nameDB).Name);
		for(int i=0;i<plotsName.length;i+=1) {
			ResultSet set = DataBaseManager.Get(nameDB).SendSqlWithResult("SELECT * FROM "+ plotsName[i]);
			if(set==null)
				continue;
			TablePlot plot=new TablePlot(base);
				
				while(set.next())
				{
					String plotName=set.getString("PlotName");
					String playerName=set.getString("Player");
					int CenterX=set.getInt(2);
					int CenterY=set.getInt(3);
					int size=set.getInt(4);
					
					OfflinePlayer pl=Bukkit.getOfflinePlayer(playerName);
					if(pl==null)
						continue;
					PlotModel model=new PlotModel(plotName, new PlotLocation(new Vector(CenterX,CenterY,0),size,pl.getLocation().getWorld()),pl );
					plot.AddModel(model,false);
					
				}
				base.AddTable(plot);
		}
		DataBaseStorage.Add(base);
		return true;
		} catch (SQLException e) {
		return false;
		}
	}

}
