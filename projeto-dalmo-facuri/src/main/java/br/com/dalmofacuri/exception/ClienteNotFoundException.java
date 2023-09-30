package br.com.dalmofacuri.exception;

import javax.persistence.EntityNotFoundException;

public class ClienteNotFoundException extends EntityNotFoundException {
	
	 public ClienteNotFoundException(Long id) {
		 super(String.format("Cliente com id %s não existe", id));
	 }
}
