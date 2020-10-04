package com.desafios.demo.automacao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.core.io.ClassPathResource;

public class DesafioDosNicks {

	private static final String CAMINHO_WEB_DRIVER = "C:\\\\chromedriver.exe";
	private static final String INPUT_METODO = "method";
	private static final String INPUT_QUANTIDADE = "quantity";
	private static final String NUMERO_DE_LETRAS = "limit";
	private static final String SITE_GERADOR_DE_NICKS = "https://www.4devs.com.br/gerador_de_nicks";
	private static final String SITE_GERADOR_CPF = "https://www.4devs.com.br/gerador_de_cpf";

	static List<WebElement> todosOsNicks;
	static List<String> listaDeNicks = new ArrayList<String>();
	static List<String> listaDeNicksComCPF = new ArrayList<String>();

	public static void main(String[] args) {
		System.out.println(CAMINHO_WEB_DRIVER);
		System.setProperty("webdriver.chrome.driver", CAMINHO_WEB_DRIVER);

		WebDriver driver = new ChromeDriver();
		driver.get(SITE_GERADOR_DE_NICKS);

		selecionarMetodo(driver);
		inserirQuantidadeDeNicks(driver);
		selecionarNumeroDeLetras(driver);
		clicarBotaoGerarNicks(driver);

		listarTodosOsNicksGerados(listaDeNicks, driver);

		driver.get(SITE_GERADOR_CPF);
		String nickComCpf;
		for (String nick : listaDeNicks) {
			try {
				nickComCpf = nick + ";" + gerarCPF(driver);
				listaDeNicksComCPF.add(nickComCpf);
				salvarArquivoTxt(nickComCpf);
				System.out.println(nickComCpf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void listarTodosOsNicksGerados(List<String> listaDeNicks, WebDriver driver) {
		List<WebElement> todosOsNicks;
		todosOsNicks = driver.findElements(By.className("generated-nick"));

		for (WebElement nick : todosOsNicks) {
			listaDeNicks.add(nick.getText());
			System.out.println(nick.getText());
		}
	}

	private static void selecionarMetodo(WebDriver driver) {
		driver.findElement(By.name(INPUT_METODO)).sendKeys(Keys.ENTER);
		driver.findElement(By.name(INPUT_METODO)).sendKeys(Keys.ARROW_DOWN);
		driver.findElement(By.name(INPUT_METODO)).sendKeys(Keys.ENTER);
	}

	private static void inserirQuantidadeDeNicks(WebDriver driver) {
		driver.findElement(By.name(INPUT_QUANTIDADE)).sendKeys(Keys.DELETE);
		driver.findElement(By.name(INPUT_QUANTIDADE)).sendKeys(Keys.DELETE);
		driver.findElement(By.name(INPUT_QUANTIDADE)).sendKeys("50");
	}

	private static void selecionarNumeroDeLetras(WebDriver driver) {
		driver.findElement(By.name(NUMERO_DE_LETRAS)).sendKeys("8");
	}

	private static void clicarBotaoGerarNicks(WebDriver driver) {
		driver.findElement(By.className("btn-cta")).sendKeys(Keys.ENTER);
	}

	private static String gerarCPF(WebDriver driver) {
		List<WebElement> CPF;
		driver.findElement(By.className("btn-cta")).sendKeys(Keys.ENTER);
		aguardar(600);
		CPF = driver.findElements(By.id("texto_cpf"));
		return CPF.get(0).getText();
	}

	private static void aguardar(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/* Salva os nicks com CPF gerado no arquivo */
	private static void salvarArquivoTxt(String nickComCPF) {
		try {
            //File file = new ClassPathResource("nickscomcpf.txt").getFile();
			File file = new File("nickscomcpf.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true);
            //BufferedWriter bw = new BufferedWriter(fw);
            fw.write(nickComCPF + "\n");
            //fw.newLine();
            fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
