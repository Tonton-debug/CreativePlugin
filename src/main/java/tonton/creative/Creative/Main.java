package tonton.creative.Creative;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin {
	private static JavaPlugin _instance;
	public static JavaPlugin getInstance() {
		return _instance;
	}
	 @Override
	    public void onEnable(){
		 _instance=this;
		 getDataFolder().mkdirs();
		
		 new PlotDataBaseInitialization().Initializate("plotsDB", new String[]{"Plots"});
		 if(DataBaseStorage.Get("plotsDB")!=null&&DataBaseStorage.Get("plotsDB").GetTable("Plots")==null)
		 {
			 DataBase base=DataBaseStorage.Get("plotsDB");
			 base.AddTable(new TablePlot(base));
		 }
		 getCommand("plot").setExecutor(new Commands());
		 getServer().getPluginManager().registerEvents(new PlayerLister(), this);
	    }
	    @Override
	    public void onDisable(){
	    }     
	
}
