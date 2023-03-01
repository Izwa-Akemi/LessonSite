package lesson.com.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
import lesson.com.model.entity.TransactionItemEntity;

public interface TransactionItemDao extends JpaRepository<TransactionItemEntity, Long> {
	TransactionItemEntity save(TransactionItemEntity transactionItemEntity);
	@Transactional
	List<TransactionItemEntity> deleteByTransactionId(Long transactionId);
}
