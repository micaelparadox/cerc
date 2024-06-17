package com.cerc.tio.financial.asset.tr.layout.middleware.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagAdvancementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagReceivablesResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagSettlementsResponse;
import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagResponseItem;
import com.cerc.tio.financial.asset.tr.layout.middleware.service.TioMiddlewareCdpTagReceivableApiService;
import com.cerc.tio.libcdpcommon.exception.FailedBatchTransactionException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TioMiddlewareCdpTagReceivableApiController.class)
class TioMiddlewareCdpTagReceivableApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TioMiddlewareCdpTagReceivableApiService service;

	@MockBean
	private com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.service.AuthService authService;

	private final EasyRandom easyRandom = new EasyRandom();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void registerReceivable_shouldReturnCreatedResponse_whenCalled() throws Exception {
		// given
		TagReceivablesRequest tagRequest = easyRandom.nextObject(TagReceivablesRequest.class);
		TagReceivablesResponse tagResponse = TagReceivablesResponse.builder().processKey("12345").build();

		// when
		when(service.sendReceivable(any(), any())).thenReturn(tagResponse);

		// then
		mockMvc.perform(post("/receivable").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.processKey").value(tagResponse.getProcessKey()));
	}

	@Test
	void registerReceivable_shouldReturnBadRequest_whenServiceThrowsException() throws Exception {
		// given
		TagReceivablesRequest tagRequest = easyRandom.nextObject(TagReceivablesRequest.class);
		List<String> errors = List.of("Error1", "Error2");

		// when
		when(service.sendReceivable(any(), any()))
				.thenThrow(FailedBatchTransactionException.of(errors, "correlationId"));

		// then
		mockMvc.perform(post("/receivable").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]").value("Error1"));
	}

	@Test
	void registerAdvancement_shouldReturnCreatedResponse_whenCalled() throws Exception {
		// given
		TagAdvancementsRequest tagRequest = easyRandom.nextObject(TagAdvancementsRequest.class);
		TagAdvancementsResponse tagResponse = TagAdvancementsResponse.builder().processKey("12345")
				.advancements(List.of(TagResponseItem.builder().reference("reference").key("key").build()))
				.errors(Collections.emptyList()).build();

		// when
		when(service.sendAdvancement(any(), any())).thenReturn(tagResponse);

		// then
		mockMvc.perform(post("/receivable/advancement").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.processKey").value(tagResponse.getProcessKey()));
	}

	@Test
	void registerAdvancement_shouldReturnBadRequest_whenServiceThrowsException() throws Exception {
		// given
		TagAdvancementsRequest tagRequest = easyRandom.nextObject(TagAdvancementsRequest.class);
		List<String> errors = List.of("Error1", "Error2");

		// when
		when(service.sendAdvancement(any(), any()))
				.thenThrow(FailedBatchTransactionException.of(errors, "correlationId"));

		// then
		mockMvc.perform(post("/receivable/advancement").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]").value("Error1"));
	}

	@Test
	void registerAdvancement_shouldReturnBadRequest_whenNoAdvancements() throws Exception {
		// given
		TagAdvancementsRequest tagRequest = easyRandom.nextObject(TagAdvancementsRequest.class);
		TagAdvancementsResponse tagResponse = TagAdvancementsResponse.builder().processKey("12345")
				.advancements(Collections.emptyList()).errors(Collections.emptyList()).build();

		// when
		when(service.sendAdvancement(any(), any())).thenReturn(tagResponse);

		// then
		mockMvc.perform(post("/receivable/advancement").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isBadRequest());
	}

	@Test
	void registerSettlement_shouldReturnCreatedResponse_whenCalled() throws Exception {
		// given
		TagSettlementsRequest tagRequest = easyRandom.nextObject(TagSettlementsRequest.class);
		TagSettlementsResponse tagResponse = TagSettlementsResponse.builder().processKey("12345")
				.settlements(List.of(TagResponseItem.builder().reference("reference").key("key").build()))
				.errors(Collections.emptyList()).build();

		// when
		when(service.sendSettlement(any(), any())).thenReturn(tagResponse);

		// then
		mockMvc.perform(patch("/receivable/settlement").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.processKey").value(tagResponse.getProcessKey()));
	}

	@Test
	void registerSettlement_shouldReturnBadRequest_whenServiceThrowsException() throws Exception {
		// given
		TagSettlementsRequest tagRequest = easyRandom.nextObject(TagSettlementsRequest.class);
		List<String> errors = List.of("Error1", "Error2");

		// when
		when(service.sendSettlement(any(), any()))
				.thenThrow(FailedBatchTransactionException.of(errors, "correlationId"));

		// then
		mockMvc.perform(patch("/receivable/settlement").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]").value("Error1"));
	}

	@Test
	void registerSettlement_shouldReturnBadRequest_whenNoSettlements() throws Exception {
		// given
		TagSettlementsRequest tagRequest = easyRandom.nextObject(TagSettlementsRequest.class);
		TagSettlementsResponse tagResponse = TagSettlementsResponse.builder().processKey("12345")
				.settlements(Collections.emptyList()).errors(List.of("Error1")).build();

		// when
		when(service.sendSettlement(any(), any())).thenReturn(tagResponse);

		// then
		mockMvc.perform(patch("/receivable/settlement").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]").value("Error1"));
	}

	@Test
	void registerSettlement_shouldReturnAccepted_whenPartialErrors() throws Exception {
		// given
		TagSettlementsRequest tagRequest = easyRandom.nextObject(TagSettlementsRequest.class);
		TagSettlementsResponse tagResponse = TagSettlementsResponse.builder().processKey("12345")
				.settlements(List.of(TagResponseItem.builder().reference("reference").key("key").build()))
				.errors(List.of("Error1")).build();

		// when
		when(service.sendSettlement(any(), any())).thenReturn(tagResponse);

		// then
		mockMvc.perform(patch("/receivable/settlement").header("company-document", "companyDocument")
				.header("company-id", "companyId").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagRequest))).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.processKey").value(tagResponse.getProcessKey()))
				.andExpect(jsonPath("$.errors[0]").value("Error1"));
	}
}
