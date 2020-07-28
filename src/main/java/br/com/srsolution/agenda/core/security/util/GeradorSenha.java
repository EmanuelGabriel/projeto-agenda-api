package br.com.srsolution.agenda.core.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

	public static void main(String[] args) {
		gerarSenhaCriptografada();
	}

	private static void gerarSenhaCriptografada() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("@ngul@r-@654321"));
	}

}
