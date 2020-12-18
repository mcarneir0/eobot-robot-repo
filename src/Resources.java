package app;

import java.net.URI;
import java.time.ZoneId;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.net.http.HttpResponse.BodyHandlers;

public class Resources {
	
	private static final String
		ID = "2312598",
		API = "25a018891381480a9f21",
		email = "matheusfeitosa@outlook.com",
		baseURL = "https://www.eobot.com/api.aspx?";		/*   Eobot info   */
	
	private static final String
		TelegramURL = "https://api.telegram.org/bot958905117:AAGkBoRdE3rdwMal4r7tEA6gee0wCbkLWUc/",		/*   Telegram info   */
		chatID = "376293662";
	
	static final DecimalFormat
		df = new DecimalFormat("#,##0.00"),							/*   Formatadores de double   */
		porc = new DecimalFormat("#0.00");
	
	static final DateTimeFormatter									/*	Definições do horário	*/
		dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	static HttpClient client = HttpClient.newHttpClient();			/*   Recursos do HTTP   */
	static HttpResponse<String> response;
	static HttpRequest req;
	
	private double													/*   Variáveis do programa   */
		btcP = 0,		btcPAnt = 0,
		open = 0,		perc = 0;
	
	private int offCounter = 0;
	
	private String
		ghs5 = "",		ghs5Ant = "",
		ghs6 = "",		ghs6Ant = "",		ghs6Ini = "0.0",		ghs6G = "0.0",
		mining = "",	miningAnt = "",
		saldo = "",		saldoAnt = "",		saldoIni = "0.0",		saldoG = "0.0";
	
	static boolean
		var = false,		isForced = false,
		debug = false,		noTG = false,
		ganhoBTC = false,	ganhoGHS6 = false;
		
	
	static String													/*   Textos do Telegram   */
		btcT = "",		btcPT = "",		btcGT = "",
		ghs5T = "",		ghs6T = "",		ghs6GT = "";
	
	static final String
		vg = "_Visão%20Geral_%0A",
		minBTC =  "%C2%AB%20%20_Minerando%20BTC_%20%20%C2%BB%0A",
		minGHS6 = "%C2%AB%20%20_Minerando%20GHS%206.0_%20%20%C2%BB%0A";
	
	
	
