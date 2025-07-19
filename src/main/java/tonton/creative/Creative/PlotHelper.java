package tonton.creative.Creative;

import java.util.ArrayList;

public class PlotHelper {
	private PlotModel[] _models;
	public PlotHelper(PlotModel[] model) {
		_models=model;
		// TODO Auto-generated constructor stub
	}
	public ArrayList<PlotModel> GetNearestPlot(PlotModel model,int distance){
		ArrayList<PlotModel> finalModels=new ArrayList<PlotModel>();
		for(int i=0;i<_models.length;i+=1) {
			if(_models[i].equals(model))
				continue;
			if(_models[i].Location.ContaintsLocation(model.Location, distance)||model.Location.ContaintsLocation(_models[i].Location, distance))
				finalModels.add(model);
		}
		return finalModels;
	}
	public String GetInfoPlots(int index) {
		String finalString="";
		int startX=index*5;
		int endX=startX+5;
		for(int i=startX;i<_models.length;i+=1) {
			if(i>endX)
				return finalString;
			finalString+=_models[i].GetShortInfo();
		}
		return finalString;
	}

}
