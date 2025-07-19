package tonton.creative.Creative;

import org.bukkit.Material;
import org.bukkit.World;

public class PlotCreator {
	private PlotLocation _location;
	
	public PlotCreator(PlotLocation location) {
		// TODO Auto-generated constructor stub
		_location=location;
	}
	public void Remove() {
		int startX=_location.GetLeftDown().getBlockX();
		int startY=_location.GetLeftDown().getBlockY();
		int endX=_location.GetRightDown().getBlockX();
		int endY=_location.GetLeftUp().getBlockY();
		
		for(int x=startX;x<=endX;x+=1) {
			for(int y=startY;y<=endY;y+=1) {
				if(!((x==startX||x==endX)||(y==startY||y==endY)))
					continue;
				
				int yMax=_location.MainWorld.getHighestBlockAt(x, y).getY();
				_location.MainWorld.getBlockAt(x,yMax,y).setType(Material.AIR);
				
				
			}
		}
	}
	public void Create() {
		int startX=_location.GetLeftDown().getBlockX();
		int startY=_location.GetLeftDown().getBlockY();
		int endX=_location.GetRightDown().getBlockX();
		int endY=_location.GetLeftUp().getBlockY();
		
		for(int x=startX;x<=endX;x+=1) {
			for(int y=startY;y<=endY;y+=1) {
				if(!((x==startX||x==endX)||(y==startY||y==endY)))
					continue;
				   
				int yMax=_location.MainWorld.getHighestBlockAt(x, y).getY()+1;
				_location.MainWorld.getBlockAt(x,yMax,y).setType(Material.BEDROCK);
				
				
			}
		}
	}

}
