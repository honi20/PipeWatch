package com.pipewatch.domain.pipelineModel.service;

import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelRequest;
import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelResponse;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PipelineModelService {
	PipelineModelResponse.FileUploadDto uploadImage(Long userId, MultipartFile file) throws IOException;

	PipelineModelResponse.FileUploadDto uploadFile(Long userId, MultipartFile file) throws IOException, ParseException;

	PipelineModelResponse.CreateModelingDto createModeling(PipelineModelRequest.ModelingDto requestDto) throws IOException, ParseException;

	void subTransactionForCreateModel(String modelUuid);

	void initModel(Long userId, Long modelId, PipelineModelRequest.InitDto requestDto);

	PipelineModelResponse.ListDto getModelList(Long userId, String building, Integer floor);

	PipelineModelResponse.DetailDto getModelDetail(Long userId, Long modelId);

	void modifyModel(Long userId, Long modelId, PipelineModelRequest.ModifyDto requestDto);

	void modifyThumbnailModel(Long userId, Long modelId, MultipartFile file) throws IOException;

	void deleteModel(Long userId, Long modelId);

	PipelineModelResponse.MemoListDto getModelMemoList(Long userId, Long modelId);

	PipelineModelResponse.MemoListDto createModelMemo(Long userId, Long modelId, PipelineModelRequest.MemoDto requestDto);

	void deleteModelMemo(Long userId, Long memoId);
}