	public double getOpen() {
		
		req = HttpRequest.newBuilder(URI.create("https://api-pub.bitfinex.com/v2/platform/status")).build();	// Status do servidor; 1 = OK, 0 = manutenção
		
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no getOpen\n");
			sendMessage(erroT("Erro%20de%20conexão%20no%20getOpen%20!!!"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.body().contains("0")) {	checkOn(response.body());	}
		
		req = HttpRequest.newBuilder(URI.create("https://api-pub.bitfinex.com/v2/candles/trade:1D:tBTCUSD/last")).build();
		
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no getOpen\n");
			sendMessage(erroT("Erro%20de%20conexão%20no%20getOpen%20!!!"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.statusCode() != 200) {
			System.out.println("\nErro de conexão no getOpen\n");
			sendMessage(erroT("Erro%20de%20conexão%20no%20getOpen%20!!!"));
			System.exit(0);
		}
		
		String[] separador = response.body().split("\\,");		// Separa as informações em linhas a cada vírgula
		
		if (debug) {
			System.out.println("\n/*\tgetOpen\n*");
			System.out.println("* " + req + "\n*");
			for (int i = 0; i < separador.length; i++) { System.out.println("* " + separador[i] + "\t" + i); }	// imprimir tudo em linhas
			System.out.println("\n* Return = " + separador[1] + "\n*/");
		}
		
		return open = Double.parseDouble(separador[1]);
	}
	
	public double getBTCprice() {
		
		req = HttpRequest.newBuilder(URI.create(baseURL + "coin=BTC")).build();
		
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no getBTCprice\n");
			sendMessage(erroT("Algo%20deu%20errado%20no%20getBTCprice%20!!!"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.statusCode() != 200) {
			System.out.println("\nErro de conexão no getBTCprice\n");
			return btcPAnt;
		}
		
		if (response.body().contains("off") || response.body().contains("temp")){
			
			offCounter++;
			
			if (offCounter == 3) {	checkOn(response.body());	}
			
			else if (btcPAnt == 0) {	checkOn(response.body());	}
			
			return btcPAnt;
		}
		
		if (debug) {
			System.out.println("\n/*\tgetBTCprice\n*");
			System.out.println("* " + req + "\n*");
			System.out.println("* Return = " + response.body() + "\n*/");
		}
		
		btcP = Double.parseDouble(response.body());
		btcPAnt = btcP;
		return btcP;
	}
	
	public String getMining() {
		
		req = HttpRequest.newBuilder(URI.create(baseURL + "idmining=" + ID)).build();
		
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no getMining\n");
			sendMessage(erroT("Algo%20deu%20errado%20no%20getMining%20!!!"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.statusCode() != 200) {
			System.out.println("\nErro de conexão no getMining\n");
			return miningAnt;
		}
		
		if (response.body().contains("off") || response.body().contains("temp")){
			
			offCounter++;
			
			if (offCounter == 3) {	checkOn(response.body());	}
			
			else if (miningAnt.isBlank()) {	checkOn(response.body());	}
			
			return miningAnt;
		}
		
		if (debug) {
			System.out.println("\n/*\tgetMining\n*");
			System.out.println("* " + req + "\n*");
			System.out.println("* Return = " + response.body() + "\n*/");
		}
		
	//	if (response.body().contains("BTC"))	{ mining = "BTC";	}
	//	if (response.body().contains("GHS6"))	{ mining = "GHS 6.0";	}
		
		mining = (response.body().contains("BTC")) ? "BTC" : "GHS 6.0";
		
		miningAnt = mining;
		return mining;
	}
		
	public String getBTC() {
		
		req = HttpRequest.newBuilder(URI.create(baseURL + "total=" + ID)).build();
		
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no getBTC\n");
			sendMessage(erroT("Algo%20deu%20errado%20no%20getBTC%20!!!"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.statusCode() != 200) {
			System.out.println("\nErro de conexão no getBTC\n");
			return saldoAnt;
		}
		
		if (response.body().contains("off") || response.body().contains("temp")){
			
			offCounter++;
			
			if (offCounter == 3) {	checkOn(response.body());	}
			
			else if (saldoAnt.isBlank()) {	checkOn(response.body());	}
			
			return saldoAnt;
		}
		
		String[] separador = response.body().split("\\;");		// Separa as informações em linhas a cada ;
		
		if (debug) {
			System.out.println("\n/*\tgetBTC\n*");
			System.out.println("* " + req + "\n*");
			for (int i = 0; i < separador.length; i++) { System.out.println("* " + separador[i] + "\t" + i); }	// imprimir tudo em linhas
			System.out.println("*\n* Return = " + separador[1] + "\t(1)\n*/");
		}
		
		for (int i = 0 ; i < separador.length ; i++) {
			if (separador[i].contains("BTC")) {
				saldo = separador[i].substring(4);		//apaga os 4 primeiros chars
				break;
			}
		}
		
		saldoAnt = saldo;
		return saldo;
	}
	
	public String getGHS5() {
		
		req = HttpRequest.newBuilder(URI.create(baseURL + "total=" + ID)).build();
		
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no getGHS5\n");
			sendMessage(erroT("Algo%20deu%20errado%20no%20getGHS5%20!!!"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.statusCode() != 200) {
			System.out.println("\nErro de conexão no getGHS5\n");
			return ghs5Ant;
		}
		
		if (response.body().contains("off") || response.body().contains("temp")){
			
			offCounter++;
			
			if (offCounter == 3) {	checkOn(response.body());	}
			
			else if (ghs5Ant.isBlank()) {	checkOn(response.body());	}
			
			return ghs5Ant;
		}
		
		String[] separador = response.body().split("\\;");		// Separa as informações em linhas a cada ;
		
		if (debug) {
			System.out.println("\n/*\tgetGHS5\n*");
			System.out.println("* " + req + "\n*");
			for (int i = 0; i < separador.length; i++) { System.out.println("* " + separador[i] + "\t" + i); }	// imprimir tudo em linhas
			System.out.println("*\n* Return = " + separador[25] + "\t(25)\n*/");
		}
		
		for (int i=0;i<separador.length;i++) {
			if (separador[i].contains("GHS5")) {
				ghs5 = separador[i].substring(5);		// Apaga os 5 primeiros chars
				break;
			}
		}
		
		ghs5Ant = ghs5;
		return ghs5;
	}
	
	public String getGHS6() {
		
		req = HttpRequest.newBuilder(URI.create(baseURL + "total=" + ID)).build();
		
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no getGHS6\n");
			sendMessage(erroT("Algo%20deu%20errado%20no%20getGHS6%20!!!"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.statusCode() != 200) {
			System.out.println("\nErro de conexão no getGHS6\n");
			return ghs6Ant;
		}
		
		if (response.body().contains("off") || response.body().contains("temp")){
			
			offCounter++;
			
			if (offCounter == 3) {	checkOn(response.body());	}
			
			else if (ghs6Ant.isBlank()) {	checkOn(response.body());	}
			
			return ghs6Ant;
		}
		
		String[] separador = response.body().split("\\;");		// Separa as informações em linhas a cada ;
		
		if (debug) {
			System.out.println("\n/*\tgetGHS6\n*");
			System.out.println("* " + req + "\n*");
			for (int i = 0; i < separador.length; i++) { System.out.println("* " + separador[i] + "\t" + i); }	// imprimir tudo em linhas
			System.out.println("*\n* Return = " + separador[26] + "\t(26)\n*/");
		}
		
		for (int i=0 ; i<separador.length ; i++) {
			if (separador[i].contains("GHS6")) {
				ghs6 = separador[i].substring(5);		// Apaga os 5 primeiros chars
				break;
			}
		}
		
		ghs6Ant = ghs6;
		return ghs6;
	}
		
	static public String getTime() {
		
		LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT-3"));
		return dtf.format(now);
	}
	
	public void setMining(String min) {
		
		req = HttpRequest.newBuilder(URI.create(baseURL + "id=" + ID + "&email=" + email + "&password=" + API + "&mining=" + min.toUpperCase())).build();
		
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no setMining\n");
			sendMessage(erroT("Algo%20deu%20errado%20no%20setMining"));
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.statusCode() != 200) {	System.out.println("\nErro de conexão no setMining\n"); }
		
		if (debug) {
			System.out.println("\n/*\tsetMining\n*");
			System.out.println("* " + req + "\n*/");
		}
		
		return;
	}

	public void sendMessage(String txt) {
		
		if (noTG) {	return;	}
		
		req = HttpRequest.newBuilder(URI.create(TelegramURL + "sendMessage?parse_mode=Markdown&chat_id=" + chatID + "&text=" + txt)).build();
				
		try {	response = client.send(req, BodyHandlers.ofString());	}
		
		catch (IOException | InterruptedException e) {
			System.out.println("\nAlgo deu errado no sendMessage\n");
			e.printStackTrace();
			System.exit(0);
		}
		
		if (response.statusCode() != 200) {	System.out.println("\nErro de conexão no sendMessage\n"); }
		
		if (debug) {
			System.out.println("\n/*\tsendMessage\n*");
			System.out.println("* " + req + "\n*/");
		}
		
		return;
	}
	
	public void setTG() {
		
		if (noTG) {	return;	}
		
		btcT =  "%0A*BTC:*%20%60" + saldo + "%60";
		btcGT = "%0A(%20" + ((ganhoBTC) ? ("%2B%20%60" + saldoG.substring(2) + "%60%20)") : ("-%20%60" + saldoG.substring(2) + "%60%20%20)"));
//		ghs5T = "%0A*GHS%205.0:*%20%60" + ghs5.substring(0, 8) + "%60";
		ghs6T = "%0A*GHS%206.0:*%20%60" + ghs6.substring(0, 7) + "%60";
		if (ghs6G.length() > 8) {	ghs6GT = "%0A(%20" + ((ganhoGHS6) ? ("%2B%20%60" + ghs6G.substring(2, 8) + "%60%20)") : ("-%20%60" + ghs6G.substring(2, 8) + "%60%20%20)"));	}
		else {	ghs6GT = "%0A(%20" + ((ganhoGHS6) ? ("%2B%20%60" + ghs6G.substring(2) + "%60%20)") : ("-%20%60" + ghs6G.substring(2) + "%60%20%20)"));	}
		btcPT = "%0A*1%20BTC%20*=%20$%20%60" + df.format(btcP) + "%60";
		
		if (debug) {
			System.out.println("\n/*\tsetTG\n*");
			System.out.println("* btcT = " + btcT);
			System.out.println("* btcGT = " + btcGT);
			System.out.println("* ghs6T = " + ghs6T);
			System.out.println("* ghs6GT = " + ghs6GT);
			System.out.println("* btcPT = " + btcPT);
			System.out.println("*/");
		}
		
		return;
	}
	
	public void checkOn(String resp) {
		
		if (debug) {
			System.out.println("\n/*\tcheckOn\n*");
			System.out.println("* String = " + resp + "\n*/");
		}
		
		if (resp.contains("off") || resp.contains("temp") || resp.isBlank() || resp.contentEquals("[0]"))	{	// Encerra o programa nessas condições
			
			sendMessage(erroT("EOBOT%20OFFLINE!"));
			System.out.println("\n###\tEobot Offline\t###\n");
			
			if (resp.isBlank()) {				resp = "~ Resposta em branco ~";	}
			if (resp.contentEquals("[0]")) {	resp = "Servidor offline";	}
			
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();		// Identifica qual método chamou o checkOn
			StackTraceElement element = stack[2];
			
			System.out.println("Causado pelo:\t" + element.getMethodName());
			System.out.println("\nDevido a:\t" + resp);
			System.out.println("\n" + getTime() + "\n");
			System.exit(0);
		}
	}
	
	public String erroT(String txt) {
		
	/*	space = %20		URL Encoding
	 * 	enter = %0A
	 * 	# = %23	
	 *	« = %C2%AB
	 *	» = %C2%BB
	 *	+ = %2B
	 */
		
		return "*%23%20%20%20%20%20" + txt + "%20%20%20%20%20%23*";
	}
	
	public void VisaoGeral() {
		
		ghs6Ini = ghs6;
		saldoIni = saldo;
		
		if (noTG == false) {
			setTG();
			sendMessage(vg + btcT + btcGT + ghs6T + ghs6GT + btcPT);
		}
		
		return;
	}
	
	public void calcGanho() {
		
		if (ghs6Ini.equals("0.0")) { ganhoBTC = true;	ganhoGHS6 = true;	return;	}
		
		ghs6G =		new BigDecimal(Double.parseDouble(ghs6) - Double.parseDouble(ghs6Ini)).toPlainString();		// evitar notação científica
		saldoG =	new BigDecimal(Double.parseDouble(saldo) - Double.parseDouble(saldoIni)).toPlainString();	// evitar notação científica
		
		if (ghs6G.length() > 10) {
			
			ghs6G = (Double.parseDouble(ghs6G) < 0) ? "- " + ghs6G.substring(1, 11) : "+ " + ghs6G.substring(0, 10);
			ganhoGHS6 = (ghs6G.startsWith("+")) ? true : false;
		}
		
		else {
			
			ghs6G = (Double.parseDouble(ghs6G) < 0) ? "- " + ghs6G : "+ " + ghs6G;
			ganhoGHS6 = (ghs6G.startsWith("+")) ? true : false;
		}
		
		if (saldoG.length() > 10) {
			
			saldoG = (Double.parseDouble(saldoG) < 0) ? "- " + saldoG.substring(1, 11) : "+ " + saldoG.substring(0, 10);
			ganhoBTC = (saldoG.startsWith("+")) ? true : false;
		}
		
		else {
			
			saldoG = (Double.parseDouble(saldoG) < 0) ? "- " + saldoG : "+ " + saldoG;
			ganhoBTC = (saldoG.startsWith("+")) ? true : false;
		}
		
		if (debug) {
			System.out.println("\n/*\tcalcGanho\n*");
			System.out.println("* ghs6G = " + ghs6G);
			System.out.println("* saldoG = " + saldoG + "\n*/");
		}
		
		return;
	}
	
	
	
	
	
	public boolean calc() {
		
		getBTCprice();
		perc = ((btcP - open) / open) * 100;		// Calcula a variação de preço em porcentagem
		
		if (isForced) {
			getMining();
			return var = (perc >= 0) ? true : false;
		}
		
		getMining();
		
		if (btcP < open) {						// Se BTC está em baixa, minerar BTC
			
			if (mining.contains("BTC")) {	return var = false;	}
			
			else {
				setTG();
				setMining("BTC");
				sendMessage(minBTC + btcT + ghs6T);
				return var = false;
			}
		}
		
		else {									// Se BTC está em alta, minerar GHS
			
			if (mining.contains("GHS")) {	return var = true;	}
			
			else {
				setTG();
				setMining("GHS6");
				sendMessage(minGHS6 + btcT + ghs6T);
				return var = true;
			}
		}
	}
	
	public void info() {
		
		getBTC();
//		getGHS5();
		getGHS6();
		calc();
		calcGanho();
		
		System.out.println("\n" + getTime() + " -> Minerando " + mining);
		System.out.println("BTC: " + saldo + " ( " + saldoG + " )");
//		System.out.println("GHS 5.0: " + ghs5);
		System.out.println("GHS 6.0: " + ghs6 + " ( " + ghs6G + " )");
		System.out.print("1 BTC = $ " + df.format(btcP) + " ->");
		
		if (var) {			/*   BTC em alta   */
			System.out.print(" + $ " + df.format(btcP - open));
			System.out.print(" (+ " + porc.format(perc).concat(" %)\n"));
		}
		
		else {				/*   BTC em baixa   */
			System.out.print(" - $ " + df.format(btcP - open).substring(1));
			System.out.print(" (- " + porc.format(perc).substring(1).concat(" %)\n"));
		}
		offCounter = 0;
	}
}