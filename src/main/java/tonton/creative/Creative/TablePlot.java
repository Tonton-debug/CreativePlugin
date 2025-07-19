package tonton.creative.Creative;

public class TablePlot extends Table {
	private PlotModel[] _temp;
	private boolean _update=true;
	public TablePlot(DataBase base) {
		super(new TableParam[] {
		   		new TableParam("Player","TEXT NOT NULL"),
				new TableParam("CenterX","INT"),
				new TableParam("CenterY","INT"),
				new TableParam("Size","INT"),
				new TableParam("PlotName","TEXT NOT NULL"),
				new TableParam("Rules","TEXT NOT NULL"),
				new TableParam("Admins","TEXT NOT NULL"),
				new TableParam("Bans","TEXT NOT NULL")
				}, 0, "Plots",base);
		
		// TODO Auto-generated constructor stub
	}
	public PlotFinder GetPlotFinder() {
		return new PlotFinder(GetPlotModels());
	}
	public PlotHelper GetPlotHelper() {
		return new PlotHelper(GetPlotModels());
	}
	private PlotModel[] GetNewPlotModels() {
		IBaseModel[] models=GetModels();
		PlotModel[] plots=new PlotModel[models.length];
		for(int i=0;i<models.length;i+=1)
				plots[i]=(PlotModel) models[i];
		return plots;
	}
	public PlotModel[] GetPlotModels() {
		if(_update) {
			_temp=GetNewPlotModels();
			_update=false;
		}
		return _temp;
	}
	public boolean RemoveModel(PlotModel model) {
		boolean yess=super.RemoveModel(model);
		if(yess) {
			model.GetCreator().Remove();
			_update=true;
		}
		return yess;
	}
	public boolean AddModel(PlotModel model,boolean createPlot)
	{
		
		if(GetPlotHelper().GetNearestPlot(model, 1).size()!=0||GetPlotFinder().FindByName(model.MainPlayer.getName())!=null)
			return false;
		boolean yess=super.AddModel(model);
		if(createPlot&&yess) 
		model.GetCreator().Create();
		if(yess)
		_update=true;
		return yess;
	}
}
