package tonton.creative.Creative;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class PlayerLister implements Listener {
private TablePlot _plot;
public PlayerLister() {
	_plot=(TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots");
}   
	@EventHandler
public void OnJoin(PlayerJoinEvent event) {
	}
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event){
		if(TryBan(event.getPlayer().getLocation(),event.getPlayer()))
			return;
		if(PlayerIsAdmin(event.getPlayer().getLocation(),event.getPlayer()))
			return;
		if(event.getClickedBlock()!=null) {
		if(HasRule(Rules.InteractionWithBlocks,event.getClickedBlock().getLocation(),event.getPlayer())&&!HasRule(Rules.UseItems,event.getClickedBlock().getLocation(),event.getPlayer()))
		{
			event.setUseItemInHand(Result.DENY);
			return;
		}
		if(!HasRule(Rules.InteractionWithBlocks,event.getClickedBlock().getLocation(),event.getPlayer())&&HasRule(Rules.UseItems,event.getClickedBlock().getLocation(),event.getPlayer()))
		{
			event.setUseInteractedBlock(Result.DENY);
			return;
		}
		if(HasRule(Rules.InteractionWithBlocks,event.getClickedBlock().getLocation(),event.getPlayer())&&HasRule(Rules.UseItems,event.getClickedBlock().getLocation(),event.getPlayer()))
		{
			return;
		}
		event.setCancelled(!Use(event.getClickedBlock().getLocation(),event.getPlayer()));
		
		}
		else {
			if(HasRule(Rules.UseItems,event.getPlayer().getLocation(),event.getPlayer()))
				return;
			event.setCancelled(!Use(event.getPlayer().getLocation(),event.getPlayer()));
			
		}
	}
	private boolean HasMeta(Entity entity,String meta) {
		return entity.hasMetadata(meta);
	}
	private boolean PlayerIsAdmin(Location loc,Player pl) {
		PlotModel model=GetPlotModel(loc);
		if(model==null)
			return false;
		return model.PlayerInPlot(pl);
	}
	private boolean CheckWithMeta(Entity entity) {
		if(HasMeta(entity,"startPosition"))
		{
			Location first=(Location)entity.getMetadata("startPosition").get(0).value();
			Location finalLoc=entity.getLocation();
			PlotModel model=GetPlotModel(first);
			if(model==null)
				return false;
			else if(model.MainPlayer.isOnline()) {
				return Use(finalLoc,model.MainPlayer.getPlayer());
				
			}
			else
				return false;
		}
		return false;
	}
	public boolean PlayerInBan(Location loc,Player pl) {
		PlotModel model=GetPlotModel(loc);
		if(model==null)
			return false;
		return model.PlayerInBan(pl);
	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		Entity entity=event.getDamager();
		if(entity.hasMetadata("startPosition"))
		{
			Location finalLoc=entity.getLocation();
			Location first=(Location)entity.getMetadata("startPosition").get(0).value();
			PlotModel get=GetPlotModel(first);
			PlotModel get2=GetPlotModel(finalLoc);
			if(get==null) {
				event.setCancelled(true);
				return;
			}
			if(!get.MainPlayer.isOnline()){
				event.setCancelled(true);
				return;
			}
			if(get.MainPlayer.isOnline()){
				if(get2==null)
				{
					event.setCancelled(true);
					return;
				}
				if(get2.PlayerInBan(get.MainPlayer.getPlayer()))
				{
					event.setCancelled(true);
					return;
				}
				
			}
			if(HasRule(Rules.Pvp,finalLoc))
				return;
			event.setCancelled(!CheckWithMeta(entity));
		}else if(entity instanceof Player) {
			Player pl=(Player)entity;
			if(TryBan(event.getEntity().getLocation(),pl))
				return;
			if(HasRule(Rules.Pvp,event.getEntity().getLocation(),pl)||PlayerIsAdmin(event.getEntity().getLocation(),pl))
				return;
			event.setCancelled(!Use(event.getEntity().getLocation(),pl));
		}
	}
	private EntityType[] _tntTypes=new EntityType[] {EntityType.TNT,EntityType.TNT_MINECART,EntityType.END_CRYSTAL,EntityType.ARROW,
			EntityType.WIND_CHARGE,EntityType.FIREBALL,EntityType.SNOWBALL,EntityType.FIREWORK_ROCKET,EntityType.TRIDENT,EntityType.EGG,EntityType.SPECTRAL_ARROW};
	@EventHandler
	public void onSpawn(EntitySpawnEvent event) {
		
		for(EntityType type:_tntTypes) {
			if(event.getEntityType()==type) {
				Entity tnt=event.getEntity();
				tnt.setMetadata("startPosition", new FixedMetadataValue(Main.getInstance(),event.getEntity().getLocation()));
				return;
			}
		}
	}
	private PlotModel GetPlotModel(Location loc) {
		Vector pos1=new Vector(loc.getBlockX(),loc.getBlockZ(),0);
		PlotModel model1=_plot.GetPlotFinder().FindByPosition(pos1);
		return model1;
	}
	private boolean HasRule(Rules rule,Location loc) {
		return HasRule(rule,loc,null);
	}
	private boolean HasRule(Rules rule,Location loc,Player pl) {
		PlotModel model1=GetPlotModel(loc);
		if(model1==null)
			return false;
		if(pl!=null&&model1.PlayerInPlot(pl))
			return true;
		
		return model1.GetRules().GetRule(rule);
	}
	@EventHandler
	public void onExplode(EntityExplodeEvent  e) {
		if(!HasRule(Rules.Explosions,e.getEntity().getLocation()))
		{
			e.getEntity().remove();
			e.setCancelled(true);
			return;
		}
		if(!HasMeta(e.getEntity(),"startPosition"))
		{
			e.getEntity().remove();
			e.setCancelled(true);
			return;
		}
		if(!CheckWithMeta(e.getEntity())) {
			e.getEntity().remove();
			e.setCancelled(true);
			return;
		}
		List<Block> blocks=new ArrayList<Block>();
		Location first=(Location)e.getEntity().getMetadata("startPosition").get(0).value();
			for(Block block:e.blockList()) {
				if(Use(first,block.getLocation()))
					blocks.add(block); 
			}
			e.blockList().clear();
			for(Block block:blocks) {
				//if(id%100==0)
			//	System.out.println("ADD BLOCK:"+e.blockList().size()+" "+blocks.size());
				//id+=1;
				e.blockList().add(block);
			}
	}
	private boolean Use(Location startLoc,Location endLoc) {
		PlotModel model1=GetPlotModel(startLoc);
		PlotModel model2=GetPlotModel(endLoc);
		if(model1==null||model2==null)
			return false;
		if(model1.equals(model2))
			return true;
		return false;
	}
	private boolean Use(Location loc,Player pl) {
		PlotModel model1=GetPlotModel(loc);
		if(model1==null)
			return false;
		return model1.PlayerInPlot(pl);
	}
	private boolean TryBan(Location loc,Player pl) {
		if(PlayerInBan(loc,pl))
		{
			PlotModel.TpHome(pl, "ВЫ ЗАБАНЕНЫ НА ЭТОЙ ТЕРРИТОРИИ");
			return true;
		}
		return false;
	}
	@EventHandler
	public void OnMove(PlayerMoveEvent event) {
		Vector pos1=event.getFrom().toVector();
		Vector pos2=event.getTo().toVector();
		double size=pos1.subtract(pos2).length();
		if(size<=0.1)
			return;
		if(TryBan(event.getPlayer().getLocation(),event.getPlayer()))
			return;
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		
		if(PlayerIsAdmin(event.getBlock().getLocation(),event.getPlayer()))
			return;
		event.setCancelled(!Use(event.getBlock().getLocation(),event.getPlayer()));
	 }
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(PlayerIsAdmin(event.getBlock().getLocation(),event.getPlayer()))
			return;
		event.setCancelled(!Use(event.getBlock().getLocation(),event.getPlayer()));
	 }
	@EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
		Location loc=e.getRightClicked()==null?e.getPlayer().getLocation():e.getRightClicked().getLocation();
		if(TryBan(loc,e.getPlayer()))
			return;
		if(PlayerIsAdmin(loc,e.getPlayer()))
			return;
		if(e.getRightClicked()!=null) {
			if(HasRule(Rules.InteractionWithEntity,e.getRightClicked().getLocation(),e.getPlayer()))
				return;
			e.setCancelled(!Use(e.getRightClicked().getLocation(),e.getPlayer()));
		}
		else
			e.setCancelled(!Use(e.getPlayer().getLocation(),e.getPlayer()));
	}
	@EventHandler
	public void OnLeave(PlayerQuitEvent event) {
	
	}
}
