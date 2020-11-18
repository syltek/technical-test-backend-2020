package com.playtomic.tests.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	private String errorMessage;
	
	private String exceptionMessage;
	
	private Integer errorCode;

}