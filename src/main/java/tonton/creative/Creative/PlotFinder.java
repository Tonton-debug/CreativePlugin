package tonton.creative.Creative;

import java.util.Random;

import org.bukkit.util.Vector;

public class PlotFinder {
	private PlotModel[] _models;
	public PlotFinder(PlotModel[] models) {
		// TODO Auto-generated constructor stub
		_models=models;
	}
	public PlotModel FindByPosition(Vector pos) {
		
		for(int i=0;i<_models.length;i+=1) {
			
			if(_models[i].Location.ContainsPosition(pos))
				return _models[i];
		}
		return null;
	}
	public PlotModel FindByName(String name) {
		
		for(int i=0;i<_models.length;i+=1) {
			if(_models[i].MainPlayer.getName().equals(name))
				return _models[i];
		}
		return null;
	}
	public PlotModel GetRandom() {
		if(_models.length==0)
			return null;
		Random random=new Random();
		int i=random.nextInt(_models.length);
		return _models[i];
	}

}
