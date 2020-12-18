package app;

public class Launcher {			//	

	public static void main(String[] args) throws InterruptedException {
		
		Resources app = new Resources();
		
		for (String arg : args) {											// Obter parâmetros de inicialização
			
			if (arg.equalsIgnoreCase("debug")) {							// Ativar debug mode
				
				Resources.debug = true;
				System.out.println("\n##   Debug mode Ativado   ##\n");
			}
			
			if (arg.contains("force-")) {									// Forçar mineração
				
				String[] argForce = arg.split("-");
				
				if ( !argForce[1].equalsIgnoreCase("BTC") && !argForce[1].equalsIgnoreCase("GHS6")) {
					System.out.println("\nNão é possível forçar a mineração na opção inserida: " + argForce[1]);
					System.out.println("\nOpções disponíveis: BTC GHS6\n");
					System.exit(0);
				}
				
				Resources.isForced = true;
				app.setMining(argForce[1].toUpperCase());
				
				System.out.println("\n##   Forçando mineração de " + argForce[1].toUpperCase() + "   ##\n");
			}
			
			if (arg.equalsIgnoreCase("noTG")) {								// Não enviar mensagens no Telegram
				
				Resources.noTG = true;
				System.out.println("\n##	Não enviar mensagens no Telegram	##\n");
			}
		}	
		
/*	=========================================================================================================================	*/		
		
		final boolean testMode = false;
		
		if (testMode) {
			
			Resources.isForced = true;		Resources.debug = true;		Resources.noTG = false;
			
			app.getOpen();
			app.info();
			app.VisaoGeral();
			Thread.sleep(130000);		// Pausa de 120k ms = 120 s
			app.info();
			app.setTG();
			
		}
		
		else {
		
			app.getOpen();
		
			for (int loop = 0; loop < 360; loop++) {

				app.info();

				if (loop == 0 || loop == (360*0.5)) {	app.VisaoGeral();	}	// Visão geral no Telegram a cada 6 hr
			
				if (loop == 359) {												// Zera o contador para iniciar novamente
					
					app.getOpen();
					loop = -1;
				}
			
				Thread.sleep(120000);		// Pausa de 120k ms = 120 s
			}
		}
	} 
}