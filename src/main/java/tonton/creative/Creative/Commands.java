package tonton.creative.Creative;

import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		Player player = (Player) arg0;
		PlotModel modelPlot;
		if(arg2.equals("plot")&&(arg0.isOp() ||arg0.hasPermission("creative.create"))&&arg3.length>0) {
			switch(arg3[0]) {
			case "create":
				if(arg3.length!=3||!IntegerHelper.IsInteger(arg3[2])) {
					arg0.sendMessage("НЕ УДАЛОСЬ СОЗДАТЬ ТЕРРИТОРИЮ. МАЛО ПАРАМЕТРОВ");
					break;
				}
				int size=Integer.parseInt(arg3[2]);
				if(size<=10||size>=100)
				{
					arg0.sendMessage("РАЗМЕР ДОЛЖЕН БЫТЬ В ДИАПАЗОНЕ ОТ 10 ДО 100");
					break;
				}
				PlotModel model=new PlotModel(arg3[1],new PlotLocation(new Vector(player.getLocation().getBlockX(),player.getLocation().getBlockZ(),0),size,player.getWorld()),player);
				if(((TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots")).AddModel(model,true))
					arg0.sendMessage("ТЕРРИТОРИЯ УСПЕШНО СОЗДАНА");
				else
					arg0.sendMessage("НЕ УДАЛОСЬ СОЗДАТЬ ТЕРРИТОРИЮ");
				break;
			case "remove":
				modelPlot=GetPlot(player);
				if(modelPlot==null) 
					break;
				if(((TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots")).RemoveModel(modelPlot))
					arg0.sendMessage("ТЕРРИТОРИЯ УСПЕШНО УДАЛЕНА");
				else
					arg0.sendMessage("НЕ УДАЛОСЬ УДАЛИТЬ ПЛОТ");
				break;
			case "change":
				modelPlot=GetPlot(player);
				if(modelPlot==null)
					break;
				if(arg3.length!=3||!(arg3[2].equals("true")|| arg3[2].equals("false"))) {
					arg0.sendMessage("Что это.");
					break;
				}
				Rules rule;
				try {
					rule=Rules.valueOf(arg3[1]);
				}catch(Exception ex) {
					arg0.sendMessage("Неверное правило!");
					String send="Доступные правила:";
					for(Rules rule2:Rules.values())
					{
						send+=" "+rule2.toString()+" ";
					}
					arg0.sendMessage(send);
					break;
				}
				if(modelPlot.GetRules().ChangeRule(rule, arg3[2].equals("true"))) {
					arg0.sendMessage("Правило "+rule.toString()+" изменено на "+arg3[2]);
				}else {
					arg0.sendMessage("Не удалось изменить правило "+rule.toString());
				}
				break;
			case "player":
				modelPlot=GetPlot(player);
				if(modelPlot==null)
					break;
				if(arg3.length==2&&arg3[1].equals("list")) {
					arg0.sendMessage(modelPlot.GetAdminList().GetInfo());
					arg0.sendMessage(modelPlot.GetBanList().GetInfo());
					break;
				}
				if(arg3.length!=3) {
					arg0.sendMessage("Что это.");
					break;
				}
				switch(arg3[2]) {
				case "op":
					if(modelPlot.ChangePlayerList(arg3[1],PlayerParam.Op))
						arg0.sendMessage("Игрок "+arg3[1]+" назначен админом");
					else
						arg0.sendMessage("Игрок "+arg3[1]+" НЕ назначен админом");
					break;
				case "deop":
					if(modelPlot.ChangePlayerList(arg3[1],PlayerParam.Deop))
						arg0.sendMessage("Игрок "+arg3[1]+" понижен");
					else
						arg0.sendMessage("Игрок "+arg3[1]+" НЕ понижен");
					break;
				case "ban":
					if(modelPlot.ChangePlayerList(arg3[1],PlayerParam.Ban))
						arg0.sendMessage("Игрок "+arg3[1]+" забанен");
					else
						arg0.sendMessage("Игрок "+arg3[1]+" НЕ забанен");
					break;
				case "pardon":
					if(modelPlot.ChangePlayerList(arg3[1],PlayerParam.Pardon))
						arg0.sendMessage("Игрок "+arg3[1]+" разбанен");
					else
						arg0.sendMessage("Игрок "+arg3[1]+" НЕ разбанен");
					break;
				default:
					arg0.sendMessage("НЕПРАВИЛЬНАЯ КОМАНДА. ПРАВИЛЬНЫЙ ВАРИАНТ: /plot player [имя игрока] [op/deop/ban/pardon/kick]");
				}
				break;
			case "info":
				modelPlot=GetPlot(player.getLocation());
				if(modelPlot==null) {
					arg0.sendMessage("ОТСУТСВУЕТ ТЕРРИТОРИЯ");
					break;
				}
				arg0.sendMessage(modelPlot.GetInfo());
				break;
			case "my":
				modelPlot=GetPlot(player);
				if(modelPlot==null)
					break;
				PlotModel.TpHome(player, "");
				break;
			case "rtp":
				modelPlot=((TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots")).GetPlotFinder().GetRandom();
				if(modelPlot==null) {
					arg0.sendMessage("ОТСУТСВУЕТ ТЕРРИТОРИЯ");
					break;
				}
				if(!modelPlot.TpPlayer(player)) {
					arg0.sendMessage("Вы забанены на этой территории!");
					break;
				}
				break;
			case "gm":
				if(arg3.length!=2||!IntegerHelper.IsInteger(arg3[1])) {
					arg0.sendMessage("МАЛО ПАРАМЕТРОВ ИЛИ INDEX НЕ ЧИСЛО");
					break;
				}
				int gm=Integer.parseInt(arg3[1]);
				if(gm==0)
					player.setGameMode(GameMode.SURVIVAL);
				if(gm==1)
					player.setGameMode(GameMode.CREATIVE);
				if(gm==2)
					player.setGameMode(GameMode.SPECTATOR);
				break;
			case "tppos":
				if(arg3.length!=4||!IntegerHelper.IsInteger(arg3[1])||!IntegerHelper.IsInteger(arg3[2])||!IntegerHelper.IsInteger(arg3[3])) {
					arg0.sendMessage("МАЛО ПАРАМЕТРОВ ИЛИ POS НЕ ЧИСЛО");
					break;
				}
				player.teleport(new Location(player.getWorld(),Integer.parseInt(arg3[1]),Integer.parseInt(arg3[2]),Integer.parseInt(arg3[3])));
				break;
			case "tp":
				if(arg3.length!=2) {
					arg0.sendMessage("НЕДОСТАТОЧНО АРГУМЕНТОВ");
					break;
				}
				modelPlot=((TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots")).GetPlotFinder().FindByName(arg3[1]);
				if(modelPlot==null)
				{
					arg0.sendMessage("ТЕРРИТОРИЯ НЕ НАЙДЕНА");
					break;
				}
				if(!modelPlot.TpPlayer(player)) {
					arg0.sendMessage("Вы забанены на этой территории!");
					break;
				}
				break;
			case "list":
				if(arg3.length!=2||!IntegerHelper.IsInteger(arg3[1])) {
					arg0.sendMessage("МАЛО ПАРАМЕТРОВ ИЛИ INDEX НЕ ЧИСЛО");
					break;
				}
				String info=((TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots")).GetPlotHelper().GetInfoPlots(Integer.parseInt(arg3[1]));
				arg0.sendMessage(info);
				break;
			case "help":
				arg0.sendMessage("/plot create [имя] [размер] - создать территорию");
				arg0.sendMessage("/plot remove - удалить территорию");
				String sendRules="";
				for(int i=0;i<Rules.values().length;i+=1) {
					sendRules+=Rules.values()[i].toString();
					if(i+1!=Rules.values().length)
						sendRules+="/";
				}
				arg0.sendMessage("/plot change  ["+sendRules+"]"+" [true/false] "+"- изменить правило");
				arg0.sendMessage("/plot player [имя] [op/deop/ban/pardon] - опнуть/деопнуть/забанить/разбанить игрока на террритори. Опнутые игроки могут делать всё то же, что и вы");
				arg0.sendMessage("/plot player list - выводит список забаненых и опнутых игроков");
				arg0.sendMessage("/plot info - информация о территории на который вы щас находитесь");
				arg0.sendMessage("/plot my - тп на свою территорию");
				arg0.sendMessage("/plot rtp - тп на рандомную территорию");
				arg0.sendMessage("/plot gm [0/1/2] - переключить свой режим на выживание/креатив/спектатор соотвественно");
				arg0.sendMessage("/plot tppos [X] [Y] [Z] - телепортирует на координаты x y z");
				arg0.sendMessage("/plot tp [игрок] - телепортирует на территорию игрока");
				arg0.sendMessage("/plot list [номер страницы] - Показать список территорий. Счёт страниц начинается с 0");
		}
		}
		for(String arg:arg3) {
			System.out.println("D:"+arg);
		}
		return true;
	}
	private PlotModel GetPlot(Location loc) {
		PlotModel modelPlot3=((TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots")).GetPlotFinder().FindByPosition(new Vector(loc.getBlockX(),loc.getBlockZ(),0));
		return modelPlot3;
	}
	private PlotModel GetPlot(Player player) {
		PlotModel modelPlot3=((TablePlot)DataBaseStorage.Get("plotsDB").GetTable("Plots")).GetPlotFinder().FindByName(player.getName());
		if(modelPlot3==null)
		{
			player.sendMessage("У ВАС НЕТ ТЕРРИТОРИИ");
			return null;
		}
		return modelPlot3;
	}
}


