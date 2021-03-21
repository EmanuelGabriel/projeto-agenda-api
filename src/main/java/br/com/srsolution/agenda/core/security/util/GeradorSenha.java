package br.com.srsolution.agenda.core.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

	public static void main(String[] args) {
		gerarSenhaCriptografada();
	}

	private static void gerarSenhaCriptografada() {
		String senhaEncoder = "usuario123";

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String senhaCripto = encoder.encode(senhaEncoder);
		System.out.println(senhaCripto);
	}

}
