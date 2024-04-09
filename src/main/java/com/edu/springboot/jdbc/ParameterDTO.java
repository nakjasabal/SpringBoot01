package com.edu.springboot.jdbc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterDTO {	
	private String searchField;  
	private String searchKeyword;
	private int start;		 
	private int end; 
}



