package tonton.creative.Creative;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Table {
	private TableParam[] _values;
	private int _primaryKey;
	public final String Name;
	private ArrayList<IBaseModel> _models=new ArrayList<IBaseModel>();
	public final DataBase PinnedBase;
	public Table(TableParam[] values,int primaryKey,String name,DataBase base) {
		_values=values;
		_primaryKey=primaryKey;
		Name=name;
		PinnedBase=base;
	}
	private boolean SendInsertModel() {
		String finalString="INSERT INTO '"+Name+"'(";
		for(int i=0;i<_values.length;i+=1) {
			finalString+=_values[i].toString(false);
			if((i+1)<_values.length)
				finalString+=",";
		}
		finalString+=")"+ "VALUES";
		finalString+=_models.getLast().GetValues();
		return DataBaseManager.Get(PinnedBase.Name).SendSqlExecute(SqlExecuteType.Update, finalString);
	}
	private boolean HasModel(IBaseModel model) {
		for(IBaseModel getModel:_models) {
			if(getModel.GetUID()==model.GetUID())
				return true;
		}
		return false;
	}
	public boolean AddModel(IBaseModel model) {
		if (HasModel(model))
			return false;
		model.SetPinnedModel(this);
		_models.add(model);
		SendInsertModel();
		return true;
	}
	public boolean RemoveModel(IBaseModel model) {
		String finalString="DELETE FROM "+Name+" "+model.GetRemoveString();
		boolean remove=DataBaseManager.Get(PinnedBase.Name).SendSqlExecute(SqlExecuteType.Update, finalString);
		if(!remove)
			return false;
		_models.remove(model);
		return true;
	}
	public IBaseModel[] GetModels() {
		IBaseModel[] models=new IBaseModel[_models.size()];
		for(int i=0;i<models.length;i+=1)
			models[i]=_models.get(i);
		return models;
	}
	public final String GetCreateString() {
		String finalString="CREATE TABLE if not exists '"+Name+"' ( ";
		for(int i=0;i<_values.length;i+=1) {
			finalString+=_values[i].toString(true)+",";
		 }
		finalString+="PRIMARY KEY('"+_values[_primaryKey].Name+"'));";
		return finalString;
	}           
}
