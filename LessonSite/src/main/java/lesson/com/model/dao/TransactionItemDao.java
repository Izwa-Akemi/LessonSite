package lesson.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lesson.com.model.entity.TransactionItemEntity;

public interface TransactionItemDao extends JpaRepository<TransactionItemEntity, Long> {
	TransactionItemEntity save(TransactionItemEntity transactionItemEntity);
}
