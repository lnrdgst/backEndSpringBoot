package com.viasoft.servnf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.viasoft.servnf.model.ServNf;
import com.viasoft.servnf.repository.ServNfRepository;

@RestController
@RequestMapping("/ServNf")
public class ServNfController {

	@Autowired
	private ServNfRepository repository;
	private static List<Element> lista;

	public static void main(String[] args) { // Método main que popula a lista de informações

		Timer timer = new Timer();
		final long mins = (1000 * 300); // 5 minutos
		TimerTask operacao = new TimerTask() {

			public void run() { // job com repetição de 5 em 5 minutos
				lista = new ArrayList<>();
				try {
					Document d = Jsoup.connect("https://www.nfe.fazenda.gov.br/portal/disponibilidade.aspx").get();
					Elements dados = d.getElementById("ctl00_ContentPlaceHolder1_gdvDisponibilidade2")
							.getElementsByTag("tbody");

					for (int i = 0; i < 50; i++) {
						Element post = dados.get(i);
						System.out.println(post.getElementsByTag("tr").text());
						lista.add(post);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		timer.scheduleAtFixedRate(operacao, 0, mins);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ServNf adicionar(@RequestBody ServNf nf) { // Método que armazena no banco de dados de memória H2

		for (Element dado : lista) {
			nf.setEstado(dado.getElementsByTag("tbody").text());
			nf.setStatus(dado.getElementsByTag("tr").text());
			nf.setData(new Date());
			repository.save(nf);
		}
		return nf;
	}

	@GetMapping
	public ServNf listarAll(ServNf nf) { // Método que lê do banco de dados todas as informações armazenadas 

		repository.findAll();
		return nf;
	}
	
	@GetMapping
	public ServNf listarByEstado(ServNf nf, @RequestParam (value="Estado") String estado) { // Método que lê do banco de dados as informações por estado
		return nf;
	}
	
	@GetMapping
	public ServNf listarByData(ServNf nf, @RequestParam (value="Data") Date data) { // Método que lê do banco de dados as informações por data
		return nf;
	}
	
	
	
	
}
