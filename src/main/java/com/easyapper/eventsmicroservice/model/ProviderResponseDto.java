package com.easyapper.eventsmicroservice.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ProviderResponseDto {

	private List<ProviderDto> providers;
	
}
