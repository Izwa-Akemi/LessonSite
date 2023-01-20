package lesson.com.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lesson.com.model.dao.TransactionHistoryDao;
import lesson.com.model.entity.LessonEntity;
import lesson.com.model.entity.TransactionHistoryEntity;


@Service
public class TransactoinHistoryService {
	@Autowired
	private TransactionHistoryDao transactionHistoryDao;
	// 保存処理
		public void createTransactoinHistory(Long studentId,int amount) {
			LocalDateTime transactionDate = LocalDateTime.now();
			transactionHistoryDao.save(new TransactionHistoryEntity(studentId,amount,transactionDate));
		}
		public TransactionHistoryEntity getTransactoinId(Long studentId) {
			return transactionHistoryDao.findByStudentId(studentId);
		}

}