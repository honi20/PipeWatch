package com.pipewatch.domain.pipelineModel.repository;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.pipeline.model.entity.Pipe;
import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pipewatch.domain.enterprise.model.entity.QBuildingAndFloor.buildingAndFloor;
import static com.pipewatch.domain.pipeline.model.entity.QPipe.pipe;
import static com.pipewatch.domain.pipeline.model.entity.QPipeline.pipeline;
import static com.pipewatch.domain.pipelineModel.model.entity.QPipelineModel.pipelineModel;

@Repository
@RequiredArgsConstructor
public class PipelineModelCustomRepositoryImpl implements PipelineModelCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<PipelineModel> findAllByBuildingAndFloor(Enterprise enterprise, String building, Integer floor) {
		JPAQuery<PipelineModel> query = queryFactory.selectFrom(pipelineModel)
				.leftJoin(buildingAndFloor).on(buildingAndFloor.eq(pipelineModel.buildingAndFloor))
				.where(pipelineModel.enterprise.eq(enterprise))
				.orderBy(pipelineModel.updatedAt.desc());

		if (building != null) {
			query.where(buildingAndFloor.name.eq(building));
		}
		if (floor != null) {
			query.where(buildingAndFloor.floor.eq(floor));
		}

		return query.fetch();
	}

	@Override
	public List<Pipe> findPipeByModel(Long modelId) {
		return queryFactory.selectFrom(pipe)
				.leftJoin(pipeline).on(pipeline.eq(pipe.pipeline))
				.where(pipeline.pipelineModel.id.eq(modelId))
				.orderBy(pipe.id.asc())
				.fetch();
	}
}
