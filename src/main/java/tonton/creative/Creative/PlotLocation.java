package tonton.creative.Creative;

import org.bukkit.World;
import org.bukkit.util.Vector;

public class PlotLocation {
	public final Vector Center;
	public final World MainWorld;
	public final int Size;
	private Vector _leftDown;
	private Vector _leftUp;
	private Vector _rightUp;
	private Vector _rightDown;
	public PlotLocation(Vector center,int size,World world) {
		Center = center;
		Size = size;
		_leftDown=new Vector(Center.getX()-Size,Center.getY()-Size,0);
		_leftUp=new Vector(Center.getX()-Size,Center.getY()+Size,0);
		_rightUp=new Vector(Center.getX()+Size,Center.getY()+Size,0);
		_rightDown=new Vector(Center.getX()+Size,Center.getY()-Size,0);
		MainWorld=world;
		// TODO Auto-generated constructor stub
	}
	public Vector GetLeftDown() {
		return _leftDown;
	}
	public Vector GetLeftUp() {
		return _leftUp;
	}
	public Vector GetRightUp() {
		return _rightUp;
	}
	public Vector GetRightDown() {
		return _rightDown;
	}
	public boolean ContainsPosition(Vector pos) {
		return ContainsPosition(pos,0);
	}
	public boolean ContainsPosition(Vector pos,int size) {
		return pos.getBlockX()>GetLeftDown().getBlockX()-size&&pos.getBlockX()<GetRightDown().getBlockX()+size&&pos.getBlockY()>GetLeftDown().getBlockY()-size&&pos.getBlockY()<GetLeftUp().getBlockY()+size;
	}
	public boolean ContaintsLocation(PlotLocation loc,int size) {
		if(loc.ContainsPosition(Center,2)||loc.ContainsPosition(GetLeftDown(),2)||loc.ContainsPosition(GetLeftUp(),2)||loc.ContainsPosition(GetRightDown(),2)||loc.ContainsPosition(GetRightUp(),2))
		  return true;
		return false;
	}

}
