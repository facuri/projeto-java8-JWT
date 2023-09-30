package br.com.dalmofacuri.exception;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.dalmofacuri.exception.Problema.Campo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestControllerAdvice // Alex
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Problema.Campo> campos = new ArrayList<>();

		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			campos.add(new Problema.Campo(nome, mensagem));
		}
		Problema problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo("Um ou mais campos estão inválidos. Faça o preenchimento correto!");
		problema.setCampos(campos);

		return handleExceptionInternal(ex, problema, headers, status, request);

	}

	// Interceptar erros mais comuns no projeto
	// A maioria das execões acabam nessa classe pai:Exception.class(),
	// RuntimeException.class:Java Virtual Machine, Throwable.class(Classe de Erros
	// também)
	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String msg = "";

		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n";
			}
		} else {
			msg = ex.getMessage();
		}

		ObjetoErro objetoErro = new ObjetoErro();
		objetoErro.setError(msg);
		objetoErro.setCode(status.value() + " ==> " + status.getReasonPhrase());// int

		return new ResponseEntity<>(objetoErro, headers, status);

	}

	// Tratamento da maioria dos erro a nível de banco de dados
	// DataIntegrityViolationException(deletar registro que tenha chave dependendo
	// dela(integridade), PSQLException(Postgresql))
	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, PSQLException.class,
			SQLException.class })
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {

		String msg = "";

		if (ex instanceof DataIntegrityViolationException) {

			msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();

		} else if (ex instanceof ConstraintViolationException) {

			msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();

		} else if (ex instanceof PSQLException) {

			msg = ((PSQLException) ex).getCause().getCause().getMessage();

		} else if (ex instanceof SQLException) {

			msg = ((SQLException) ex).getCause().getCause().getMessage();

		} else {

			msg = ex.getMessage(); // outras mensagens de erros mais genericas
		}

		ObjetoErro objectError = new ObjetoErro();
		objectError.setError(msg);
		objectError.setCode(
				HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

		return new ResponseEntity<>(objectError, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
